package tanls.service;

import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.database.AnswerDAO;
import tanls.database.LawyerDAO;
import tanls.database.QuestionDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Answer;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.entity.Quote;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

@Service
public class AnswerService 
{
	public static final Logger log = Logger.getLogger(AnswerService.class);
	
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired AnswerDAO answerDAO;
	@Autowired QuestionDAO questionDAO;
	@Autowired LawyerDAO lawyerDAO;
	@Autowired MailService mailService;
	@Autowired QuoteService quoteService;
	
    public Answer createAnswer(TanlsUser user, Answer answer) throws InvalidInputException
    {
    	Lawyer lawyer = tanlsUserDAO.getLawyer(user);
    	if( lawyer == null || !user.getLawyer() ) throw new InvalidInputException("You do not have permission to answer a question");
    	
    	// check no existing answer
    	Question question = questionDAO.get(answer.getQuestion().getId());
    	Answer existingAnswer = answerDAO.getLawyerAnswer(lawyer, question);
    	if( existingAnswer != null ) throw new InvalidInputException("You have already answered this question.");
    	
    	if( answer.getQuote() != null )
    	{
    		Quote quote = quoteService.createQuote(answer.getQuote(), lawyer, question);
    		answer.setQuote(quote);
    	}
    	
    	answer.setLawyer(lawyer);
    	answer.setStatus(Answer.STATUS_NEW);
    	answer.setQuestion(question);
    	answer.setTime(Calendar.getInstance().getTimeInMillis());
    	answerDAO.put(answer);
    	
    	if( lawyer.getPoints() == null ) lawyer.setPoints(1);
    	else lawyer.setPoints(lawyer.getPoints()+1);
    	lawyerDAO.update(lawyer);
    	
    	SendAnswerToCustomerThread thread = new SendAnswerToCustomerThread(answer);
    	thread.start();
    	
    	return answer;
    }
    
    public class SendAnswerToCustomerThread extends Thread
    {
    	Answer answer;
    	
    	public SendAnswerToCustomerThread(Answer answer)
    	{
    		this.answer = answer;
    	}
    	
    	@Override
    	public void run()
    	{
    		try 
    		{
				sendAnswerToCustomer(answer);
			} 
    		catch (AddressException e) 
    		{
				log.error(e);
			} 
    		catch (MessagingException e) 
    		{
				log.error(e);
			}
    	}
    }
    
    public void sendAnswerToCustomer(Answer answer) throws AddressException, MessagingException
    {
    	Customer customer = questionDAO.getWithCustomer(answer.getQuestion().getId()).getCustomer();
    	
    	if( customer.getEmailNotifications() != null && customer.getEmailNotifications() )
    	{
    		String to = customer.getUser().getEmail();
    		
    		String subject = "Answer received to Easy Law question";
    		StringBuffer messageBuffer = new StringBuffer();
    		messageBuffer.append("<p>Hi!</p>");
    		messageBuffer.append("<p>One of our lawyers has answered your question!</p>");
    		messageBuffer.append("<p><strong>Name: </strong>" + answer.getLawyer().getName() + "</p>");
    		if( !answer.getQuestion().getQuoteRequired() )
    		{
    		    messageBuffer.append("<h3>Response:</h3>");
    		    messageBuffer.append("<p>"+answer.getText()+"</p>");
    		}
    		messageBuffer.append("<h3>Your question was:</h3>");
    		messageBuffer.append("<p>"+answer.getQuestion().getText()+"</p>");
    		if( answer.getQuote() != null )
    		{
    			messageBuffer.append("<p>" + answer.getLawyer().getName() + " provided a proposal for legal services.</p>");
    			messageBuffer.append("<h3>Description of legal services:</h3>");
    			messageBuffer.append("<p>" + answer.getQuote().getText() + "</p>");
    			if( !answer.getQuestion().getProBono() )
    			{
    			    messageBuffer.append("<p><strong>Legal Fees (AUD): </strong>" + answer.getQuote().getLegalFees() + "</p>");
    			    messageBuffer.append("<p><strong>Disbursments: </strong> " + answer.getQuote().getDisbursments() + "</p>");
    			}
    		}
    		messageBuffer.append("<p>You can view this and any other replies");
    		if( answer.getQuote() != null ) {
    			messageBuffer.append(" and book the lawyer");
    		}
    		messageBuffer.append(" on the <a href='");
    		messageBuffer.append(mailService.getBaseUrl());
    		messageBuffer.append("/#!/customer/questions");
    		messageBuffer.append("'>web interface</a>.</p>");
    		messageBuffer.append("<p>Have a great day!</p>");
    		messageBuffer.append("<p>Easy Law</p>");
    		
    		mailService.sendMessageHTML(to, subject, messageBuffer.toString());
    	}
    }
    
    public Answer updateAnswerText(TanlsUser user, Answer answer) throws InvalidInputException
    {
        Lawyer currentLawyer = tanlsUserDAO.getLawyer(user);
        if( !user.getLawyer() || currentLawyer == null ) throw new InvalidInputException("You do not have permission to update this answer");
        Answer existingAnswer = answerDAO.getWithQuestion(answer.getId());
        if( existingAnswer == null ) throw new InvalidInputException("Answer not found");
        if( !existingAnswer.getLawyer().getId().equals(currentLawyer.getId()) )
        {
        	throw new InvalidInputException("You cannot modify an answer you did not write");
        }
        if( !existingAnswer.getText().equals(answer.getText()) )
        {
        	existingAnswer.setText(answer.getText());
        	answerDAO.update(existingAnswer);
        }
        return existingAnswer;
    }
    
    public Answer updateAnswerStatus(TanlsUser user, Answer answer) throws InvalidInputException
    {
        Customer currentCustomer = tanlsUserDAO.getCustomer(user);
        if( currentCustomer == null ) throw new InvalidInputException("You do not have permission to update this answer");
        Answer existingAnswer = answerDAO.getWithQuestion(answer.getId());
        if( existingAnswer == null ) throw new InvalidInputException("Answer not found");
        if( !existingAnswer.getQuestion().getCustomer().getId().equals(currentCustomer.getId()) )
        {
        	throw new InvalidInputException("You cannot modify an answer to a question that you did not write to be useful or not useful");
        }
        if( !existingAnswer.getStatus().equals(answer.getStatus()) )
        {
        	if( answer.getStatus().equals(Answer.STATUS_USEFUL) && existingAnswer.getStatus().equals(Answer.STATUS_NEW) )
        	{
        		Lawyer lawyer = existingAnswer.getLawyer();
        		if( lawyer.getPoints() == null ) lawyer.setPoints(1);
        		lawyer.setPoints(lawyer.getPoints()+2);
        		lawyerDAO.update(lawyer);
        	}
        	existingAnswer.setStatus(answer.getStatus());
        	answerDAO.update(existingAnswer);
        }
        return existingAnswer;
    }
    
    public List<Answer> getAnswers(TanlsUser user, Integer questionId, Integer limit, Integer offset, String status) throws InvalidInputException
    {
    	Customer customer = tanlsUserDAO.getCustomer(user);
    	if( customer == null ) throw new InvalidInputException("Could not locate your customer details");
    	Question question = questionDAO.getFromCustomer(customer, questionId);
    	if( question == null ) throw new InvalidInputException("Question not found");
    	return answerDAO.getAnswersWithStatus(question, limit, offset, status);
    }
    
    public List<Answer> getAllAnswers(TanlsUser user, Integer questionId, Integer limit, Integer offset) throws InvalidInputException
    {
    	Customer customer = tanlsUserDAO.getCustomer(user);
    	if( customer == null ) throw new InvalidInputException("Could not locate your customer details");
    	Question question = questionDAO.getFromCustomer(customer, questionId);
    	if( question == null ) throw new InvalidInputException("Question not found");
    	return answerDAO.getAllAnswers(question, limit, offset);
    }
}

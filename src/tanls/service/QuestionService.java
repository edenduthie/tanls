package tanls.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.database.AnswerDAO;
import tanls.database.AreaOfPractiseDAO;
import tanls.database.LawyerDAO;
import tanls.database.QuestionDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Answer;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

@Service
public class QuestionService 
{
	public static int LAWYER_LIMIT = 100;
	public static String REPLY_KEY = "---Please reply above this line---";
	
	public static final Logger log = Logger.getLogger(QuestionService.class);
	
	@Autowired QuestionDAO questionDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired CustomerService customerService;
	@Autowired LawyerDAO lawyerDAO;
	@Autowired MailService mailService;
	@Autowired AreaOfPractiseDAO areaOfPractiseDAO;
	@Autowired AnswerDAO answerDAO;	

    public Question askQuestion(Question question, TanlsUser tanlsUser) throws InvalidInputException
    {	
    	Customer existingCustomer = tanlsUserDAO.getCustomer(tanlsUser);
    	if( existingCustomer != null ) 
    	{
    		existingCustomer.setNumberOfQuestionsAsked(existingCustomer.getNumberOfQuestionsAsked()+1);
    		customerService.update(existingCustomer, question.getCustomer());
    		question.setCustomer(existingCustomer);
    	}
    	else 
    	{
    		question.getCustomer().setUser(tanlsUser);
    		customerService.put(question.getCustomer());
    	}
    	if( question.getAreaOfPractise() != null && question.getAreaOfPractise().getId() != null )
    	{
    		question.setAreaOfPractise(areaOfPractiseDAO.get(question.getAreaOfPractise().getId()));
    	}
    	question.setTime(Calendar.getInstance().getTimeInMillis());
    	if( question.getProBono() == null ) question.setProBono(false);
    	if( question.getQuoteRequired() == null ) question.setQuoteRequired(false);
    	questionDAO.put(question);
    	
    	SendQuestionToLawyers thread = new SendQuestionToLawyers(question);
    	thread.start();

    	return question;
    }
    
    public class SendQuestionToLawyers extends Thread
    {
    	public Question question;
    	
    	public SendQuestionToLawyers(Question question) 
    	{
    		this.question = question;
    	}
    	
    	@Override
    	public void run() 
    	{
    	     sendQuestionToLawyers(question);	
    	}
    }
    
    public void sendQuestionToLawyers(Question question)
    {
    	int offset = 0;
    	List<Lawyer> lawyers = lawyerDAO.searchWithUser(offset, LAWYER_LIMIT);
    	offset = lawyers.size();
    	while( lawyers.size() > 0 )
    	{
    		for( Lawyer lawyer : lawyers )
    		{
    			try {
					
    				sendQuestionToLawyer(lawyer,question);
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
    		lawyers = lawyerDAO.search(offset, LAWYER_LIMIT);
    		offset += lawyers.size();
    	}
    }
    
    public void sendQuestionToLawyer(Lawyer lawyer, Question question) throws AddressException, MessagingException
    {
    	if( lawyer.getQuestionNotifications() != null && lawyer.getQuestionNotifications() )
    	{
    		String to = lawyer.getUser().getEmail();
    		
    		if( lawyer.getUuid() == null )
    		{
    			lawyer.setUuid(lawyer.getId().toString());
    			lawyerDAO.update(lawyer);
    		}
    		
    		String subject = "Easy Law Question #" + question.getId() + " #" + lawyer.getUuid();
    		StringBuffer messageBuffer = new StringBuffer();

    		if( question.getQuoteRequired() == null || !question.getQuoteRequired() )
    		{
    			 constructEmailNoQuote(messageBuffer,question,lawyer);
    		}
    		else
    		{
    			constructEmailWithQuote(messageBuffer,question,lawyer);
    		}
    		
    		mailService.sendMessageHTMLQuestionAddress(to, subject, messageBuffer.toString());
    	}
    }
    
    public void constructEmailNoQuote(StringBuffer messageBuffer, Question question, Lawyer lawyer)
    {
		messageBuffer.append(REPLY_KEY);
		messageBuffer.append("<p>Hi ");
		messageBuffer.append(lawyer.getName());
		messageBuffer.append(",</p>");
		messageBuffer.append("<p>An Easy Law customer has asked a question! If you know the answer reply to this email and gain 1 Easy Law point!");
		messageBuffer.append(" If the customer finds the message useful you will receive 3 points. Go on, give it a go!</p>");
		messageBuffer.append("<p><b>Customer Name: </b>");
		messageBuffer.append(question.getCustomer().getName());
		messageBuffer.append("</p>");
		if( question.getCustomer().getCompanyName() != null && question.getCustomer().getCompanyName().trim().length() > 0 )
		{
			messageBuffer.append("<p><b>Business Name: </b>");
			messageBuffer.append(question.getCustomer().getCompanyName());
			messageBuffer.append("</p>");
		}
		messageBuffer.append("<p><b>Question: </b>");
		messageBuffer.append(question.getText());
		messageBuffer.append("</p>");
		messageBuffer.append("<p>You can also reply to this question via the <a href='");
		messageBuffer.append(mailService.getBaseUrl());
		messageBuffer.append("/#!/lawyer/questions/");
		messageBuffer.append(question.getId());
		messageBuffer.append("'>web interface</a> if you prefer.</p>");
		messageBuffer.append("<p>You can also add a quote for legal services to your response but you must use the web interface for this.</p>");
		messageBuffer.append("<p>Have a great day!</p>");
    }
    
    public void constructEmailWithQuote(StringBuffer messageBuffer, Question question, Lawyer lawyer)
    {
		messageBuffer.append("<p>Hi ");
		messageBuffer.append(lawyer.getName());
		messageBuffer.append(",</p>");
		messageBuffer.append("<p>An Easy Law customer has requested a quote for legal services.</p>");
		if( question.getProBono() != null && question.getProBono() ) messageBuffer.append("<p>They are a not-for-profit or community organisation seeking pro-bono legal work.</p>"); 
		messageBuffer.append("<p>If you would like to place a quote please use the <a href='");
		messageBuffer.append(mailService.getBaseUrl());
		messageBuffer.append("/#!/lawyer/questions/");
		messageBuffer.append(question.getId());
		messageBuffer.append("'>web interface</a></p>");
		messageBuffer.append("<p>You will recieve 1 point for providing a quote, an an extra 2 points if the customer finds your response useful. You may also be booked by the customer!</p>");
		messageBuffer.append("<p><b>Customer Name: </b>");
		messageBuffer.append(question.getCustomer().getName());
		messageBuffer.append("</p>");
		if( question.getCustomer().getCompanyName() != null && question.getCustomer().getCompanyName().trim().length() > 0 )
		{
			messageBuffer.append("<p><b>Business Name: </b>");
			messageBuffer.append(question.getCustomer().getCompanyName());
			messageBuffer.append("</p>");
		}
		messageBuffer.append("<p><b>Question: </b>");
		messageBuffer.append(question.getText());
		messageBuffer.append("</p>");
		messageBuffer.append("<p>Have a great day!</p>");
    }
    
    public List<Question> getCustomerQuestions(TanlsUser user, Integer limit, Integer offset)
    {
    	Customer customer = tanlsUserDAO.getCustomer(user);
    	if( customer == null ) return new ArrayList<Question>();
    	List<Question> questions =  questionDAO.getCustomerQuestions(customer, limit, offset);
    	for( Question question : questions )
    	{
    		question.setAnswers(answerDAO.countAnswersWithStatus(null, question));
    		question.setUseful(answerDAO.countAnswersWithStatus(Answer.STATUS_USEFUL, question));
    		question.setNotUseful(answerDAO.countAnswersWithStatus(Answer.STATUS_NOT_USEFUL, question));
    	}
    	return questions;
    }
    
    public Question get(TanlsUser user, Integer questionId)
    {
    	Customer customer = tanlsUserDAO.getCustomer(user);
    	if( customer == null ) return null;
    	return questionDAO.getFromCustomer(customer, questionId);
    }
    
    public Question getAsLawyer(TanlsUser user, Integer questionId)
    {
    	if( !user.getLawyer() ) return null;
    	Question question =  questionDAO.get(questionId);
    	Lawyer lawyer = tanlsUserDAO.getLawyer(user);
    	if( question != null && lawyer != null )
    	{
    		question.setAnswers(answerDAO.countAnswersWithStatus(null, question));
    		question.setUseful(answerDAO.countAnswersWithStatus(Answer.STATUS_USEFUL, question));
    		question.setNotUseful(answerDAO.countAnswersWithStatus(Answer.STATUS_NOT_USEFUL, question));
    		question.setAnswer(answerDAO.getLawyerAnswer(lawyer, question));
    	}
    	return question;
    }
    
    public List<Question> getLawyerQuestions(TanlsUser user, Integer limit, Integer offset)
    {
    	List<Question> questions = new ArrayList<Question>();
    	
    	if( user == null || !user.getLawyer() ) return questions;
    	Lawyer lawyer = tanlsUserDAO.getLawyer(user);
    	if( lawyer == null ) return questions;
    	
    	questions =  questionDAO.getLawyerQuestions(limit, offset);
    	
    	for( Question question : questions )
    	{
    		question.setAnswers(answerDAO.countAnswersWithStatus(null, question));
    		question.setUseful(answerDAO.countAnswersWithStatus(Answer.STATUS_USEFUL, question));
    		question.setNotUseful(answerDAO.countAnswersWithStatus(Answer.STATUS_NOT_USEFUL, question));
    		question.setAnswer(answerDAO.getLawyerAnswer(lawyer, question));
    	}
    	return questions;
    }
}

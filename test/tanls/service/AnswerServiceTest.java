package tanls.service;

import java.math.BigDecimal;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AnswerDAO;
import tanls.database.CustomerDAO;
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

public class AnswerServiceTest extends BaseTest
{
	@Autowired AnswerService answerService;
	@Autowired AnswerDAO answerDAO;
	@Autowired QuestionDAO questionDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired LawyerDAO lawyerDAO;
	@Autowired CustomerDAO customerDAO;
	
	@Test
    public void createAnswer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
		Question question = Generator.question();
		questionDAO.put(question);

		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		
		answerService.createAnswer(user, answer);
		
		Answer result = answerDAO.getWithQuestion(answer.getId());
		Assert.assertEquals(result.getQuestion().getId(),question.getId());
		Assert.assertEquals(result.getQuestion().getText(),question.getText());
		Assert.assertEquals(result.getStatus(),Answer.STATUS_NEW);
		Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
		Assert.assertEquals(result.getLawyer().getPoints().intValue(),1);
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void createAnswerNotALawyer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(false);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
		Question question = Generator.question();
		questionDAO.put(question);

		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		
		try
		{
		    answerService.createAnswer(user, answer);
		    Assert.fail();
		}
		catch(InvalidInputException e) {}
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void createAnswerExistingPoints() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyer.setPoints(10);
		lawyerDAO.put(lawyer);
		
		Question question = Generator.question();
		questionDAO.put(question);

		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		
		answerService.createAnswer(user, answer);
		
		Answer result = answerDAO.getWithQuestion(answer.getId());
		Assert.assertEquals(result.getQuestion().getId(),question.getId());
		Assert.assertEquals(result.getQuestion().getText(),question.getText());
		Assert.assertEquals(result.getStatus(),Answer.STATUS_NEW);
		Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
		Assert.assertEquals(result.getLawyer().getPoints().intValue(),11);
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void createAnswerExistingAnswer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
		Question question = Generator.question();
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		
		answerService.createAnswer(user, answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setText("new text");
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		try
		{
		    answerService.createAnswer(user, newAnswer);
		    Assert.fail();
		}
		catch(InvalidInputException e) {}
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void createAnswerWithCustomer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);

		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		
		answerService.createAnswer(user, answer);
		
		Answer result = answerDAO.getWithQuestion(answer.getId());
		Assert.assertEquals(result.getQuestion().getId(),question.getId());
		Assert.assertEquals(result.getQuestion().getText(),question.getText());
		Assert.assertEquals(result.getStatus(),Answer.STATUS_NEW);
		Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
		Assert.assertEquals(result.getLawyer().getPoints().intValue(),1);
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void updateAnswerText() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
		Question question = Generator.question();
		questionDAO.put(question);

		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		
		answerService.createAnswer(user, answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setText("new text");
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		answerService.updateAnswerText(user, newAnswer);
	    Answer result = answerDAO.getWithQuestion(answer.getId());
	    Assert.assertEquals(result.getText(),"new text");
	    Assert.assertEquals(result.getQuestion().getText(),question.getText());
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void updateAnswerTextNotLawyer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(false);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
		Question question = Generator.question();
		questionDAO.put(question);

		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setText("new text");
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		try
		{
		    answerService.updateAnswerText(user, newAnswer);
		    Assert.fail();
		}
		catch(InvalidInputException e) {}
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void updateAnswerTextNoLawyerFound() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		
		Question question = Generator.question();
		questionDAO.put(question);

		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
        answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setText("new text");
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		try
		{
		    answerService.updateAnswerText(user, newAnswer);
		    Assert.fail();
		}
		catch(InvalidInputException e) {}
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void updateAnswerTextNotTheLawyer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
		Question question = Generator.question();
		questionDAO.put(question);
		
		Lawyer differentLawyer = Generator.lawyer();
		lawyerDAO.put(differentLawyer);

		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		answer.setLawyer(differentLawyer);
		answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setText("new text");
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		try
		{
		    answerService.updateAnswerText(user, newAnswer);
		    Assert.fail();
		}
		catch(InvalidInputException e) {}
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void updateAnswerStatus() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		answer.setQuestion(question);
		answer.setStatus(Answer.STATUS_NEW);
		answer.setLawyer(lawyer);
		answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setStatus(Answer.STATUS_USEFUL);
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		answerService.updateAnswerStatus(user, newAnswer);
	    Answer result = answerDAO.getWithQuestion(answer.getId());
	    Assert.assertEquals(result.getStatus(),newAnswer.getStatus());
	    Assert.assertEquals(result.getQuestion().getText(),question.getText());
	    Assert.assertEquals(result.getLawyer().getPoints().intValue(),2);
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void updateAnswerStatusNoCustomerFound() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		
		Question question = Generator.question();
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		answer.setQuestion(question);
		answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setStatus(Answer.STATUS_USEFUL);
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		try
		{
		    answerService.updateAnswerStatus(user, newAnswer);
		}
		catch( InvalidInputException  e) {}
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void updateAnswerStatusNotTheCustomer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
		Customer otherCustomer = Generator.customer();
		customerDAO.put(otherCustomer);
		
		Question question = Generator.question();
		question.setCustomer(otherCustomer);
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		answer.setQuestion(question);
		answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setStatus(Answer.STATUS_USEFUL);
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		try
		{
		    answerService.updateAnswerStatus(user, newAnswer);
	        Assert.fail();
		}
		catch(InvalidInputException e) {}
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void getAnswersWithStatus() throws InvalidInputException
    {
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerService.getAnswers(user, question.getId(), 2, 1, Answer.STATUS_NEW);
    	Assert.assertEquals(newAnswers.size(),2);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(), 3);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(), 2);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
	
	@Test
    public void getAnswersWithStatusCustomerNotFound() throws InvalidInputException
    {
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		//customer.setUser(user);
		customerDAO.put(customer);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	try
    	{
    	    List<Answer> newAnswers = answerService.getAnswers(user, question.getId(), 2, 1, Answer.STATUS_NEW);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
	
	@Test
    public void getAnswersWithStatusQuestionNotFound() throws InvalidInputException
    {
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	try
    	{
    	    List<Answer> newAnswers = answerService.getAnswers(user, 666, 2, 1, Answer.STATUS_NEW);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
	
	@Test
    public void sendAnswerToCustomer() throws InvalidInputException, AddressException, MessagingException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		answer.setQuestion(question);
		answer.setStatus(Answer.STATUS_NEW);
		answer.setLawyer(lawyer);
		answerDAO.put(answer);
		
		Quote quote = Generator.quote();
		quote.setLegalFees(new BigDecimal("234234.343434"));
		answer.setQuote(quote);
		//question.setQuoteRequired(true);
		
		answerService.sendAnswerToCustomer(answer);
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
    public void getAllAnswers() throws InvalidInputException
    {
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerService.getAllAnswers(user, question.getId(), 2, 1);
    	Assert.assertEquals(newAnswers.size(),2);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(), 3);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(), 2);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

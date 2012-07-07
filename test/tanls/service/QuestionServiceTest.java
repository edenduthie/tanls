package tanls.service;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AnswerDAO;
import tanls.database.AreaOfPractiseDAO;
import tanls.database.CustomerDAO;
import tanls.database.LawyerDAO;
import tanls.database.QuestionDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Answer;
import tanls.entity.AreaOfPractise;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

public class QuestionServiceTest extends BaseTest
{
	@Autowired QuestionService questionService;
	@Autowired QuestionDAO questionDAO;
	@Autowired CustomerDAO customerDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired LawyerDAO lawyerDAO;
	@Autowired AreaOfPractiseDAO areaOfPractiseDAO;
	@Autowired AnswerDAO answerDAO;
	
	@Test
	public void testAskQuestionNoExistingCustomer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		
		Question question = Generator.question();
		Customer customer = Generator.customer();
		question.setCustomer(customer);
		
		Question result = questionService.askQuestion(question, user);
		Assert.assertNotNull(result.getCustomer());
		Assert.assertEquals(result.getCustomer().getId(), customer.getId());
		Assert.assertNotNull(result.getCustomer().getUser());
		Assert.assertEquals(result.getCustomer().getUser().getId(),user.getId());
		
		Customer customerResult = customerDAO.get(customer.getId());
		Assert.assertEquals(customerResult.getName(),customer.getName());
		Assert.assertEquals(customerResult.getNumberOfQuestionsAsked().intValue(),1);
		Assert.assertTrue(customerResult.getEmailNotifications());
		
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
	public void testAskQuestionNoExistingCustomerNullBools() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		
		Question question = Generator.question();
		Customer customer = Generator.customer();
		question.setCustomer(customer);
		question.setProBono(null);
		question.setProBono(null);
		
		Question result = questionService.askQuestion(question, user);
        Assert.assertFalse(result.getProBono());
        Assert.assertFalse(result.getQuoteRequired());
		
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
	public void testAskQuestionExistingCustomer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		
		Question question = Generator.question();
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		customer.setName("newname");
		question.setCustomer(customer);
		
		Question result = questionService.askQuestion(question, user);
		Assert.assertNotNull(result.getCustomer());
		Assert.assertEquals(result.getCustomer().getId(), customer.getId());
		Assert.assertNotNull(result.getCustomer().getUser());
		Assert.assertEquals(result.getCustomer().getUser().getId(),user.getId());
		
		Customer customerResult = customerDAO.get(customer.getId());
		Assert.assertEquals(customerResult.getName(),customer.getName());
		Assert.assertEquals(customerResult.getNumberOfQuestionsAsked().intValue(),35);
		
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}

    @Test
    public void testSendQuestionToLawyer() throws AddressException, MessagingException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setEmail("eden@easylaw.com.au");
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	
    	Question question = Generator.question();
    	question.setId(23434);
    	Customer customer = Generator.customer();
    	question.setCustomer(customer);
    	
    	questionService.sendQuestionToLawyer(lawyer, question);
    }
    
    @Test
    public void testSendQuestionToLawyerQuoteRequired() throws AddressException, MessagingException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setEmail("eden@easylaw.com.au");
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	
    	Question question = Generator.question();
    	question.setId(23434);
    	Customer customer = Generator.customer();
    	question.setCustomer(customer);
    	question.setQuoteRequired(true);
    	
    	questionService.sendQuestionToLawyer(lawyer, question);
    }
    
    @Test
    public void testSendQuestionToLawyers() throws AddressException, MessagingException
    {
    	int oldLawyerLimit = questionService.LAWYER_LIMIT;
    	questionService.LAWYER_LIMIT = 2;
    	
    	TanlsUser user = Generator.tanlsUser();
    	user.setEmail("eden@easylaw.com.au");
    	tanlsUserDAO.put(user);
    	
    	for( int i=0; i < 5; ++i )
    	{
    	    Lawyer lawyer = Generator.lawyer();
    	    lawyer.setUser(user);
    	    lawyerDAO.put(lawyer);
    	}
    	
    	Question question = Generator.question();
    	question.setId(23434);
    	Customer customer = Generator.customer();
    	question.setCustomer(customer);
    	
    	questionService.sendQuestionToLawyers(question);
    	
    	questionService.LAWYER_LIMIT = oldLawyerLimit;
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
	@Test
	public void testAskQuestionExistingCustomerAndLawyers() throws InvalidInputException
	{
    	TanlsUser user = Generator.tanlsUser();
    	user.setEmail("eden@easylaw.com.au");
    	tanlsUserDAO.put(user);
    	
    	for( int i=0; i < 2; ++i )
    	{
    	    Lawyer lawyer = Generator.lawyer();
    	    lawyer.setUser(user);
    	    lawyerDAO.put(lawyer);
    	}
		
		Question question = Generator.question();
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		customer.setName("newname");
		question.setCustomer(customer);
		
		Question result = questionService.askQuestion(question, user);
		Assert.assertNotNull(result.getCustomer());
		Assert.assertEquals(result.getCustomer().getId(), customer.getId());
		Assert.assertNotNull(result.getCustomer().getUser());
		Assert.assertEquals(result.getCustomer().getUser().getId(),user.getId());
		
		Customer customerResult = customerDAO.get(customer.getId());
		Assert.assertEquals(customerResult.getName(),customer.getName());
		Assert.assertEquals(customerResult.getNumberOfQuestionsAsked().intValue(),35);
		
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
	@Test
	public void testAskQuestionNoExistingCustomerAreaOfPractise() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		
		Question question = Generator.question();
		Customer customer = Generator.customer();
		question.setCustomer(customer);
		AreaOfPractise areaOfPractise = Generator.areaOfPractise();
		areaOfPractiseDAO.put(areaOfPractise);
		question.setAreaOfPractise(areaOfPractise);
		
		Question result = questionService.askQuestion(question, user);
		Assert.assertNotNull(result.getCustomer());
		Assert.assertEquals(result.getCustomer().getId(), customer.getId());
		Assert.assertNotNull(result.getCustomer().getUser());
		Assert.assertEquals(result.getCustomer().getUser().getId(),user.getId());
		Assert.assertEquals(result.getAreaOfPractise().getId(),areaOfPractise.getId());
		
		Customer customerResult = customerDAO.get(customer.getId());
		Assert.assertEquals(customerResult.getName(),customer.getName());
		Assert.assertEquals(customerResult.getNumberOfQuestionsAsked().intValue(),1);
		
		questionDAO.deleteAll();
		areaOfPractiseDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	}
	
    @Test
    public void getCustomerQuestions()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		Question question = Generator.question();
    		question.setTime(new Long(i));
    		question.setCustomer(customer);
    		question.setAreaOfPractise(area);
    		questionDAO.put(question);
    	}
    	
    	List<Question> results = questionService.getCustomerQuestions(user, 3, 5);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTime().intValue(),4);
    	Assert.assertEquals(results.get(1).getTime().intValue(),3);
    	Assert.assertEquals(results.get(2).getTime().intValue(),2);
    	
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
    
    @Test
    public void getCustomerQuestionsNoCustomer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	List<Question> results = questionService.getCustomerQuestions(user, 3, 5);
    	Assert.assertEquals(results.size(),0);
    	
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getCustomerQuestionsWithAnswers()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		Question question = Generator.question();
    		question.setTime(new Long(i));
    		question.setCustomer(customer);
    		question.setAreaOfPractise(area);
    		questionDAO.put(question);
    		
        	for( int j=0; j < 5; ++j )
        	{
        		Answer answer = Generator.answer();
        		answer.setStatus(Answer.STATUS_USEFUL);
        		answer.setQuestion(question);
        		answerDAO.put(answer);
        	}
        	for( int j=0; j < 5; ++j )
        	{
        		Answer answer = Generator.answer();
        		answer.setStatus(Answer.STATUS_NOT_USEFUL);
        		answer.setQuestion(question);
        		answerDAO.put(answer);
        	}
    	}
    	
    	List<Question> results = questionService.getCustomerQuestions(user, 3, 5);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTime().intValue(),4);
    	Assert.assertEquals(results.get(1).getTime().intValue(),3);
    	Assert.assertEquals(results.get(2).getTime().intValue(),2);
    	for( Question question : results )
    	{
    		Assert.assertEquals(question.getAnswers().intValue(),10);
    		Assert.assertEquals(question.getUseful().intValue(),5);
    		Assert.assertEquals(question.getNotUseful().intValue(),5);
    	}
    	
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
    
    @Test
    public void getFromCustomer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	
		Question question = Generator.question();
		question.setCustomer(customer);
		question.setAreaOfPractise(area);
		questionDAO.put(question);
	
    	Question result = questionService.get(user, question.getId());
    	Assert.assertNotNull(result);
    	
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
    
    @Test
    public void getLawyerQuestions()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		Question question = Generator.question();
    		question.setTime(new Long(i));
    		question.setAreaOfPractise(area);
    		questionDAO.put(question);
    	}
    	
    	List<Question> results = questionService.getLawyerQuestions(user, 3, 5);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTime().intValue(),4);
    	Assert.assertEquals(results.get(1).getTime().intValue(),3);
    	Assert.assertEquals(results.get(2).getTime().intValue(),2);
    	
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getLawyerQuestionsNotALawyer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(false);
    	tanlsUserDAO.put(user);
    	
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		Question question = Generator.question();
    		question.setTime(new Long(i));
    		question.setAreaOfPractise(area);
    		questionDAO.put(question);
    	}
    	
    	List<Question> results = questionService.getLawyerQuestions(user, 3, 5);
    	Assert.assertEquals(results.size(),0);
    	
    	questionDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getLawyerQuestionsWithAnswers()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		Question question = Generator.question();
    		question.setTime(new Long(i));
    		question.setAreaOfPractise(area);
    		questionDAO.put(question);
    		
        	for( int j=0; j < 5; ++j )
        	{
        		Answer answer = Generator.answer();
        		answer.setStatus(Answer.STATUS_USEFUL);
        		answer.setQuestion(question);
        		answer.setLawyer(lawyer);
        		answerDAO.put(answer);
        	}
        	for( int j=0; j < 5; ++j )
        	{
        		Answer answer = Generator.answer();
        		answer.setStatus(Answer.STATUS_NOT_USEFUL);
        		answer.setQuestion(question);
        		answer.setLawyer(lawyer);
        		answerDAO.put(answer);
        	}
    	}
    	
    	List<Question> results = questionService.getLawyerQuestions(user, 3, 5);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTime().intValue(),4);
    	Assert.assertEquals(results.get(1).getTime().intValue(),3);
    	Assert.assertEquals(results.get(2).getTime().intValue(),2);
    	
    	for( Question question : results )
    	{
    		Assert.assertEquals(question.getAnswers().intValue(),10);
    		Assert.assertEquals(question.getUseful().intValue(),5);
    		Assert.assertEquals(question.getNotUseful().intValue(),5);
    		Assert.assertNotNull(question.getAnswer());
    	}
    	
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
    
    @Test
    public void getFromLawyer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
		Question question = Generator.question();
		questionDAO.put(question);
	
    	Question result = questionService.getAsLawyer(user, question.getId());
    	Assert.assertNotNull(result);
    	
    	questionDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getFromLawyerWithLawyerAndAnswer()
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
    	answer.setQuestion(question);
    	answer.setLawyer(lawyer);
        answer.setStatus(Answer.STATUS_USEFUL);
    	answerDAO.put(answer);
	
    	Question result = questionService.getAsLawyer(user, question.getId());
    	Assert.assertNotNull(result);
    	Assert.assertEquals(result.getAnswers().intValue(),1);
    	Assert.assertEquals(result.getUseful().intValue(),1);
    	Assert.assertEquals(result.getNotUseful().intValue(),0);
    	Assert.assertNotNull(result.getAnswer());
    	Assert.assertEquals(result.getAnswer().getId(),answer.getId());
    	
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getFromLawyerNotLawyer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(false);
    	tanlsUserDAO.put(user);
    	
		Question question = Generator.question();
		questionDAO.put(question);
	
    	Question result = questionService.getAsLawyer(user, question.getId());
    	Assert.assertNull(result);
    	
    	questionDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

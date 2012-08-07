package tanls.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

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
import tanls.dto.QuestionDTO;
import tanls.entity.AreaOfPractise;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.LoginStatus;

public class QuestionControllerTest extends BaseTest
{
	@Autowired QuestionDAO questionDAO;
	@Autowired CustomerDAO customerDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired LawyerDAO lawyerDAO;
	@Autowired AreaOfPractiseDAO areaOfPractiseDAO;
	@Autowired AnswerDAO answerDAO;
	
	@Autowired QuestionController controller;
	
	@Test
	public void testAskQuestionNoExistingCustomer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		Customer customer = Generator.customer();
		question.setCustomer(customer);
		
		QuestionDTO result = controller.askQuestion(new QuestionDTO(question));
		Assert.assertTrue(result.isSuccess());
		Assert.assertNotNull(result.getCustomer());
		Assert.assertNotNull(result.getCustomer().getId());
		
		Customer customerResult = customerDAO.get(result.getCustomer().getId());
		Assert.assertEquals(customerResult.getName(),customer.getName());
		Assert.assertEquals(customerResult.getNumberOfQuestionsAsked().intValue(),1);
		
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	    controller.setLoginStatus(oldLoginStatus);
	}
	
	@Test
	public void testAskQuestionExistingCustomer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		customer.setName("newname");
		question.setCustomer(customer);
		
		QuestionDTO result = controller.askQuestion(new QuestionDTO(question));
		Assert.assertNotNull(result.getCustomer());
		Assert.assertTrue(result.isSuccess());
		
		Customer customerResult = customerDAO.get(customer.getId());
		Assert.assertEquals(customerResult.getName(),customer.getName());
		Assert.assertEquals(customerResult.getNumberOfQuestionsAsked().intValue(),35);
		
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
		
		controller.setLoginStatus(oldLoginStatus);
	}
	
	@Test
	public void testAskQuestionNoUser() throws InvalidInputException
	{
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(null);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);

		QuestionDTO result = controller.askQuestion(null);
        Assert.assertFalse(result.isSuccess());
        Assert.assertTrue(result.isRedirectToLogin());
		
	    controller.setLoginStatus(oldLoginStatus);
	}
	
	@Test
	public void testAskQuestionCustomerNameIsNull() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		Customer customer = Generator.customer();
		customer.setName(null);
		question.setCustomer(customer);
		
		QuestionDTO result = controller.askQuestion(new QuestionDTO(question));
        Assert.assertFalse(result.isSuccess());
        Assert.assertFalse(result.isRedirectToLogin());
		
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
	    controller.setLoginStatus(oldLoginStatus);
	}
	
    @Test
    public void getCustomerQuestions()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
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
    	
    	List<QuestionDTO> results = controller.getCustomerQuestions(3, 5);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTime().intValue(),4);
    	Assert.assertEquals(results.get(1).getTime().intValue(),3);
    	Assert.assertEquals(results.get(2).getTime().intValue(),2);
    	
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void getCustomerQuestionsNoUser()
    {

       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(null);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	List<QuestionDTO> results = controller.getCustomerQuestions(3, 5);
    	Assert.assertEquals(results.size(),0);
    	
    	controller.setLoginStatus(oldLoginStatus);
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
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
	
    	QuestionDTO result = controller.getFromCustomer(question.getId());
    	Assert.assertNotNull(result);
    	
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
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
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	List<QuestionDTO> results = controller.getLawyerQuestions(3, 5);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTime().intValue(),4);
    	Assert.assertEquals(results.get(1).getTime().intValue(),3);
    	Assert.assertEquals(results.get(2).getTime().intValue(),2);
    	
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void getAsLawyer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
		Question question = Generator.question();
		questionDAO.put(question);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
	
    	QuestionDTO result = controller.getAsLawyer(question.getId());
    	Assert.assertNotNull(result);
    	
    	questionDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
}

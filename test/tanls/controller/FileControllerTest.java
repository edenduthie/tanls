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
import tanls.database.CustomerDAO;
import tanls.database.FileDAO;
import tanls.database.LawyerDAO;
import tanls.database.ProfileItemDAO;
import tanls.database.QuestionDAO;
import tanls.database.QuoteDAO;
import tanls.database.TanlsUserDAO;
import tanls.dto.FileDTO;
import tanls.dto.ProfileItemDTO;
import tanls.entity.Customer;
import tanls.entity.File;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;
import tanls.entity.Question;
import tanls.entity.Quote;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.LoginStatus;

public class FileControllerTest extends BaseTest
{
    @Autowired FileDAO fileDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired CustomerDAO customerDAO;
    @Autowired QuestionDAO questionDAO;
    @Autowired QuoteDAO quoteDAO;
    @Autowired LawyerDAO lawyerDAO;
    @Autowired ProfileItemDAO profileItemDAO;
    
    @Autowired FileController controller;
    
    @Test
    public void getCustomer()
    {
    	TanlsUser user = Generator.tanlsUser();
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
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quote.setLawyer(lawyer);
    	quoteDAO.put(quote);
    	
    	File file = null;
    	
    	for( int i=0; i < 10; ++i )
    	{
    		file = Generator.file();
    		file.setQuote(quote);
    		file.setTime(new Long(i));
    		file.setQuote(quote);
    		file.setCustomer(customer);
    		file.setLawyer(lawyer);
    		fileDAO.put(file);
    	}
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	List<FileDTO> results = controller.getCustomer(3, 5);
    	Assert.assertEquals(results.size(),3);
    	for( int i=0; i < results.size(); ++i )
    	{
    		FileDTO result = results.get(i);
    		Assert.assertEquals(result.getTime().intValue(),4-i);
    		Assert.assertNotNull(result.getQuote());
    		Assert.assertNotNull(result.getQuote().getQuestion());
    		Assert.assertNotNull(result.getCustomer());
    		Assert.assertNotNull(result.getCustomer().getUser());
    		Assert.assertNotNull(result.getLawyer());
    		Assert.assertNotNull(result.getLawyer().getUser());
    	}
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void getCustomerSingle()
    {
    	TanlsUser user = Generator.tanlsUser();
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
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quote.setLawyer(lawyer);
    	quoteDAO.put(quote);
    	
    	File file = Generator.file();
		file.setQuote(quote);
		file.setTime(1l);
		file.setQuote(quote);
		file.setCustomer(customer);
		file.setLawyer(lawyer);
		fileDAO.put(file);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	FileDTO result = controller.getCustomerSingle(file.getId());
		Assert.assertEquals(result.getTime().intValue(),1);
		Assert.assertNotNull(result.getQuote());
		Assert.assertNotNull(result.getQuote().getQuestion());
		Assert.assertNotNull(result.getCustomer());
		Assert.assertNotNull(result.getCustomer().getUser());
		Assert.assertNotNull(result.getLawyer());
		Assert.assertNotNull(result.getLawyer().getUser());
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void getLawyer()
    {
    	TanlsUser user = Generator.tanlsUser();
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
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quote.setLawyer(lawyer);
    	quoteDAO.put(quote);
    	
    	File file = null;
    	
    	for( int i=0; i < 10; ++i )
    	{
    		file = Generator.file();
    		file.setQuote(quote);
    		file.setTime(new Long(i));
    		file.setQuote(quote);
    		file.setCustomer(customer);
    		file.setLawyer(lawyer);
    		fileDAO.put(file);
    	}
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	List<FileDTO> results = controller.getLawyer(3, 5);
    	Assert.assertEquals(results.size(),3);
    	for( int i=0; i < results.size(); ++i )
    	{
    		FileDTO result = results.get(i);
    		Assert.assertEquals(result.getTime().intValue(),4-i);
    		Assert.assertNotNull(result.getQuote());
    		Assert.assertNotNull(result.getQuote().getQuestion());
    		Assert.assertNotNull(result.getCustomer());
    		Assert.assertNotNull(result.getCustomer().getUser());
    		Assert.assertNotNull(result.getLawyer());
    		Assert.assertNotNull(result.getLawyer().getUser());
    	}
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void getLawyerSingle()
    {
    	TanlsUser user = Generator.tanlsUser();
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
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quote.setLawyer(lawyer);
    	quoteDAO.put(quote);
    	
    	File file = Generator.file();
		file.setQuote(quote);
		file.setTime(1l);
		file.setQuote(quote);
		file.setCustomer(customer);
		file.setLawyer(lawyer);
		fileDAO.put(file);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	FileDTO result = controller.getCustomerSingle(file.getId());
		Assert.assertEquals(result.getTime().intValue(),1);
		Assert.assertNotNull(result.getQuote());
		Assert.assertNotNull(result.getQuote().getQuestion());
		Assert.assertNotNull(result.getCustomer());
		Assert.assertNotNull(result.getCustomer().getUser());
		Assert.assertNotNull(result.getLawyer());
		Assert.assertNotNull(result.getLawyer().getUser());
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void markAsComplete() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	File file = Generator.file();
    	file.setStatus(File.STATUS_IN_PROGRESS);
    	file.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	file.setCustomer(customer);
    	Quote quote = Generator.quote();
    	quoteDAO.put(quote);
    	file.setQuote(quote);
    	fileDAO.put(file);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	FileDTO result = controller.markComplete(file.getId());
    	File resultFile = fileDAO.getWithPaymentAndQuote(file.getId());
    	Assert.assertEquals(resultFile.getStatus(),File.STATUS_COMPLETE);
    	Assert.assertTrue(result.isSuccess());
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void markAsCompleteNoFile() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	File file = Generator.file();
    	file.setStatus(File.STATUS_IN_PROGRESS);
    	file.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	file.setCustomer(customer);
    	Quote quote = Generator.quote();
    	quoteDAO.put(quote);
    	file.setQuote(quote);
    	fileDAO.put(file);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	FileDTO result = controller.markComplete(666);
    	File resultFile = fileDAO.getWithPaymentAndQuote(file.getId());
    	Assert.assertEquals(resultFile.getStatus(),File.STATUS_IN_PROGRESS);
    	Assert.assertFalse(result.isSuccess());
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void submitFeedback() throws InvalidInputException
    {
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	File file = Generator.file();
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	Question question = Generator.question();
    	questionDAO.put(question);
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quoteDAO.put(quote);
    	file.setQuote(quote);
    	fileDAO.put(file);
    	
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setRating(5);
    	feedback.setText("Feedback Text");
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItemDTO feedbackResult = controller.submitFeedback(new ProfileItemDTO(feedback),file.getId());
    	Assert.assertEquals(feedbackResult.getCustomer().getId(),customer.getId());
    	File fileResult = fileDAO.getCustomerSingle(file.getId(), user);
    	Assert.assertNotNull(fileResult.getFeedback());
    	Assert.assertEquals(fileResult.getFeedback().getType(),ProfileItem.FEEDBACK);
    	Assert.assertEquals(fileResult.getFeedback().getText(),feedback.getText());
    	Assert.assertEquals(fileResult.getFeedback().getRating(),feedback.getRating());
    	
    	Lawyer lawyerResult = lawyerDAO.get(lawyer.getId());
    	Assert.assertEquals(lawyerResult.getPercentagePositive().intValue(),100);
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	profileItemDAO.deleteAll();
    	customerDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
}

package tanls.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AddressDAO;
import tanls.database.CustomerDAO;
import tanls.database.FileDAO;
import tanls.database.LawyerDAO;
import tanls.database.PaymentDAO;
import tanls.database.ProfileItemDAO;
import tanls.database.QuestionDAO;
import tanls.database.QuoteDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Address;
import tanls.entity.Customer;
import tanls.entity.File;
import tanls.entity.Lawyer;
import tanls.entity.Payment;
import tanls.entity.ProfileItem;
import tanls.entity.Question;
import tanls.entity.Quote;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.exception.PaymentException;

public class FileServiceTest extends BaseTest
{
    @Autowired CustomerDAO customerDAO;
    @Autowired AddressDAO addressDAO;
    @Autowired PaymentDAO paymentDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired QuoteDAO quoteDAO;
    @Autowired FileDAO fileDAO;
    @Autowired QuestionDAO questionDAO;
    @Autowired LawyerDAO lawyerDAO;
    @Autowired ProfileItemDAO profileItemDAO;
    
    @Autowired FileService fileService;
    
    @Test
    public void create() throws InvalidInputException, PaymentException
    {
    	Customer customer = Generator.customer();
    	Address address = Generator.address();
    	customer.setBillingAddress(address);
    	Payment payment = Generator.payment();
    	payment.setCustomer(customer);
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
    	Question question = Generator.question();
    	question.setCustomer(customer);
    	questionDAO.put(question);
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quote.setLegalFees(new BigDecimal(100));
    	quote.setDisbursments(new BigDecimal(1));
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	quote.setLawyer(lawyer);
    	Assert.assertEquals(quote.getAmount().toString(),"101");
    	quoteDAO.put(quote);
    	
    	File file = Generator.file();
    	file.setQuote(quote);
    	file.setPayment(payment);
    	
    	fileService.create(file,user);
    	
    	File result = fileDAO.getWithPaymentAndQuote(file.getId());
    	Assert.assertNotNull(result);
    	Assert.assertNotNull(result.getTime());
    	Assert.assertEquals(result.getStatus(),File.STATUS_IN_PROGRESS);
    	Assert.assertNotNull(result.getPayment());
    	Assert.assertNotNull(result.getQuote());
    	Assert.assertNotNull(result.getLawyer());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Assert.assertEquals(file.getPayment().getAmount().intValue(),101);
    	
    	Customer resultCustomer = customerDAO.get(customer.getId());
    	Assert.assertNotNull(resultCustomer.getBillingAddress());
    	Assert.assertEquals(resultCustomer.getBillingAddress().getStreetAddress(),customer.getBillingAddress().getStreetAddress());
    	
    	// can't do it again for the same quote
    	try
    	{
    		fileService.create(file,user);
    		Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    	
    	
    	fileDAO.deleteAll();
    	paymentDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	addressDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateCustomerNoChange() throws InvalidInputException, PaymentException
    {
    	Customer customer = Generator.customer();
    	Address address = Generator.address();
    	customer.setBillingAddress(address);
    	Payment payment = Generator.payment();
    	payment.setCustomer(customer);
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
    	Question question = Generator.question();
    	question.setCustomer(customer);
    	questionDAO.put(question);
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quote.setLegalFees(new BigDecimal(100));
    	quote.setDisbursments(new BigDecimal(1));
    	quoteDAO.put(quote);
    	
    	File file = Generator.file();
    	file.setQuote(quote);
    	file.setPayment(payment);
    	
    	fileService.updateBillingAddress(file,customer);
    	
    	Customer resultCustomer = customerDAO.get(customer.getId());
    	Assert.assertNotNull(resultCustomer.getBillingAddress());
    	Assert.assertEquals(resultCustomer.getBillingAddress().getStreetAddress(),customer.getBillingAddress().getStreetAddress());
    	
    	fileDAO.deleteAll();
    	paymentDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	addressDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateCustomerNoExistingAddress() throws InvalidInputException, PaymentException
    {
    	Customer customer = Generator.customer();
    	Payment payment = Generator.payment();
    	payment.setCustomer(customer);
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
    	Question question = Generator.question();
    	question.setCustomer(customer);
    	questionDAO.put(question);
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quote.setLegalFees(new BigDecimal(100));
    	quote.setDisbursments(new BigDecimal(1));
    	quoteDAO.put(quote);
    	
    	File file = Generator.file();
    	file.setQuote(quote);
    	file.setPayment(payment);
    	
    	Address newAddress = Generator.address();
    	file.getQuote().getQuestion().getCustomer().setBillingAddress(newAddress);
    	Customer newCustomerObject = customerDAO.get(customer.getId());
    	
    	fileService.updateBillingAddress(file,newCustomerObject);
    	
    	Customer resultCustomer = customerDAO.get(customer.getId());
    	Assert.assertNotNull(resultCustomer.getBillingAddress());
    	Assert.assertEquals(resultCustomer.getBillingAddress().getStreetAddress(),newAddress.getStreetAddress());
    	
    	List<Address> addresses = addressDAO.getAll();
    	Assert.assertEquals(addresses.size(),1);
    	
    	fileDAO.deleteAll();
    	paymentDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	addressDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateCustomerExistingAddress() throws InvalidInputException, PaymentException
    {
    	Customer customer = Generator.customer();
    	Address address = Generator.address();
    	customer.setBillingAddress(address);
    	Payment payment = Generator.payment();
    	payment.setCustomer(customer);
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
    	Question question = Generator.question();
    	question.setCustomer(customer);
    	questionDAO.put(question);
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quote.setLegalFees(new BigDecimal(100));
    	quote.setDisbursments(new BigDecimal(1));
    	quoteDAO.put(quote);
    	
    	File file = Generator.file();
    	file.setQuote(quote);
    	file.setPayment(payment);
    	
    	Address newAddress = Generator.address();
    	newAddress.setStreetAddress("changed");
    	file.getQuote().getQuestion().getCustomer().setBillingAddress(newAddress);
    	Customer newCustomerObject = customerDAO.get(customer.getId());
    	
    	fileService.updateBillingAddress(file,newCustomerObject);
    	
    	Customer resultCustomer = customerDAO.get(customer.getId());
    	Assert.assertNotNull(resultCustomer.getBillingAddress());
    	Assert.assertEquals(resultCustomer.getBillingAddress().getStreetAddress(),"changed");
    	
    	List<Address> addresses = addressDAO.getAll();
    	Assert.assertEquals(addresses.size(),1);
    	
    	fileDAO.deleteAll();
    	paymentDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	addressDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateCustomerExistingAddressButDoNotChange() throws InvalidInputException, PaymentException
    {
    	Customer customer = Generator.customer();
    	Address address = Generator.address();
    	customer.setBillingAddress(address);
    	Payment payment = Generator.payment();
    	payment.setCustomer(customer);
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
    	Question question = Generator.question();
    	question.setCustomer(customer);
    	questionDAO.put(question);
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quote.setLegalFees(new BigDecimal(100));
    	quote.setDisbursments(new BigDecimal(1));
    	quoteDAO.put(quote);
    	
    	File file = Generator.file();
    	file.setQuote(quote);
    	file.setPayment(payment);
    	
    	Address newAddress = Generator.address();
    	newAddress.setStreetAddress("changed");
    	file.getQuote().getQuestion().getCustomer().setBillingAddress(newAddress);
    	Customer newCustomerObject = customerDAO.get(customer.getId());
    	
    	file.getPayment().setUseExistingCustomer(true);
    	
    	fileService.updateBillingAddress(file,newCustomerObject);
    	
    	Customer resultCustomer = customerDAO.get(customer.getId());
    	Assert.assertNotNull(resultCustomer.getBillingAddress());
    	Assert.assertEquals(resultCustomer.getBillingAddress().getStreetAddress(),address.getStreetAddress());
    	
    	List<Address> addresses = addressDAO.getAll();
    	Assert.assertEquals(addresses.size(),1);
    	
    	fileDAO.deleteAll();
    	paymentDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	addressDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void createProBono() throws InvalidInputException, PaymentException
    {
    	Customer customer = Generator.customer();
    	Address address = Generator.address();
    	customer.setBillingAddress(address);
    	Payment payment = Generator.payment();
    	payment.setCustomer(customer);
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
    	Question question = Generator.question();
    	question.setCustomer(customer);
    	question.setProBono(true);
    	questionDAO.put(question);
    	Quote quote = Generator.quote();
    	quote.setQuestion(question);
    	quoteDAO.put(quote);
    	
    	File file = Generator.file();
    	file.setQuote(quote);
    	file.setPayment(payment);
    	
    	fileService.create(file,user);
    	
    	File result = fileDAO.get(file.getId());
    	Assert.assertNotNull(result);
    	Assert.assertNotNull(result.getTime());
    	Assert.assertEquals(result.getStatus(),File.STATUS_IN_PROGRESS);
    	
    	Assert.assertNull(file.getPayment());
    	
    	Customer resultCustomer = customerDAO.get(customer.getId());
    	Assert.assertNotNull(resultCustomer.getBillingAddress());
    	Assert.assertEquals(resultCustomer.getBillingAddress().getStreetAddress(),customer.getBillingAddress().getStreetAddress());
    	
    	fileDAO.deleteAll();
    	paymentDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	addressDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void sendEmailToCustomer() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	Lawyer lawyer = Generator.lawyer();
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendEmailToCustomer(file);
    }
    
    @Test
    public void sendEmailToCustomerProBono() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	question.setProBono(true);
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	quote.setLegalFees(null);
    	quote.setDisbursments(null);
    	Lawyer lawyer = Generator.lawyer();
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendEmailToCustomer(file);
    }
    
    @Test
    public void sendEmailToCustomerNullDisbursments() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	quote.setDisbursments(null);
    	Lawyer lawyer = Generator.lawyer();
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendEmailToCustomer(file);
    }
    
    @Test
    public void sendEmailToLawyer() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	Lawyer lawyer = Generator.lawyer();
    	TanlsUser user2 = Generator.tanlsUser();
    	tanlsUserDAO.put(user2);
    	lawyer.setUser(user2);
    	lawyerDAO.put(lawyer);
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendEmailToLawyer(file);
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void sendEmailToLawyerProBono() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	question.setProBono(true);
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	quote.setLegalFees(null);
    	quote.setDisbursments(null);
    	Lawyer lawyer = Generator.lawyer();
    	TanlsUser user2 = Generator.tanlsUser();
    	tanlsUserDAO.put(user2);
    	lawyer.setUser(user2);
    	lawyerDAO.put(lawyer);
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendEmailToLawyer(file);
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void sendEmailToLawyerNullDisbusments() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	quote.setDisbursments(null);
    	Lawyer lawyer = Generator.lawyer();
    	TanlsUser user2 = Generator.tanlsUser();
    	tanlsUserDAO.put(user2);
    	lawyer.setUser(user2);
    	lawyerDAO.put(lawyer);
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendEmailToLawyer(file);
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
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
    	
    	File result = fileService.markAsComplete(file.getId(), user);
    	result = fileDAO.getWithPaymentAndQuote(file.getId());
    	Assert.assertEquals(result.getStatus(),File.STATUS_COMPLETE);
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void markAsCompleteNoLawyer() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	//lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	File file = Generator.file();
    	file.setStatus(File.STATUS_IN_PROGRESS);
    	file.setLawyer(lawyer);
    	fileDAO.put(file);
    	
    	try
    	{
    		fileService.markAsComplete(file.getId(), user);
    		Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	fileDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
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
    	fileDAO.put(file);
    	
    	try
    	{
    		fileService.markAsComplete(666, user);
    		Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	fileDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void markAsCompleteNotParty() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	File file = Generator.file();
    	file.setStatus(File.STATUS_IN_PROGRESS);
    	Lawyer otherLawyer = Generator.lawyer();
        lawyerDAO.put(otherLawyer);
        file.setLawyer(otherLawyer);
    	fileDAO.put(file);
    	
    	try
    	{
    		fileService.markAsComplete(file.getId(), user);
    		Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    	
    	fileDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void markAsCompleteAlreadyComplete() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	File file = Generator.file();
    	file.setStatus(File.STATUS_COMPLETE);
    	file.setLawyer(lawyer);
    	fileDAO.put(file);
    	
    	try
    	{
    		fileService.markAsComplete(file.getId(), user);
    		Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	fileDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void sendCompleteToCustomer() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	Lawyer lawyer = Generator.lawyer();
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendCompleteToCustomer(file);
    }
    
    @Test
    public void sendCompleteToLawyer() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	Lawyer lawyer = Generator.lawyer();
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendCompleteToLawyer(file);
    }
    
    @Test
    public void sendCompleteToLawyerProBono() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	question.setProBono(true);
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	quote.setLegalFees(null);
    	quote.setDisbursments(null);
    	Lawyer lawyer = Generator.lawyer();
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendCompleteToLawyer(file);
    }
    
    @Test
    public void sendCompleteToLawyerNoDisbursments() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	//question.setCustomer(customer);
    	quote.setQuestion(question);
    	quote.setDisbursments(null);
    	Lawyer lawyer = Generator.lawyer();
    	//quote.setLawyer(lawyer);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	
    	fileService.sendCompleteToLawyer(file);
    }
    
    @Test
    public void submitFeedback() throws InvalidInputException
    {
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyer.setPoints(10);
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
    	
    	ProfileItem feedbackResult = fileService.submitFeedback(file.getId(), feedback, user);
    	Assert.assertEquals(feedbackResult.getCustomer().getId(),customer.getId());
    	File fileResult = fileDAO.getCustomerSingle(file.getId(), user);
    	Assert.assertNotNull(fileResult.getFeedback());
    	Assert.assertEquals(fileResult.getFeedback().getType(),ProfileItem.FEEDBACK);
    	Assert.assertEquals(fileResult.getFeedback().getText(),feedback.getText());
    	Assert.assertEquals(fileResult.getFeedback().getRating(),feedback.getRating());
    	Assert.assertEquals(fileResult.getStatus(),File.FEEDBACK_RECEIVED);
    	
    	Lawyer lawyerResult = lawyerDAO.get(lawyer.getId());
    	Assert.assertEquals(lawyerResult.getPercentagePositive().intValue(),100);
    	Assert.assertEquals(lawyerResult.getPoints().intValue(),15);
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	profileItemDAO.deleteAll();
    	customerDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void submitFeedbackFileNotFound() throws InvalidInputException
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	File file = Generator.file();
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	fileDAO.put(file);
    	
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setRating(5);
    	feedback.setText("Feedback Text");
    	
    	try
    	{
    	    fileService.submitFeedback(666, feedback, user);
    	    Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    	
    	fileDAO.deleteAll();
    	profileItemDAO.deleteAll();
    	customerDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void submitFeedbackFileNotTheCustomer() throws InvalidInputException
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	File file = Generator.file();
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	fileDAO.put(file);
    	
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setRating(5);
    	feedback.setText("Feedback Text");
    	
    	TanlsUser otherUser = Generator.tanlsUser();
    	tanlsUserDAO.put(otherUser);
    	
    	try
    	{
    	    fileService.submitFeedback(file.getId(), feedback, otherUser);
    	    Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    	
    	fileDAO.deleteAll();
    	profileItemDAO.deleteAll();
    	customerDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void submitFeedbackFileAlreadyFeedback() throws InvalidInputException
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	File file = Generator.file();
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	fileDAO.put(file);
    	
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setRating(5);
    	feedback.setText("Feedback Text");
    	profileItemDAO.put(feedback);
    	file.setFeedback(feedback);
    	fileDAO.update(file);
    	
    	try
    	{
    	    fileService.submitFeedback(file.getId(), feedback, user);
    	    Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    	
    	fileDAO.deleteAll();
    	profileItemDAO.deleteAll();
    	customerDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void validateProfileItemNoText()
    {
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setText(null);
    	try{ fileService.validate(feedback); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateProfileItemTextZeroLength()
    {
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setText(" ");
    	try{ fileService.validate(feedback); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateProfileItemTextTooLong()
    {
    	ProfileItem feedback = Generator.profileItem();
    	StringBuffer text = new StringBuffer();
    	for( int i=0; i < 701; ++i ) text.append("c");
    	feedback.setText(text.toString());
    	try{ fileService.validate(feedback); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateProfileItemRatingTooLow()
    {
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setRating(0);
    	try{ fileService.validate(feedback); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateProfileItemRatingTooHigh()
    {
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setRating(11);
    	try{ fileService.validate(feedback); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateProfileItemRatingMissing()
    {
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setRating(null);
    	try{ fileService.validate(feedback); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void submitFeedbackNotPositive() throws InvalidInputException
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	File file = Generator.file();
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	fileDAO.put(file);
    	
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setRating(4);
    	feedback.setText("Feedback Text");
    	
    	ProfileItem feedbackResult = fileService.submitFeedback(file.getId(), feedback, user);
    	Assert.assertEquals(feedbackResult.getCustomer().getId(),customer.getId());
    	File fileResult = fileDAO.getCustomerSingle(file.getId(), user);
    	Assert.assertNotNull(fileResult.getFeedback());
    	Assert.assertEquals(fileResult.getFeedback().getType(),ProfileItem.FEEDBACK);
    	Assert.assertEquals(fileResult.getFeedback().getText(),feedback.getText());
    	Assert.assertEquals(fileResult.getFeedback().getRating(),feedback.getRating());
    	
    	Lawyer lawyerResult = lawyerDAO.get(lawyer.getId());
    	Assert.assertEquals(lawyerResult.getPercentagePositive().intValue(),0);
    	
    	fileDAO.deleteAll();
    	profileItemDAO.deleteAll();
    	customerDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void sendFeedbackToLawyer() throws AddressException, MessagingException
    {
    	File file = Generator.file();
    	Quote quote = Generator.quote();
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	TanlsUser user = Generator.tanlsUser();
    	customer.setUser(user);
    	quote.setQuestion(question);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	file.setQuote(quote);
    	file.setId(666);
    	file.setCustomer(customer);
    	file.setLawyer(lawyer);
    	ProfileItem feedback = Generator.profileItem();
    	feedback.setRating(5);
    	feedback.setText("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum claritatem. Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant sollemnes in futurum.");
    	file.setFeedback(feedback);
    	
    	fileService.sendFeedbackToLawyer(file);
    }
    
    @Test
    public void getLegalFeeGST()
    {
    	fileService.setEasyLawGST(true);
    	
    	Quote quote = Generator.quote();
    	quote.setLegalFees(new BigDecimal(100));
    	
    	BigDecimal gst = fileService.getLegalFeeGST(quote, true);
    	Assert.assertEquals(gst.intValue(),10);
    	
    	fileService.setEasyLawGST(false);
    }
    
    @Test
    public void getLegalFeeGSTNoOneRegistered()
    {
    	fileService.setEasyLawGST(false);
    	
    	Quote quote = Generator.quote();
    	quote.setLegalFees(new BigDecimal(100));
    	
    	BigDecimal gst = fileService.getLegalFeeGST(quote,false);
    	Assert.assertEquals(gst.intValue(),0);
    }
    
    @Test
    public void getLegalFeeGSTJustLawyerRegistered()
    {
    	fileService.setEasyLawGST(false);
    	
    	Quote quote = Generator.quote();
    	quote.setLegalFees(new BigDecimal(100));
    	
    	BigDecimal gst = fileService.getLegalFeeGST(quote,true);
    	gst = gst.setScale(2,RoundingMode.HALF_DOWN);
    	Assert.assertEquals(gst.toPlainString(),"8.50");
    }
    
    @Test
    public void getLegalFeeGSTJustEasyLawRegistered()
    {
    	fileService.setEasyLawGST(true);
    	
    	Quote quote = Generator.quote();
    	quote.setLegalFees(new BigDecimal(100));
    	
    	BigDecimal gst = fileService.getLegalFeeGST(quote,false);
    	gst = gst.setScale(2,RoundingMode.HALF_DOWN);
    	Assert.assertEquals(gst.toPlainString(),"1.50");
    	
    	fileService.setEasyLawGST(false);
    }
}

package tanls.database;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.entity.Customer;
import tanls.entity.File;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.entity.Quote;
import tanls.entity.TanlsUser;

public class FileDAOTest extends BaseTest
{
    @Autowired FileDAO fileDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired CustomerDAO customerDAO;
    @Autowired QuestionDAO questionDAO;
    @Autowired QuoteDAO quoteDAO;
    @Autowired LawyerDAO lawyerDAO;
    
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
    	
    	List<File> results = fileDAO.getCustomer(3, 5, user);
    	Assert.assertEquals(results.size(),3);
    	for( int i=0; i < results.size(); ++i )
    	{
    		File result = results.get(i);
    		Assert.assertEquals(result.getTime().intValue(),4-i);
    		//Assert.assertNotNull(result.getQuote().getQuestion().getCustomer().getUser());
    		//Assert.assertNotNull(result.getQuote().getLawyer().getUser());
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
    	tanlsUserDAO.deleteAll();
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
		file.setTime(Calendar.getInstance().getTimeInMillis());
		file.setCustomer(customer);
		file.setLawyer(lawyer);
		fileDAO.put(file);
    	
    	File result = fileDAO.getCustomerSingle(file.getId(),user);
		Assert.assertEquals(result.getTime().intValue(),file.getTime().intValue());
		//Assert.assertNotNull(result.getQuote().getQuestion().getCustomer().getUser());
		//Assert.assertNotNull(result.getQuote().getLawyer().getUser());
		Assert.assertNotNull(result.getCustomer());
		Assert.assertNotNull(result.getCustomer().getUser());
		Assert.assertNotNull(result.getLawyer());
		Assert.assertNotNull(result.getLawyer().getUser());
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
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
    	
    	List<File> results = fileDAO.getCustomer(3, 5, user);
    	Assert.assertEquals(results.size(),3);
    	for( int i=0; i < results.size(); ++i )
    	{
    		File result = results.get(i);
    		Assert.assertEquals(result.getTime().intValue(),4-i);
    		//Assert.assertNotNull(result.getQuote().getQuestion().getCustomer().getUser());
    		//Assert.assertNotNull(result.getQuote().getLawyer().getUser());
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
    	tanlsUserDAO.deleteAll();
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
		file.setTime(Calendar.getInstance().getTimeInMillis());
		file.setCustomer(customer);
		file.setLawyer(lawyer);
		fileDAO.put(file);
    	
    	File result = fileDAO.getLawyerSingle(file.getId(),user);
		Assert.assertEquals(result.getTime().intValue(),file.getTime().intValue());
		//Assert.assertNotNull(result.getQuote().getQuestion().getCustomer().getUser());
		//Assert.assertNotNull(result.getQuote().getLawyer().getUser());
		Assert.assertNotNull(result.getCustomer());
		Assert.assertNotNull(result.getCustomer().getUser());
		Assert.assertNotNull(result.getLawyer());
		Assert.assertNotNull(result.getLawyer().getUser());
    	
    	fileDAO.deleteAll();
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

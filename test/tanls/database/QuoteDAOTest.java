package tanls.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.entity.Customer;
import tanls.entity.Question;
import tanls.entity.Quote;
import tanls.entity.TanlsUser;

public class QuoteDAOTest extends BaseTest
{
	@Autowired QuoteDAO quoteDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired CustomerDAO customerDAO;
	@Autowired QuestionDAO questionDAO;
	
    @Test
    public void getFromUser()
    {
    	Quote quote = Generator.quote();
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	Question question = Generator.question();
    	question.setCustomer(customer);
    	questionDAO.put(question);
    	quote.setQuestion(question);
    	quoteDAO.put(quote);
    	
    	Quote result = quoteDAO.getFromUser(user, quote.getId());
    	Assert.assertNotNull(result);
    	
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getFromUserNoUser()
    {
    	Quote quote = Generator.quote();
    	quoteDAO.put(quote);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	Quote result = quoteDAO.getFromUser(user, quote.getId());
    	Assert.assertNull(result);
    	
    	quoteDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

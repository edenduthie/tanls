package tanls.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.CustomerDAO;
import tanls.database.QuestionDAO;
import tanls.database.QuoteDAO;
import tanls.database.TanlsUserDAO;
import tanls.dto.QuoteDTO;
import tanls.entity.Customer;
import tanls.entity.Question;
import tanls.entity.Quote;
import tanls.entity.TanlsUser;
import tanls.service.LoginStatus;

public class QuoteControllerTest extends BaseTest
{
	@Autowired QuoteDAO quoteDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired CustomerDAO customerDAO;
	@Autowired QuestionDAO questionDAO;
	
	@Autowired QuoteController controller;
	
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
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	QuoteDTO result = controller.get(quote.getId());
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.isSuccess());
    	
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
}

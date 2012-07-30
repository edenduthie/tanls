package tanls.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.entity.AreaOfPractise;
import tanls.entity.Customer;
import tanls.entity.Question;

public class QuestionDAOTest extends BaseTest
{
    @Autowired QuestionDAO questionDAO;
    @Autowired CustomerDAO customerDAO;
    @Autowired AreaOfPractiseDAO areaOfPractiseDAO;
    
    @Test
    public void getCustomerQuestions()
    {
    	Customer customer = Generator.customer();
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
    	
    	List<Question> results = questionDAO.getCustomerQuestions(customer, 3, 5);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTime().intValue(),4);
    	Assert.assertEquals(results.get(1).getTime().intValue(),3);
    	Assert.assertEquals(results.get(2).getTime().intValue(),2);
    	
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
    
    @Test
    public void getFromCustomer()
    {
    	Customer customer = Generator.customer();
    	customerDAO.put(customer);
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	
		Question question = Generator.question();
		question.setCustomer(customer);
		question.setAreaOfPractise(area);
		questionDAO.put(question);
	
    	Question result = questionDAO.getFromCustomer(customer, question.getId());
    	Assert.assertNotNull(result);
    	
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
    
    @Test
    public void getFromCustomerNoCustomer()
    {
    	Customer customer = Generator.customer();
    	customerDAO.put(customer);
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	
		Question question = Generator.question();
		question.setAreaOfPractise(area);
		questionDAO.put(question);
	
    	Question result = questionDAO.getFromCustomer(customer, question.getId());
    	Assert.assertNull(result);
    	
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
    
    @Test
    public void getLawyerQuestions()
    {
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		Question question = Generator.question();
    		question.setTime(new Long(i));
    		question.setAreaOfPractise(area);
    		questionDAO.put(question);
    	}
    	
    	List<Question> results = questionDAO.getLawyerQuestions(3, 5);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTime().intValue(),4);
    	Assert.assertEquals(results.get(1).getTime().intValue(),3);
    	Assert.assertEquals(results.get(2).getTime().intValue(),2);
    	
    	questionDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
}

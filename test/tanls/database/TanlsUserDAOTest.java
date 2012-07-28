package tanls.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.TanlsUser;

public class TanlsUserDAOTest extends BaseTest
{
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired LawyerDAO lawyerDAO;
    @Autowired CustomerDAO customerDAO;
    
    @Test
    public void testPutGet()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	TanlsUser result = tanlsUserDAO.get(user.getUsername());
    	Assert.assertTrue(user.getId().equals(result.getId()));
        tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testGetLawyer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Lawyer result = tanlsUserDAO.getLawyer(user);
    	Assert.assertNotNull(result);
    	Assert.assertEquals(result.getId(),lawyer.getId());
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testGetCustomer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
    	Customer result = tanlsUserDAO.getCustomer(user);
    	Assert.assertNotNull(result);
    	Assert.assertEquals(result.getId(),customer.getId());
    	
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

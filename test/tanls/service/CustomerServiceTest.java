package tanls.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.CustomerDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Customer;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

public class CustomerServiceTest extends BaseTest
{
	@Autowired CustomerDAO customerDAO;
	@Autowired CustomerService customerService;
	@Autowired TanlsUserDAO tanlsUserDAO;
	
     @Test
     public void update() throws InvalidInputException
     {
    	 Customer existingCustomer = Generator.customer();
    	 customerDAO.put(existingCustomer);
    	 
    	 Customer newCustomer = Generator.customer();
    	 newCustomer.setName("new name");
    	 newCustomer.setCompanyName("new company");
    	 
    	 Customer result = customerService.update(existingCustomer, newCustomer);
    	 Assert.assertEquals(result.getName(),"new name");
    	 Assert.assertEquals(result.getCompanyName(),"new company");
    	 
    	 customerDAO.deleteAll();
     }
     
     @Test
     public void updateValidation()
     {
    	 Customer existingCustomer = Generator.customer();
    	 customerDAO.put(existingCustomer);
    	 
    	 Customer newCustomer = Generator.customer();
    	 newCustomer.setName("  ");
    	 
    	 try  
    	 {
			customerService.update(existingCustomer, newCustomer);
			Assert.fail();
		 }  
    	 catch (InvalidInputException e) {}
    	 
    	 customerDAO.deleteAll();
     }
     
     @Test
     public void updateFromUser() throws InvalidInputException
     {
    	 TanlsUser user = Generator.tanlsUser();
    	 tanlsUserDAO.put(user);
    	 
    	 Customer existingCustomer = Generator.customer();
    	 existingCustomer.setUser(user);
    	 customerDAO.put(existingCustomer);
    	 
    	 Customer newCustomer = Generator.customer();
    	 newCustomer.setName("new name");
    	 newCustomer.setCompanyName("new company");
    	 
    	 Customer result = customerService.updateFromUser(user, newCustomer);
    	 Assert.assertEquals(result.getName(),"new name");
    	 Assert.assertEquals(result.getCompanyName(),"new company");
    	 
    	 customerDAO.deleteAll();
    	 tanlsUserDAO.deleteAll();
     }
     
     @Test
     public void updateFromUserNoExistingCustomer() throws InvalidInputException
     {
    	 TanlsUser user = Generator.tanlsUser();
    	 tanlsUserDAO.put(user);
    	 
    	 Customer existingCustomer = Generator.customer();
    	 customerDAO.put(existingCustomer);
    	 
    	 Customer newCustomer = Generator.customer();
    	 newCustomer.setName("new name");
    	 newCustomer.setCompanyName("new company");
    	 
    	 try
    	 {
    	     Customer result = customerService.updateFromUser(user, newCustomer);
    	     Assert.fail();
    	 }
    	 catch( InvalidInputException e ) {}
    	 
    	 customerDAO.deleteAll();
    	 tanlsUserDAO.deleteAll();
     }
     
     @Test
     public void updateDoesNotWipeCreditCardDetails() throws InvalidInputException
     {
    	 Customer existingCustomer = Generator.customer();
    	 existingCustomer.setCustomerId("existing");
    	 existingCustomer.setCreditCardToken("existing");
    	 customerDAO.put(existingCustomer);
    	 
    	 Customer newCustomer = Generator.customer();
    	 newCustomer.setName("new name");
    	 newCustomer.setCompanyName("new company");
    	 newCustomer.setCustomerId(null);
    	 newCustomer.setCreditCardToken(null);
    	 
    	 Customer result = customerService.update(existingCustomer, newCustomer);
    	 Assert.assertEquals(result.getName(),"new name");
    	 Assert.assertEquals(result.getCompanyName(),"new company");
    	 Assert.assertEquals(result.getCustomerId(),"existing");
    	 Assert.assertEquals(result.getCreditCardToken(),"existing");
    	 
    	 customerDAO.deleteAll();
     }
}

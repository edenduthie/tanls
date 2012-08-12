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
import tanls.database.TanlsUserDAO;
import tanls.dto.CustomerDTO;
import tanls.entity.Customer;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.LoginStatus;

public class CustomerControllerTest extends BaseTest
{
    @Autowired CustomerDAO customerDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired CustomerController controller;
    
    @Test
    public void get()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	CustomerDTO result = controller.get();
    	Assert.assertEquals(result.getName(),customer.getName());
    	
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
        controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void getNoUser()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(null);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	CustomerDTO result = controller.get();
    	Assert.assertFalse(result.isSuccess());
    	Assert.assertTrue(result.isRedirectToLogin());
    	
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
        controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void getNoCustomer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	CustomerDTO result = controller.get();
    	Assert.assertFalse(result.isSuccess());
    	Assert.assertFalse(result.isRedirectToLogin());
    	
    	tanlsUserDAO.deleteAll();
        controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void update() throws InvalidInputException
    {
   	    TanlsUser user = Generator.tanlsUser();
   	    tanlsUserDAO.put(user);
   	 
        LoginStatus loginStatus = createMock(LoginStatus.class);
 	    expect(loginStatus.getUserService()).andReturn(user);
 	    expect(loginStatus.isLoggedInService()).andReturn(true);
 	    LoginStatus oldLoginStatus = controller.getLoginStatus();
 	    controller.setLoginStatus(loginStatus);
 	    replay(loginStatus);
   	 
   	    Customer existingCustomer = Generator.customer();
   	    existingCustomer.setUser(user);
   	    customerDAO.put(existingCustomer);
   	 
   	    Customer newCustomer = Generator.customer();
   	    newCustomer.setName("new name");
   	    newCustomer.setCompanyName("new company");
   	 
   	    CustomerDTO result = controller.update(new CustomerDTO(newCustomer));
   	    Assert.assertEquals(result.getName(),"new name");
   	    Assert.assertEquals(result.getCompanyName(),"new company");
   	 
   	    customerDAO.deleteAll();
   	    tanlsUserDAO.deleteAll();
   	    controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void updateNoUser() throws InvalidInputException
    {
   	    TanlsUser user = Generator.tanlsUser();
   	    tanlsUserDAO.put(user);
   	 
        LoginStatus loginStatus = createMock(LoginStatus.class);
 	    expect(loginStatus.getUserService()).andReturn(null);
 	    expect(loginStatus.isLoggedInService()).andReturn(true);
 	    LoginStatus oldLoginStatus = controller.getLoginStatus();
 	    controller.setLoginStatus(loginStatus);
 	    replay(loginStatus);
   	 
   	    Customer existingCustomer = Generator.customer();
   	    existingCustomer.setUser(user);
   	    customerDAO.put(existingCustomer);
   	 
   	    Customer newCustomer = Generator.customer();
   	    newCustomer.setName("new name");
   	    newCustomer.setCompanyName("new company");
   	 
   	    CustomerDTO result = controller.update(new CustomerDTO(newCustomer));
   	    Assert.assertFalse(result.isSuccess());
   	    Assert.assertTrue(result.isRedirectToLogin());
   	 
   	    customerDAO.deleteAll();
   	    tanlsUserDAO.deleteAll();
   	    controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void updateInvalidInput() throws InvalidInputException
    {
   	    TanlsUser user = Generator.tanlsUser();
   	    tanlsUserDAO.put(user);
   	 
        LoginStatus loginStatus = createMock(LoginStatus.class);
 	    expect(loginStatus.getUserService()).andReturn(user);
 	    expect(loginStatus.isLoggedInService()).andReturn(true);
 	    LoginStatus oldLoginStatus = controller.getLoginStatus();
 	    controller.setLoginStatus(loginStatus);
 	    replay(loginStatus);
   	 
   	    Customer existingCustomer = Generator.customer();
   	    existingCustomer.setUser(user);
   	    customerDAO.put(existingCustomer);
   	 
   	    Customer newCustomer = Generator.customer();
   	    newCustomer.setName(null);
   	    newCustomer.setCompanyName("new company");
   	 
   	    CustomerDTO result = controller.update(new CustomerDTO(newCustomer));
   	    Assert.assertFalse(result.isSuccess());
   	    Assert.assertFalse(result.isRedirectToLogin());
   	 
   	    customerDAO.deleteAll();
   	    tanlsUserDAO.deleteAll();
   	    controller.setLoginStatus(oldLoginStatus);
    }
}

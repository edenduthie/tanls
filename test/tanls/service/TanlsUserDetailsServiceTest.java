package tanls.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AddressDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Address;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

public class TanlsUserDetailsServiceTest extends BaseTest
{
    @Autowired TanlsUserDetailsService tanlsUserDetailsService;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired AddressDAO addressDAO;
    
    @Test
    public void loadUserByUsername()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	UserDetails result = tanlsUserDetailsService.loadUserByUsername(user.getUsername());
    	Assert.assertEquals(result.getUsername(),user.getUsername());
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void loadUserByUsernameNoneFound()
    {
    	try
    	{
    	    tanlsUserDetailsService.loadUserByUsername("missing");
    	    Assert.fail();
    	}
    	catch( UsernameNotFoundException e ) {}
    }
    
//    @Test
//    public void createOrUpdateAddressCreate() throws InvalidInputException
//    {
//    	TanlsUser user = Generator.tanlsUser();
//    	user.setName("TO BE CHANGED");
//    	tanlsUserDAO.put(user);
//    	Address address = Generator.address();
//    	
//    	tanlsUserDetailsService.createOrUpdateAddress(user, address);
//    	
//    	TanlsUser retrievedUser = tanlsUserDAO.get(user.getId());
//    	Assert.assertNotNull(retrievedUser.getHomeAddress());
//    	Assert.assertEquals(retrievedUser.getName(),address.getFirstName() + " " + address.getLastName());
//    	Assert.assertEquals(retrievedUser.getHomeAddress().getStreet(),address.getStreet());
//    	
//    	tanlsUserDAO.deleteAll();
//    	addressDAO.deleteAll();
//    }
    
//    @Test
//    public void createOrUpdateAddressUpdate() throws InvalidInputException
//    {
//    	Address address = Generator.address();
//    	addressDAO.put(address);
//    	TanlsUser user = Generator.tanlsUser();
//    	user.setName("TO BE CHANGED");
//    	user.setHomeAddress(address);
//    	tanlsUserDAO.put(user);
//    	
//    	Address newAddress = Generator.address();
//    	newAddress.setStreet("CHANGED STREET");
//    	newAddress.setFirstName("hoho");
//    	newAddress.setLastName("haha");
//    	
//    	tanlsUserDetailsService.createOrUpdateAddress(user, newAddress);
//    	
//    	TanlsUser retrievedUser = tanlsUserDAO.get(user.getId());
//    	Assert.assertNotNull(retrievedUser.getHomeAddress());
//    	Assert.assertEquals(retrievedUser.getName(),newAddress.getFirstName() + " " + newAddress.getLastName());
//    	Assert.assertEquals(retrievedUser.getHomeAddress().getStreet(),newAddress.getStreet());
//    	Assert.assertEquals(retrievedUser.getHomeAddress().getId(),address.getId());
//    	
//    	tanlsUserDAO.deleteAll();
//    	addressDAO.deleteAll();
//    }
    
//    @Test
//    public void createOrUpdateAddressCreateValidationFail() throws InvalidInputException
//    {
//    	TanlsUser user = Generator.tanlsUser();
//    	user.setName("TO BE CHANGED");
//    	tanlsUserDAO.put(user);
//    	Address address = Generator.address();
//    	
//    	address.setState(null);
//    	
//    	try
//    	{
//    	    tanlsUserDetailsService.createOrUpdateAddress(user, address);
//    	    Assert.fail("null State was allowed");
//    	}
//    	catch( InvalidInputException e) {}
//    	
//    	tanlsUserDAO.deleteAll();
//    	addressDAO.deleteAll();
//    }
}

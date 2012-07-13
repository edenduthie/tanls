package tanls.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.LawyerDAO;
import tanls.database.TanlsGrantedAuthorityDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.TanlsGrantedAuthority;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

public class AccountServiceTest extends BaseTest
{
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired TanlsGrantedAuthorityDAO tanlsGrantedAuthorityDAO;
    @Autowired AccountService accountService;
    @Autowired LawyerDAO lawyerDAO;
    
    public static final String newEmail = "ya@gmail.com";
    public static final String newPassword = "holygoat";
    
    @Test
    public void testCreate() throws InvalidInputException
    {
    	TanlsUser user = accountService.createAccount("eduthie@gmail.com","password","password","192.0.0.1",null);
    	Assert.assertNotNull(user);
    	Assert.assertEquals(user.getEmail(),"eduthie@gmail.com");
    	Assert.assertEquals(user.getPassword(),accountService.encryptPassword("password"));
    	TanlsUser result = tanlsUserDAO.get("eduthie@gmail.com");
    	Assert.assertTrue(user.getId().equals(result.getId()));
    	
    	Assert.assertEquals(result.getAuthorities().size(), 1);
    	Assert.assertEquals(result.getAuthorities().get(0).getAuthority(),TanlsGrantedAuthority.ROLE_USER);
    	Assert.assertEquals(result.getIp(),"192.0.0.1");
    	Assert.assertNotNull(result.getSignupTime());
    	
    	result.setAuthorities(new ArrayList<TanlsGrantedAuthority>());
    	tanlsUserDAO.update(result);
    	tanlsGrantedAuthorityDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testCreateAlreadyExsits() throws InvalidInputException
    {
    	TanlsUser user = accountService.createAccount("eduthie@gmail.com","password","password","192.0.0.1",null);
    	try
    	{
    	    user = accountService.createAccount(user.getEmail(),"password","password","192.0.0.1",null);
    	    Assert.fail("Allowed creation of a duplicate user");
    	}
    	catch( InvalidInputException e ) {}
    	
    	user.setAuthorities(new ArrayList<TanlsGrantedAuthority>());
    	tanlsUserDAO.update(user);
    	tanlsGrantedAuthorityDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testCreateMissingUsername() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount(null,"password","password","192.0.0.1",null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateUsernameTooShort() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("e@com","password","password","192.0.0.1",null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateUsernameNotEmail() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eawef","password","password","192.0.0.1",null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateeNoPassword() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eduthie@gmail.com",null,"password","192.0.0.1",null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateNoRepeat() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eduthie@gmail.com","password",null,"192.0.0.1",null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreateNoMatch() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eduthie@gmail.com","password","passwor","192.0.0.1",null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testCreatePasswordTooShort() throws InvalidInputException
    {
    	try
    	{
    	    accountService.createAccount("eduthie@gmail.com","ya","ya","192.0.0.1",null);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testUpdateNoUser()
    {
    	try
    	{
    	    accountService.update(null,newEmail,newPassword,newPassword);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void testUpdate() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	accountService.update(user, newEmail, newPassword, newPassword);
    	TanlsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),newEmail);
    	Assert.assertEquals(result.getPassword(),accountService.encryptPassword(newPassword));
    	
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateSameEmail() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	accountService.update(user, user.getEmail(), newPassword, newPassword);
    	TanlsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),user.getEmail());
    	Assert.assertEquals(result.getPassword(),accountService.encryptPassword(newPassword));
    	
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateNoPasswordChange() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	accountService.update(user, newEmail, null, newPassword);
    	TanlsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),newEmail);
    	Assert.assertEquals(result.getPassword(),user.getPassword());
    	
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateExistingEmail() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	TanlsUser existing = Generator.tanlsUser();
    	existing.setEmail(newEmail);
    	tanlsUserDAO.put(existing);
    	
    	try
    	{
    	    accountService.update(user, newEmail, newPassword, newPassword);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdatePasswordsDontMatch() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    
    	try
    	{
    		accountService.update(user, newEmail, newPassword, "different");
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdatePasswordTooShort() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    
    	try
    	{
    		accountService.update(user, newEmail, "ya", "ya");
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateNoEmail() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	accountService.update(user, null, newPassword, newPassword);
    	TanlsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),user.getEmail());
    	Assert.assertEquals(result.getPassword(),accountService.encryptPassword(newPassword));
    	
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testUpdateZeroLengthEmail() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	accountService.update(user, "", newPassword, newPassword);
    	TanlsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),user.getEmail());
    	Assert.assertEquals(result.getPassword(),accountService.encryptPassword(newPassword));
    	
    	tanlsUserDAO.deleteAll();
    }
}

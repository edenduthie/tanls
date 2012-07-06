package tanls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AreaOfPractiseDAO;
import tanls.database.CustomerDAO;
import tanls.database.LawyerDAO;
import tanls.database.ProfileItemDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.AreaOfPractise;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

public class RecommendationServiceTest extends BaseTest
{
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired CustomerDAO customerDAO;
	@Autowired AreaOfPractiseDAO areaOfPractiseDAO;
    @Autowired RecommendationService service;
    @Autowired ProfileItemDAO profileItemDAO;
    @Autowired LawyerDAO lawyerDAO;
	
    @Test
    public void createNoCustomer() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void createExistingCustomer() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	customer.setUser(user);
    	customerDAO.put(customer);
    	customer.setName("new name");
    	pi.setCustomer(customer);
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	Assert.assertEquals(customerResult.getName(),"new name");
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void createNoCustomerAreaOfPractise() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaOfPractiseDAO.put(area);
    	pi.setAreaOfPractise(area);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	Assert.assertNotNull(result.getAreaOfPractise());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
    
    @Test
    public void createNoCustomerNoLawyer() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	//pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	try
    	{
    	    service.create(user,pi);
    	}
    	catch( InvalidInputException e ) {}
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void createNoCustomerTryingToRecommendYourself() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	try
    	{
    	    service.create(user,pi);
    	}
    	catch(InvalidInputException e) {}
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void createExistingRecommendation() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	try
    	{
    		service.create(user,pi);
    	}
    	catch(InvalidInputException e) {}
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void update() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	ProfileItem newProfileItem = Generator.profileItem();
    	newProfileItem.setType(ProfileItem.RECOMMENDATION);
    	newProfileItem.setId(result.getId());
    	newProfileItem.setText("changed");
    	
    	service.edit(user, newProfileItem);
    	
    	result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	Assert.assertEquals(result.getText(),"changed");
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateNoCUstomer() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	ProfileItem newProfileItem = Generator.profileItem();
    	newProfileItem.setType(ProfileItem.RECOMMENDATION);
    	newProfileItem.setId(666);
    	newProfileItem.setText("changed");
    	
    	try
    	{
    	    service.edit(user, newProfileItem);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateNoExistingPI() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	ProfileItem newProfileItem = Generator.profileItem();
    	newProfileItem.setType(ProfileItem.RECOMMENDATION);
    	newProfileItem.setId(999);
    	newProfileItem.setText("changed");
    	
    	try
    	{
    	    service.edit(user, newProfileItem);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateNotRecommendation() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.ARTICLE);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	ProfileItem newProfileItem = Generator.profileItem();
    	newProfileItem.setType(ProfileItem.RECOMMENDATION);
    	newProfileItem.setId(result.getId());
    	newProfileItem.setText("changed");
    	
    	try
    	{
    	    service.edit(user, newProfileItem);
    	}
    	catch(InvalidInputException e) {}
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateNotOwner() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	ProfileItem newProfileItem = Generator.profileItem();
    	newProfileItem.setType(ProfileItem.RECOMMENDATION);
    	newProfileItem.setId(result.getId());
    	newProfileItem.setText("changed");
    	
    	TanlsUser newUser = Generator.tanlsUser();
    	tanlsUserDAO.put(newUser);
    	Customer newCustomer = Generator.customer();
    	newCustomer.setUser(newUser);
    	customerDAO.put(newCustomer);
    	
    	try
    	{
    	    service.edit(newUser, newProfileItem);
    	}
    	catch(InvalidInputException e) {}
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void deleteCustomer() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	ProfileItem dummyItem = Generator.profileItem();
    	dummyItem.setId(result.getId());
    	service.delete(user, dummyItem);
    	
    	List<ProfileItem> items = profileItemDAO.getAll();
    	Assert.assertEquals(items.size(),0);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void deleteLaywer() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	
    	TanlsUser lawyerUser = Generator.tanlsUser();
    	tanlsUserDAO.put(lawyerUser);
    	lawyer.setUser(lawyerUser);
    	lawyerDAO.update(lawyer);
    	
    	ProfileItem dummyItem = Generator.profileItem();
    	dummyItem.setId(result.getId());
    	service.delete(lawyerUser, dummyItem);
    	
    	List<ProfileItem> items = profileItemDAO.getAll();
    	Assert.assertEquals(items.size(),0);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void deleteNotCustomerOrLawyer() throws InvalidInputException
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setType(ProfileItem.RECOMMENDATION);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	pi.setCustomer(customer);
    	
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	service.create(user,pi);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(result.getCustomer());
    	
    	Customer customerResult = tanlsUserDAO.getCustomer(user);
    	Assert.assertEquals(customerResult.getId(),result.getCustomer().getId());
    	
    	
    	TanlsUser invalidUser = Generator.tanlsUser();
    	tanlsUserDAO.put(invalidUser);
    	
    	ProfileItem dummyItem = Generator.profileItem();
    	dummyItem.setId(result.getId());
    	try
    	{
    	    service.delete(invalidUser, dummyItem);
    	    Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

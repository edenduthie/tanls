package tanls.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.CustomerDAO;
import tanls.database.LawyerDAO;
import tanls.database.ProfileItemDAO;
import tanls.database.TanlsGrantedAuthorityDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;
import tanls.entity.TanlsGrantedAuthority;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.exception.TanlsSecurityException;

public class ProfileItemServiceTest extends BaseTest
{
    @Autowired ProfileItemService service;
    @Autowired ProfileItemDAO profileItemDAO;
    @Autowired LawyerDAO lawyerDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired TanlsGrantedAuthorityDAO tanlsGrantedAuthorityDAO;
    @Autowired CustomerDAO customerDAO;
    
    
    @Test
    public void allowedUpdateNoUser() throws InvalidInputException
    {
    	try
    	{
    		service.allowedUpdate(null, null);
    		Assert.fail();
    	}
    	catch( TanlsSecurityException e )
    	{
    		Assert.assertTrue(e.isRedirectToLogin());
    	}
    }
    
    @Test
    public void allowedUpdateLawyerIdNull() throws InvalidInputException, TanlsSecurityException
    {
    	TanlsUser user = Generator.tanlsUser();
    	
    	ProfileItem pi = Generator.profileItem();
    	Lawyer lawyer = Generator.lawyer();
    	pi.setLawyer(lawyer);
    	
    	try
    	{
    		service.allowedUpdate(pi, user);
    		Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    }
    
    @Test
    public void allowedUpdate() throws InvalidInputException, TanlsSecurityException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	ProfileItem pi = Generator.profileItem();
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	profileItemDAO.put(pi);
    	
    	service.allowedUpdate(pi, user);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void allowedUpdateDifferentLawyer() throws InvalidInputException, TanlsSecurityException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer1 = Generator.lawyer();
    	lawyer1.setUser(user);
    	lawyerDAO.put(lawyer1);
    	
    	ProfileItem pi = Generator.profileItem();
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	pi.setLawyer(lawyer);
    	profileItemDAO.put(pi);
    	
    	try
    	{
    	    service.allowedUpdate(pi, user);
    	}
    	catch( InvalidInputException e ) {}
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void crud() throws TanlsSecurityException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	ProfileItem profileItem = Generator.profileItem();
    	Lawyer dummyLawyer = new Lawyer();
    	dummyLawyer.setId(lawyer.getId());
    	profileItem.setLawyer(dummyLawyer);
    	
    	service.create(profileItem, user);
    	
    	ProfileItem result = service.get(profileItem.getId());
    	Assert.assertNotNull(result.getLawyer());
    	Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
    	Assert.assertEquals(result.getLawyer().getName(),lawyer.getName());
    	
    	result.setPostedTime(666l);
    	result.setLawyer(dummyLawyer);
    	
    	service.update(result, user);
    	
    	result = service.get(profileItem.getId());
    	Assert.assertEquals(result.getPostedTime().intValue(),666);
    	Assert.assertNotNull(result.getLawyer());
    	Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
    	Assert.assertEquals(result.getLawyer().getName(),lawyer.getName());
    	
    	service.delete(result, user);
    	
    	result = service.get(profileItem.getId());
    	Assert.assertNull(result);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void search()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		ProfileItem profileItem = Generator.profileItem();
    		profileItem.setLawyer(lawyer);
    		profileItem.setTimelineTime(new Long(i));
    		profileItemDAO.put(profileItem);
    	}
    	
    	List<ProfileItem> results = service.search(lawyer.getId(),ProfileItem.JOB,7l,3,null);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTimelineTime().intValue(),6);
    	Assert.assertEquals(results.get(1).getTimelineTime().intValue(),5);
    	Assert.assertEquals(results.get(2).getTimelineTime().intValue(),4);
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void updateTryToChangeStatusNotAdmin() throws TanlsSecurityException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	ProfileItem profileItem = Generator.profileItem();
    	Lawyer dummyLawyer = new Lawyer();
    	dummyLawyer.setId(lawyer.getId());
    	profileItem.setLawyer(dummyLawyer);
    	
    	service.create(profileItem, user);
    	
    	ProfileItem result = service.get(profileItem.getId());
    	Assert.assertNotNull(result.getLawyer());
    	Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
    	Assert.assertEquals(result.getLawyer().getName(),lawyer.getName());
    	
    	result.setPostedTime(666l);
    	result.setLawyer(dummyLawyer);
    	result.setStatus(ProfileItem.APPROVED_STATUS);
    	
    	try
    	{
    	    service.updateStatus(result, user);
    	    Assert.fail();
    	}
    	catch( TanlsSecurityException e ) {} 
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateTryToChangeStatusAdmin() throws TanlsSecurityException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.add(tanlsGrantedAuthorityDAO.getRoleAdmin());
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyer.setPoints(1);
    	lawyerDAO.put(lawyer);
    	
    	ProfileItem profileItem = Generator.profileItem();
    	Lawyer dummyLawyer = new Lawyer();
    	dummyLawyer.setId(lawyer.getId());
    	profileItem.setLawyer(dummyLawyer);
    	profileItem.setStatus(ProfileItem.NEW_STATUS);
    	
    	service.create(profileItem, user);
    	
    	ProfileItem result = service.get(profileItem.getId());
    	Assert.assertNotNull(result.getLawyer());
    	Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
    	Assert.assertEquals(result.getLawyer().getName(),lawyer.getName());
    	
    	result.setPostedTime(666l);
    	result.setLawyer(dummyLawyer);
    	result.setStatus(ProfileItem.APPROVED_STATUS);
    	
        service.updateStatus(result, user);
        
        Lawyer lawyerResult = lawyerDAO.get(lawyer.getId());
        Assert.assertEquals(lawyerResult.getPoints().intValue(),1+ProfileItemService.POINTS_FOR_APPROVED_ARTICLE);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	user.setAuthorities(new ArrayList<TanlsGrantedAuthority>());
    	tanlsUserDAO.update(user);
    	tanlsUserDAO.deleteAll();
    	tanlsGrantedAuthorityDAO.deleteAll();
    }
    
    @Test
    public void searchNoText()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		ProfileItem profileItem = Generator.profileItem();
    		profileItem.setLawyer(lawyer);
    		profileItem.setTimelineTime(new Long(i));
    		profileItemDAO.put(profileItem);
    	}
    	
    	List<ProfileItem> results = service.searchNoText(lawyer.getId(),ProfileItem.JOB,7l,3,null);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTimelineTime().intValue(),6);
    	Assert.assertEquals(results.get(1).getTimelineTime().intValue(),5);
    	Assert.assertEquals(results.get(2).getTimelineTime().intValue(),4);
    	for( ProfileItem result : results ) Assert.assertNull(result.getText() );
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void getWithLawyer()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
		ProfileItem profileItem = Generator.profileItem();
		profileItem.setLawyer(lawyer);
		profileItemDAO.put(profileItem);
    	
    	ProfileItem result = service.get(profileItem.getTitle());
    	Assert.assertEquals(result.getTitle(),profileItem.getTitle());
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void checkTitleDoesntExist() throws InvalidInputException
    {
    	ProfileItem item = Generator.profileItem();
    	item.setTitle("How in the world do they do it?");
        service.checkTitle(item);
        Assert.assertEquals(item.getTitle(),"How in the world do they do it");
    }
    
    @Test
    public void checkTitleExists() throws InvalidInputException
    {
    	ProfileItem item = Generator.profileItem();
    	item.setType(ProfileItem.ARTICLE);
    	profileItemDAO.put(item);
        
    	ProfileItem newItem = Generator.profileItem();
    	newItem.setType(ProfileItem.ARTICLE);
    	newItem.setTitle(item.getTitle());
    	
    	try
    	{
            service.checkTitle(newItem);
            Assert.fail();
    	}
    	catch(InvalidInputException e ) {}
        
        profileItemDAO.deleteAll();
    }
    
    @Test
    public void checkTitleExistsButOwnTitle() throws InvalidInputException
    {
    	ProfileItem item = Generator.profileItem();
    	item.setType(ProfileItem.ARTICLE);
    	profileItemDAO.put(item);
        
    	service.checkTitle(item);
        
        profileItemDAO.deleteAll();
    }
    
    @Test
    public void checkTitleExistsButNotArticle() throws InvalidInputException
    {
    	ProfileItem item = Generator.profileItem();
    	item.setType(ProfileItem.STATUS_UPDATE);
    	profileItemDAO.put(item);
        
    	ProfileItem newItem = Generator.profileItem();
    	newItem.setTitle(item.getTitle());
    	
    	service.checkTitle(newItem);
        
        profileItemDAO.deleteAll();
    }
    
    @Test
    public void deleteFeedback() throws TanlsSecurityException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	ProfileItem profileItem = Generator.profileItem();
    	profileItem.setType(ProfileItem.FEEDBACK);
    	profileItemDAO.put(profileItem);
    	
    	try
    	{
    	    service.delete(profileItem, user);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	profileItemDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
}

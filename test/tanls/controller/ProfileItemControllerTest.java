package tanls.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.LawyerDAO;
import tanls.database.ProfileItemDAO;
import tanls.database.TanlsGrantedAuthorityDAO;
import tanls.database.TanlsUserDAO;
import tanls.dto.LawyerDTO;
import tanls.dto.ProfileItemDTO;
import tanls.dto.ProfileItemSearchResponse;
import tanls.dto.ResponseDTO;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;
import tanls.entity.TanlsGrantedAuthority;
import tanls.entity.TanlsUser;
import tanls.service.LoginStatus;

public class ProfileItemControllerTest extends BaseTest
{
    @Autowired ProfileItemController controller;
    @Autowired ProfileItemDAO profileItemDAO;
    @Autowired LawyerDAO lawyerDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired TanlsGrantedAuthorityDAO authDAO;
    
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
    	
    	ProfileItemSearchResponse response = controller.items(lawyer.getId(),ProfileItem.JOB,7l,3,null);
    	Assert.assertTrue(response.isSuccess());
    	Assert.assertFalse(response.isRedirectToLogin());
    	Assert.assertEquals(response.getProfileItems().size(),3);
    	Assert.assertEquals(response.getProfileItems().get(0).getTimelineTime().intValue(),6);
    	Assert.assertEquals(response.getProfileItems().get(1).getTimelineTime().intValue(),5);
    	Assert.assertEquals(response.getProfileItems().get(2).getTimelineTime().intValue(),4);
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
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
    	
    	ProfileItemSearchResponse response = controller.itemsNoText(ProfileItem.JOB,7l,3,null);
    	Assert.assertTrue(response.isSuccess());
    	Assert.assertFalse(response.isRedirectToLogin());
    	Assert.assertEquals(response.getProfileItems().size(),3);
    	Assert.assertEquals(response.getProfileItems().get(0).getTimelineTime().intValue(),6);
    	Assert.assertEquals(response.getProfileItems().get(1).getTimelineTime().intValue(),5);
    	Assert.assertEquals(response.getProfileItems().get(2).getTimelineTime().intValue(),4);
    	for( ProfileItemDTO dto : response.getProfileItems() ) Assert.assertNull(dto.getText());
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void searchNoLawyer()
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
    	
    	ProfileItemSearchResponse response = controller.itemsNoLawyer(ProfileItem.JOB,7l,3,null);
    	Assert.assertTrue(response.isSuccess());
    	Assert.assertFalse(response.isRedirectToLogin());
    	Assert.assertEquals(response.getProfileItems().size(),3);
    	Assert.assertEquals(response.getProfileItems().get(0).getTimelineTime().intValue(),6);
    	Assert.assertEquals(response.getProfileItems().get(1).getTimelineTime().intValue(),5);
    	Assert.assertEquals(response.getProfileItems().get(2).getTimelineTime().intValue(),4);
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void create()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItemDTO profileItemDTO = new ProfileItemDTO(Generator.profileItem());
    	LawyerDTO dummyLawyer = new LawyerDTO();
    	dummyLawyer.setId(lawyer.getId());
    	profileItemDTO.setLawyer(dummyLawyer);
    	
    	ProfileItemDTO result = controller.create(profileItemDTO);
    	
    	Assert.assertTrue(result.isSuccess());
    	Assert.assertFalse(result.isRedirectToLogin());
    	Assert.assertNotNull(result);
    	Assert.assertNotNull(result.getId());
    	Assert.assertNotNull(result.getLawyer());
    	Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void createSecurityException()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(null);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItemDTO profileItemDTO = new ProfileItemDTO(Generator.profileItem());
    	LawyerDTO dummyLawyer = new LawyerDTO();
    	dummyLawyer.setId(lawyer.getId());
    	profileItemDTO.setLawyer(dummyLawyer);
    	
    	ProfileItemDTO result = controller.create(profileItemDTO);
    	
    	Assert.assertFalse(result.isSuccess());
    	Assert.assertTrue(result.isRedirectToLogin());
    	
    	ProfileItemSearchResponse response = controller.items(lawyer.getId(), null, null, null, null);
    	Assert.assertEquals(response.getProfileItems().size(),0);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void createInvalidInputException()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItemDTO profileItemDTO = new ProfileItemDTO(Generator.profileItem());
    	
    	ProfileItemDTO result = controller.create(profileItemDTO);
    	
    	Assert.assertFalse(result.isSuccess());
    	Assert.assertFalse(result.isRedirectToLogin());
    	
    	ProfileItemSearchResponse response = controller.items(lawyer.getId(), null, null, null, null);
    	Assert.assertEquals(response.getProfileItems().size(),0);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void update()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItem pi = Generator.profileItem();
    	pi.setLawyer(lawyer);
    	profileItemDAO.put(pi);
    	
    	pi.setSubtitle("new subtitle");
    	
    	ProfileItemDTO result = controller.update(new ProfileItemDTO(pi));
    	
    	Assert.assertTrue(result.isSuccess());
    	Assert.assertFalse(result.isRedirectToLogin());
    	Assert.assertNotNull(result);
    	Assert.assertNotNull(result.getId());
    	Assert.assertNotNull(result.getLawyer());
    	Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
    	
    	Assert.assertEquals(result.getSubtitle(),"new subtitle");
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void updateSecurityException()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(null);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItem pi = Generator.profileItem();
    	pi.setLawyer(lawyer);
    	profileItemDAO.put(pi);
    	
    	pi.setSubtitle("new subtitle");
    	
    	ProfileItemDTO result = controller.update(new ProfileItemDTO(pi));
    	
    	Assert.assertFalse(result.isSuccess());
    	Assert.assertTrue(result.isRedirectToLogin());
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void updateInvalidInputException()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItem pi = Generator.profileItem();
    	pi.setLawyer(lawyer);
    	profileItemDAO.put(pi);
    	
    	pi.setSubtitle("new subtitle");
    	ProfileItemDTO dto = new ProfileItemDTO(pi);
    	dto.setLawyer(null);
    	
    	ProfileItemDTO result = controller.update(dto);
    	
    	Assert.assertFalse(result.isSuccess());
    	Assert.assertFalse(result.isRedirectToLogin());
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void updateStatus()
    {
    	TanlsUser user = Generator.tanlsUser();
    	TanlsGrantedAuthority admin = authDAO.getRoleAdmin();
    	user.add(admin);
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItem pi = Generator.profileItem();
    	pi.setLawyer(lawyer);
    	pi.setStatus(ProfileItem.NEW_STATUS);
    	profileItemDAO.put(pi);
    	
    	pi.setStatus(ProfileItem.APPROVED_STATUS);
    	
    	ProfileItemDTO result = controller.updateStatus(new ProfileItemDTO(pi));
    	
    	Assert.assertTrue(result.isSuccess());
    	Assert.assertFalse(result.isRedirectToLogin());
    	Assert.assertNotNull(result);
    	Assert.assertNotNull(result.getId());
    	Assert.assertNotNull(result.getLawyer());
    	Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
    	Assert.assertEquals(result.getStatus(),ProfileItem.APPROVED_STATUS);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	user.setAuthorities(new ArrayList<TanlsGrantedAuthority>());
    	tanlsUserDAO.update(user);
    	tanlsUserDAO.deleteAll();
    	authDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void delete()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItem pi = Generator.profileItem();
    	pi.setLawyer(lawyer);
    	profileItemDAO.put(pi);
    	
    	ResponseDTO result = controller.delete(new ProfileItemDTO(pi));
    	
    	Assert.assertTrue(result.isSuccess());
    	Assert.assertFalse(result.isRedirectToLogin());
    	
    	ProfileItem retrieved = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNull(retrieved);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void deleteSecurityException()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(null);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItem pi = Generator.profileItem();
    	pi.setLawyer(lawyer);
    	profileItemDAO.put(pi);
    	
    	ResponseDTO result = controller.delete(new ProfileItemDTO(pi));
    	
    	Assert.assertFalse(result.isSuccess());
    	Assert.assertTrue(result.isRedirectToLogin());
    	
    	ProfileItem retrieved = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(retrieved);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void deleteInvalidInputException()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	ProfileItem pi = Generator.profileItem();
    	profileItemDAO.put(pi);
    	
    	ResponseDTO result = controller.delete(new ProfileItemDTO(pi));
    	
    	Assert.assertFalse(result.isSuccess());
    	Assert.assertFalse(result.isRedirectToLogin());
    	
    	ProfileItem retrieved = profileItemDAO.getWithLawyer(pi.getId());
    	Assert.assertNotNull(retrieved);
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void get()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	ProfileItem pi = Generator.profileItem();
    	pi.setLawyer(lawyer);
    	profileItemDAO.put(pi);
    	
    	pi.setSubtitle("new subtitle");
    	
    	ProfileItemDTO result = controller.get(pi.getTitle());
    	Assert.assertEquals(result.getId(),pi.getId());
    	
    	profileItemDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
}

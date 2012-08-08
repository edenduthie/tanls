package tanls.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.LawyerDAO;
import tanls.database.PhotoDAO;
import tanls.database.PhotoDataDAO;
import tanls.database.TanlsUserDAO;
import tanls.dto.LawyerDTO;
import tanls.dto.ResponseDTO;
import tanls.entity.Lawyer;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.LoginStatus;

public class LawyerProfileControllerTest extends BaseTest
{
    @Autowired LawyerProfileController controller;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired LawyerDAO lawyerDAO;
    @Autowired PhotoDAO photoDAO;
    @Autowired PhotoDataDAO photoDataDAO;
	
    String photoFileName = "testdata/anacatanchin.jpg";
    
    @Test
    public void profile()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
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
    	
    	LawyerDTO lawyerDTO = controller.profile();
    	Assert.assertNotNull(lawyerDTO);
    	Assert.assertNotNull(lawyerDTO.getId());
    	Assert.assertTrue(lawyerDTO.isSuccess());
    	
    	controller.setLoginStatus(oldLoginStatus);
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void profileNotALawyer()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(false);
    	tanlsUserDAO.put(user);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	LawyerDTO lawyerDTO = controller.profile();
    	Assert.assertNotNull(lawyerDTO);
    	Assert.assertFalse(lawyerDTO.isSuccess());
    	
    	controller.setLoginStatus(oldLoginStatus);
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void profileUserNull()
    {
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(null);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	LawyerDTO lawyerDTO = controller.profile();
    	Assert.assertNotNull(lawyerDTO);
    	Assert.assertFalse(lawyerDTO.isSuccess());
    	
    	controller.setLoginStatus(oldLoginStatus);
    }
    
    @Test
    public void update()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
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
    	
    	lawyer.setFacebook("ya");
    	
    	ResponseDTO response = controller.edit(new LawyerDTO(lawyer));
    	Assert.assertTrue(response.success);
    	
    	Lawyer edited = lawyerDAO.get(lawyer.getId());
    	Assert.assertEquals(edited.getFacebook(),lawyer.getFacebook());
    	
    	controller.setLoginStatus(oldLoginStatus);
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateWrongId()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
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
    	
    	lawyer.setId(666);
    	
    	ResponseDTO response = controller.edit(new LawyerDTO(lawyer));
    	Assert.assertFalse(response.isSuccess());
    	
    	controller.setLoginStatus(oldLoginStatus);
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateNoUser()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
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
    	
    	lawyer.setFacebook("ya");
    	
    	ResponseDTO response = controller.edit(new LawyerDTO(lawyer));
    	Assert.assertFalse(response.isSuccess());
    	
    	controller.setLoginStatus(oldLoginStatus);
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void uploadPhoto() throws IOException, InvalidInputException
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
    	
        FileInputStream fos = new FileInputStream(new File(photoFileName));
    	
    	CommonsMultipartFile mockFile = createMock(CommonsMultipartFile.class);
    	expect(mockFile.getOriginalFilename()).andReturn("anacatanchin.jpg");
    	expect(mockFile.getInputStream()).andReturn(fos);
    	expect(mockFile.getContentType()).andReturn("image/jpg");
    	expect(mockFile.getSize()).andReturn(666l);
    	expect(mockFile.isEmpty()).andReturn(false);
    	replay(mockFile);
    	
    	controller.uploadBackgroundPhoto(mockFile);
    	
    	Lawyer result = lawyerDAO.get(lawyer.getId());
    	Assert.assertNotNull(result.getBackgroundPhoto());
    	Assert.assertNotNull(result.getBackgroundPhoto().getId());
    	
    	controller.setLoginStatus(oldLoginStatus);
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	photoDAO.deleteAll();
    	photoDataDAO.deleteAll();
    }
    
    @Test
    public void uploadPhotoNoUser() throws IOException, InvalidInputException
    {
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(null);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	CommonsMultipartFile mockFile = createMock(CommonsMultipartFile.class);
    	
    	String response = controller.uploadBackgroundPhoto(mockFile);
    	Assert.assertEquals(response,"{\"message\":\"Please log in to upload files.\",\"success\":\"false\"}");
    }
    
    @Test
    public void uploadProfilePhoto() throws IOException, InvalidInputException
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
    	
        FileInputStream fos = new FileInputStream(new File(photoFileName));
    	
    	CommonsMultipartFile mockFile = createMock(CommonsMultipartFile.class);
    	expect(mockFile.getOriginalFilename()).andReturn("anacatanchin.jpg");
    	expect(mockFile.getInputStream()).andReturn(fos);
    	expect(mockFile.getContentType()).andReturn("image/jpg");
    	expect(mockFile.getSize()).andReturn(666l);
    	expect(mockFile.isEmpty()).andReturn(false);
    	replay(mockFile);
    	
    	controller.uploadProfilePhoto(mockFile);
    	
    	Lawyer result = lawyerDAO.get(lawyer.getId());
    	Assert.assertNotNull(result.getProfilePhoto());
    	Assert.assertNotNull(result.getProfilePhoto().getId());
    	
    	controller.setLoginStatus(oldLoginStatus);
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	photoDAO.deleteAll();
    	photoDataDAO.deleteAll();
    }
}

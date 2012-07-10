package tanls.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AreaOfPractiseDAO;
import tanls.database.LawyerDAO;
import tanls.database.PhotoDAO;
import tanls.database.PhotoDataDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.AreaOfPractise;
import tanls.entity.Lawyer;
import tanls.entity.Photo;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

public class LawyerServiceTest extends BaseTest
{
    @Autowired LawyerService lawyerService;
    @Autowired LawyerDAO lawyerDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired PhotoDAO photoDAO;
    @Autowired PhotoDataDAO photoDataDAO;
    @Autowired PhotoService photoService;
    @Autowired AreaOfPractiseDAO areaOfPractiseDAO;
    
    String photoFileName = "testdata/anacatanchin.jpg";
    
    @Test
    public void getLawyerFromUserNotFound() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer newLawyer = lawyerService.getLawyerFromUser(user);
    	Assert.assertNull(newLawyer);
    	
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getFromUserFound() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Lawyer newLawyer = lawyerService.getLawyerFromUser(user);
    	Assert.assertNotNull(newLawyer.getId());
    	Assert.assertEquals(newLawyer.getId(),lawyer.getId());
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getLawyerFromUserNotALawyer() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	try
    	{
    	    lawyerService.getLawyerFromUser(user);
    	    Assert.fail();
    	}
    	catch(InvalidInputException e) {}

    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getSuggestedUsernameEmail() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	
    	String username = lawyerService.getSuggestedUsername(lawyer);
    	Assert.assertEquals(username,"eden");
    }
    
    @Test
    public void testSetUsernameEmail()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	lawyerService.setUsername(lawyer);
    	
    	Lawyer result = lawyerDAO.get(lawyer.getId());
    	Assert.assertEquals(result.getUsername(),"eden");
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testSetUsernameNumber()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	user.setEmail("123@hotmail.com");
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	lawyerService.setUsername(lawyer);
    	
    	Lawyer result = lawyerDAO.get(lawyer.getId());
    	Assert.assertEquals(result.getUsername(),"lawyer"+lawyer.getId().toString());
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testSetUsernameEmailTaken()
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	user.setEmail("eduthie@gmail.com");
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyer.setUsername("eduthie");
    	lawyerDAO.put(lawyer);
    	
    	lawyerService.setUsername(lawyer);
    	
    	Lawyer result = lawyerDAO.get(lawyer.getId());
    	Assert.assertEquals(result.getUsername(),"eduthie"+lawyer.getId().toString());
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void update() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Lawyer newLawyer = new Lawyer();
    	newLawyer.setUser(user);
    	newLawyer.setUsername("updated");
    	newLawyer.setId(lawyer.getId());
    	newLawyer.setName("new name");
    	newLawyer.setPractitionerNumber("234234");
    	
    	lawyerService.update(newLawyer);
    	
    	Lawyer result = lawyerDAO.get(newLawyer.getId());
    	Assert.assertEquals(result.getUsername(),"updated");
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void updateDifferentLawyer() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Lawyer newLawyer = new Lawyer();
    	newLawyer.setUser(user);
    	newLawyer.setId(666);
    	
    	try
    	{
    	    lawyerService.update(newLawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void testValidateChange() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setUsername("hihosilver");
    	
    	lawyerService.validate(lawyer);
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateNoChange() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyerService.validate(lawyer);
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateNullUsername() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setUsername(null);
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateEmptyUsername() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setUsername("");
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateUsernameNumber() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setUsername("23423423");
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateUsernameAlreadyExists() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	Lawyer lawyer2 = Generator.lawyer();
    	lawyer2.setUsername("lawyer2");
    	lawyerDAO.put(lawyer2);
    	
    	lawyer.setUsername(lawyer2.getUsername());
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateNoName() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setName(null);
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateZeroLengthName() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setName(" ");
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateLawyerNameSpecialCharacters() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setName("*&#&#");
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateUsernameSpecialCharacters() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setUsername("*&#&#");
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateNoPractitionerNumber() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setPractitionerNumber(null);
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateZeroLengthPractitionerNumber() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setPractitionerNumber(" ");
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateLawyerPractitionerNumberSpecialCharacters() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setPractitionerNumber("*&#&#");
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testValidateLawyerPractitionerInvalidState() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	lawyer.setState("JSH");
    	
    	try
    	{
    	    lawyerService.validate(lawyer);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testTooLongName() throws InvalidInputException 
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	try
    	{
    		lawyer.setName(generateString(41));
    	    lawyerService.validate(lawyer);
    
    	    Assert.fail();
    	}
    	catch( InvalidInputException e) {}
    	
    	lawyerDAO.deleteAll();
    }
    
    public String generateString(int length)
    {
    	StringBuffer stringBuffer = new StringBuffer();
    	for( int i=0; i < length; ++i ) stringBuffer.append('a');
    	return stringBuffer.toString();
    	
    }
    
    @Test
    public void uploadPhotoNoLawyer() throws IOException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
        FileInputStream fos = new FileInputStream(new File(photoFileName));
    	
    	CommonsMultipartFile mockFile = createMock(CommonsMultipartFile.class);
    	expect(mockFile.getOriginalFilename()).andReturn("anacatanchin.jpg");
    	expect(mockFile.getInputStream()).andReturn(fos);
    	expect(mockFile.getContentType()).andReturn("image/jpg");
    	expect(mockFile.getSize()).andReturn(666l);
    	expect(mockFile.isEmpty()).andReturn(false);
    	replay(mockFile);
    	
    	try
    	{
    	    lawyerService.uploadPhoto(mockFile, user, LawyerService.BACKGROUND_PHOTO);
    	    Assert.fail();
    	}
    	catch( InvalidInputException e ) {}
    	
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
    	
        FileInputStream fos = new FileInputStream(new File(photoFileName));
    	
    	CommonsMultipartFile mockFile = createMock(CommonsMultipartFile.class);
    	expect(mockFile.getOriginalFilename()).andReturn("anacatanchin.jpg");
    	expect(mockFile.getInputStream()).andReturn(fos);
    	expect(mockFile.getContentType()).andReturn("image/jpg");
    	expect(mockFile.getSize()).andReturn(666l);
    	expect(mockFile.isEmpty()).andReturn(false);
    	replay(mockFile);
    	
    	lawyerService.uploadPhoto(mockFile, user, LawyerService.BACKGROUND_PHOTO);
    	
    	Lawyer result = lawyerDAO.get(lawyer.getId());
    	Assert.assertNotNull(result.getBackgroundPhoto());
    	Assert.assertNotNull(result.getBackgroundPhoto().getId());
    	
    	fos.close();
    	fos = new FileInputStream(new File(photoFileName));
    	
    	CommonsMultipartFile mockFile2 = createMock(CommonsMultipartFile.class);
    	expect(mockFile2.getOriginalFilename()).andReturn("newname.jpg");
    	expect(mockFile2.getInputStream()).andReturn(fos);
    	expect(mockFile2.getContentType()).andReturn("image/jpg");
    	expect(mockFile2.getSize()).andReturn(666l);
    	expect(mockFile2.isEmpty()).andReturn(false);
    	replay(mockFile2);
    	
    	lawyerService.uploadPhoto(mockFile2, user, LawyerService.BACKGROUND_PHOTO);
    	
    	result = lawyerDAO.get(lawyer.getId());
    	Assert.assertNotNull(result.getBackgroundPhoto());
    	Assert.assertEquals(result.getBackgroundPhoto().getName(),"newname.jpg");
    	
    	List<Photo> photos = photoDAO.getAll();
    	Assert.assertEquals(photos.size(),1);
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	photoDAO.deleteAll();
    	photoDataDAO.deleteAll();
    }
    
    @Test
    public void updatePhotoDoesntClobber() throws InvalidInputException, FileNotFoundException, IOException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	
    	Photo backgroundPhoto = photoService.create(new FileInputStream(photoFileName));
    	lawyer.setBackgroundPhoto(backgroundPhoto);
    	Photo profilePhoto = photoService.create(new FileInputStream(photoFileName));
    	lawyer.setProfilePhoto(profilePhoto);
    	
    	lawyerDAO.put(lawyer);
    	
    	Lawyer newLawyer = new Lawyer();
    	newLawyer.setUser(user);
    	newLawyer.setUsername("updated");
    	newLawyer.setId(lawyer.getId());
    	newLawyer.setName("new name");
    	newLawyer.setPractitionerNumber("234234");
    	
    	Photo newBackground = new Photo();
    	newBackground.setId(backgroundPhoto.getId());
    	newBackground.setPositionX(222);
    	newLawyer.setBackgroundPhoto(newBackground);
    	
    	Photo newProfile = new Photo();
    	newProfile.setId(profilePhoto.getId());
    	newProfile.setPositionX(777);
    	newLawyer.setProfilePhoto(newProfile);
    	
    	lawyerService.update(newLawyer);
    	
    	Lawyer result = lawyerDAO.get(newLawyer.getId());
    	Assert.assertEquals(result.getUsername(),"updated");
    	Assert.assertEquals(result.getBackgroundPhoto().getPositionX(),newBackground.getPositionX());
    	Assert.assertEquals(result.getProfilePhoto().getPositionX(),newProfile.getPositionX());
    	Assert.assertEquals(photoDAO.getWithData(result.getBackgroundPhoto().getId()).getData().getId(),backgroundPhoto.getData().getId());
    	Assert.assertEquals(photoDAO.getWithData(result.getProfilePhoto().getId()).getData().getId(),profilePhoto.getData().getId());
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	photoDAO.deleteAll();
    	photoDataDAO.deleteAll();
    }
    
    @Test
    public void uploadPhotoProfile() throws IOException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
        FileInputStream fos = new FileInputStream(new File(photoFileName));
    	
    	CommonsMultipartFile mockFile = createMock(CommonsMultipartFile.class);
    	expect(mockFile.getOriginalFilename()).andReturn("anacatanchin.jpg");
    	expect(mockFile.getInputStream()).andReturn(fos);
    	expect(mockFile.getContentType()).andReturn("image/jpg");
    	expect(mockFile.getSize()).andReturn(666l);
    	expect(mockFile.isEmpty()).andReturn(false);
    	replay(mockFile);
    	
    	lawyerService.uploadPhoto(mockFile, user, LawyerService.PROFILE_PHOTO);
    	
    	Lawyer result = lawyerDAO.get(lawyer.getId());
    	Assert.assertNotNull(result.getProfilePhoto());
    	Assert.assertNotNull(result.getProfilePhoto().getId());
    	
    	fos.close();
    	fos = new FileInputStream(new File(photoFileName));
    	
    	CommonsMultipartFile mockFile2 = createMock(CommonsMultipartFile.class);
    	expect(mockFile2.getOriginalFilename()).andReturn("newname.jpg");
    	expect(mockFile2.getInputStream()).andReturn(fos);
    	expect(mockFile2.getContentType()).andReturn("image/jpg");
    	expect(mockFile2.getSize()).andReturn(666l);
    	expect(mockFile2.isEmpty()).andReturn(false);
    	replay(mockFile2);
    	
    	lawyerService.uploadPhoto(mockFile2, user, LawyerService.PROFILE_PHOTO);
    	
    	result = lawyerDAO.get(lawyer.getId());
    	Assert.assertNotNull(result.getProfilePhoto());
    	Assert.assertEquals(result.getProfilePhoto().getName(),"newname.jpg");
    	
    	List<Photo> photos = photoDAO.getAll();
    	Assert.assertEquals(photos.size(),1);
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	photoDAO.deleteAll();
    	photoDataDAO.deleteAll();
    }
    
    @Test
    public void testSearchPoints()
    {
    	for( int i=0; i < 10; ++i )
    	{
    		Lawyer lawyer = Generator.lawyer();
    		lawyer.setPoints(i);
    		lawyerDAO.put(lawyer);
    	}
    	
    	List<Lawyer> results = lawyerService.search(5, 3);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getPoints().intValue(),4);
    	Assert.assertEquals(results.get(1).getPoints().intValue(),3);
    	Assert.assertEquals(results.get(2).getPoints().intValue(),2);
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void updateNewAreaOfPractise() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Lawyer newLawyer = new Lawyer();
    	newLawyer.setUser(user);
    	newLawyer.setUsername("updated");
    	newLawyer.setId(lawyer.getId());
    	newLawyer.setName("new name");
    	newLawyer.setPractitionerNumber("234234");
    	
    	AreaOfPractise area = Generator.areaOfPractise();
    	Set<AreaOfPractise> areas = new HashSet<AreaOfPractise>();
    	areas.add(area);
    	newLawyer.setAreasOfPractise(areas);
    	
    	lawyerService.update(newLawyer);
    	
    	Lawyer result = lawyerDAO.get(newLawyer.getId());
    	Assert.assertEquals(result.getUsername(),"updated");
    	Assert.assertEquals(result.getAreasOfPractise().size(),1);
    	
    	lawyer.setAreasOfPractise(new HashSet<AreaOfPractise>());
    	lawyerDAO.update(lawyer);
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	areaOfPractiseDAO.deleteAll();
    }
    
    @Test
    public void createLawyerFromUserHaveAlready() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Lawyer newLawyer = lawyerService.createLawyerFromUser(user);
    	Assert.assertNotNull(newLawyer.getId());
    	Assert.assertEquals(newLawyer.getId(),lawyer.getId());
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void createLawyerFromUserNew() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer newLawyer = lawyerService.createLawyerFromUser(user);
    	Assert.assertNotNull(newLawyer.getId());
    	Assert.assertNotNull(newLawyer.getId());
    	Assert.assertNotNull(newLawyer.getUuid());
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

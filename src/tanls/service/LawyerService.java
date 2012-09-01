package tanls.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import tanls.database.AreaOfPractiseDAO;
import tanls.database.LawyerDAO;
import tanls.database.PhotoDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.AreaOfPractise;
import tanls.entity.Lawyer;
import tanls.entity.Photo;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.util.WebUtil;

@Service
public class LawyerService 
{
	public static final String BACKGROUND_PHOTO = "BACKGROUND_PHOTO";
	public static final String PROFILE_PHOTO = "PROFILE_PHOTO";
	
	@Autowired LawyerDAO lawyerDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired PhotoService photoService;
    @Autowired PhotoDAO photoDAO;
    @Autowired AreaOfPractiseDAO areaOfPractiseDAO;
    
    public Lawyer getLawyerFromUser(TanlsUser user) throws InvalidInputException 
    {
    	if( !user.getLawyer() ) throw new InvalidInputException("You must be signed up as a lawyer to edit your profile, please ask eden@easylaw.com.au");
    	return tanlsUserDAO.getLawyer(user);
    }
    
    public Lawyer createLawyerFromUser(TanlsUser user)
    {
    	Lawyer lawyer = tanlsUserDAO.getLawyer(user);
    	if( lawyer == null ) 
    	{
    		lawyer = new Lawyer();
    		lawyer.setTimeAdmitted(Calendar.getInstance().getTimeInMillis());
    		lawyer.setUser(user);
    		lawyer.setCreatedTime(Calendar.getInstance().getTimeInMillis());
    		lawyer.setQuestionNotifications(true);
    		lawyer.setUuid(UUID.randomUUID().toString());
    		lawyerDAO.put(lawyer);
    		setUsername(lawyer);
    	}
    	return lawyer;
    }
    
    @Transactional(isolation=Isolation.SERIALIZABLE)
    public void setUsername(Lawyer lawyer)
    {
    	String username = getSuggestedUsername(lawyer);
    	if( username == null || WebUtil.isANumber(username) )
    	{
    		username = "lawyer" + lawyer.getId().toString();
    	}
    	else if( lawyerDAO.getByUsername(username) != null )
    	{
    		username += lawyer.getId().toString();
    	}
    	lawyer.setUsername(username);
    	lawyerDAO.update(lawyer);
    }
    
    public String getSuggestedUsername(Lawyer lawyer)
    {
    	String email = lawyer.getUser().getEmail();
    	String[] splitEmail = email.split("@");
    	if( splitEmail.length == 2 ) return splitEmail[0].trim().toLowerCase();
    	else return null;
    }
    
    public Lawyer getByUsername(String username)
    {
    	return lawyerDAO.getByUsername(username);
    }
    
    public void update(Lawyer lawyer) throws InvalidInputException
    {
    	Lawyer result = tanlsUserDAO.getLawyer(lawyer.getUser());
    	if( !result.getId().equals(lawyer.getId()) ) throw new InvalidInputException("You cannot update the profile of another lawyer");
    	validate(lawyer);
    	if( lawyer.getBackgroundPhoto() != null &&  lawyer.getBackgroundPhoto().getId() != null ) 
    	{
    		lawyer.setBackgroundPhoto(photoDAO.getWithData(lawyer.getBackgroundPhoto().getId()).copy(lawyer.getBackgroundPhoto()));
    	}
    	else if (lawyer.getBackgroundPhoto() != null &&  lawyer.getBackgroundPhoto().getId() == null)
    	{
    		lawyer.setBackgroundPhoto(null);
    	}
    	if( lawyer.getProfilePhoto() != null  &&  lawyer.getProfilePhoto().getId() != null  ) 
    	{
    		Integer id = lawyer.getProfilePhoto().getId();
    		Photo oldPhoto = photoDAO.getWithData(id);
    		if(oldPhoto != null)
    		{
    		    Photo copy = oldPhoto.copy(lawyer.getProfilePhoto());
    		    lawyer.setProfilePhoto(copy);
    		}
    	}
    	else if (lawyer.getProfilePhoto() != null &&  lawyer.getProfilePhoto().getId() == null)
    	{
    		lawyer.setProfilePhoto(null);
    	}
    	if( lawyer.getAreasOfPractise() != null ) 
    	{
    		for( AreaOfPractise area : lawyer.getAreasOfPractise() )
    		{
    			if( area.getId() == null ) areaOfPractiseDAO.put(area);
    		}
    	}
    	lawyerDAO.update(lawyer);
    }
    
    public void validate(Lawyer lawyer) throws InvalidInputException
    {
    	boolean skipUsernameCheck = false;
    	if( lawyer.getId() != null ) 
    	{
    		Lawyer existing = lawyerDAO.get(lawyer.getId());
    		if( existing.getUsername().equals(lawyer.getUsername()) ) skipUsernameCheck = true;
    	}
    	if( !skipUsernameCheck )
    	{
    		String username = lawyer.getUsername();
    		if( username == null || username.trim().length() <= 0 ) throw new InvalidInputException("Username is required");
    		if( WebUtil.isANumber(username) ) throw new InvalidInputException("Your username must contain at least one letter");
    		if( getByUsername(username) != null ) throw new InvalidInputException("The username " + username + " is already taken");
    		if( !lawyer.getUsername().matches("^[a-zA-Z0-9 _-]+$") ) throw new InvalidInputException("Your username can only be alphanumeric characters, _ - or spaces");
    	}
    	if( lawyer.getName() == null || lawyer.getName().trim().length() <= 0) throw new InvalidInputException("Please provide a name");
    	if( !lawyer.getName().matches("^[a-zA-Z0-9 _-]+$") ) throw new InvalidInputException("Your name can only be alphanumeric characters, _ - or spaces");
    	if( lawyer.getPractitionerNumber() == null || lawyer.getPractitionerNumber().trim().length() <= 0) throw new InvalidInputException("Please provide your practitioner number");
    	if( !lawyer.getPractitionerNumber().matches("^[a-zA-Z0-9 _-]+$") ) throw new InvalidInputException("Your name can only be alphanumeric characters, _ - or spaces");
        if( lawyer.getState() != null && !lawyer.getState().matches("NSW|VIC|QLD|SA|WA|NT|TAS")) throw new InvalidInputException("Invalid state");

        notTooLong(lawyer.getName(),40);
        notTooLong(lawyer.getUsername(),40);
        notTooLong(lawyer.getPractitionerNumber(),50);
        notTooLong(lawyer.getUrl(),200);
        notTooLong(lawyer.getFirmName(),100);
        notTooLong(lawyer.getFirmUrl(),200);
        notTooLong(lawyer.getSummary(),4000);
        notTooLong(lawyer.getFacebook(),200);
        notTooLong(lawyer.getTwitter(),200);
        notTooLong(lawyer.getLinkedin(),200);
        notTooLong(lawyer.getAbn(),200);
        notTooLong(lawyer.getAccountNumber(),200);
        notTooLong(lawyer.getBsb(),200);
    }
    
    public void notTooLong(String value, int max) throws InvalidInputException 
    {
    	if( value != null && value.length() > max ) throw new InvalidInputException("Too many characters");
    }

	public void uploadPhoto(MultipartFile file, TanlsUser user, String type) throws InvalidInputException, IOException 
	{
		Lawyer lawyer = tanlsUserDAO.getLawyer(user);
		if( lawyer == null ) throw new InvalidInputException("Could not locate your profile");
		
		if( file != null && !file.isEmpty() && file.getSize() > 0  )
	    {
		    Photo photo = photoService.create(file.getInputStream());
		    if( photo != null )
		    {
			    //photoService.resize(photo, photoWidth, photoHeight, Photo.JPG);
		    	photo.setName(file.getOriginalFilename());
			    if( type.equals(BACKGROUND_PHOTO) ) updateBackgroundPhoto(lawyer,photo);
			    else if( type.equals(PROFILE_PHOTO) ) updateProfilePhoto(lawyer, photo);
			    else throw new InvalidInputException("Invalid photo type " + type);
		    }
	    }
	}
	
	public void updateBackgroundPhoto(Lawyer lawyer, Photo photo) throws InvalidInputException, IOException 
	{
		if( photo != null )
		{
			photoService.constrainWidth(photo, 1300, Photo.PNG);
			
			Photo oldPhoto = lawyer.getBackgroundPhoto();
		    if( oldPhoto != null )
		    {
		        lawyer.setBackgroundPhoto(null);
		    	lawyerDAO.update(lawyer);
		    	photoDAO.delete(oldPhoto.getId());
		    }
		    lawyer.setBackgroundPhoto(photo);
		    lawyerDAO.update(lawyer);
		}
	}
	
	public void updateProfilePhoto(Lawyer lawyer, Photo photo) throws InvalidInputException, IOException 
	{
		if( photo != null )
		{
			photoService.constrainHeight(photo, 160, Photo.PNG);
			
			Photo oldPhoto = lawyer.getProfilePhoto();
		    if( oldPhoto != null )
		    {
		        lawyer.setProfilePhoto(null);
		    	lawyerDAO.update(lawyer);
		    	photoDAO.delete(oldPhoto.getId());
		    }
		    lawyer.setProfilePhoto(photo);
		    lawyerDAO.update(lawyer);
		}
	}
	
	public List<Lawyer> search(Integer offset, Integer limit)
	{
		return lawyerDAO.search(offset, limit);
	}
   
}

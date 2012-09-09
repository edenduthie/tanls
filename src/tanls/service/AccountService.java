package tanls.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import tanls.database.TanlsGrantedAuthorityDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

@Service
@Transactional
public class AccountService 
{
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired TanlsGrantedAuthorityDAO authDAO;
    @Autowired LawyerService lawyerService;
    
    @Transactional(isolation=Isolation.SERIALIZABLE, rollbackFor={InvalidInputException.class})
    public TanlsUser createAccount(String email, String password, String repeatPassword, String ip, Boolean asLawyer) throws InvalidInputException
    {
    	if( email == null ) throw new InvalidInputException("Please enter an email address");
    	if( email.length() < 6 || !email.contains("@") ) throw new InvalidInputException("Email address is not valid");
    	
    	TanlsUser existingUser = tanlsUserDAO.get(email);
    	if( existingUser != null ) throw new InvalidInputException("An account already exists with the email: " + email);
    	
    	if( password == null ) throw new InvalidInputException("Please provide a password");
    	if( password.length() < 6 ) throw new InvalidInputException("Password must be at least 6 characters");
    	if( repeatPassword == null ) throw new InvalidInputException("Please repeat the password");
    	password = password.trim();
    	repeatPassword = repeatPassword.trim();
    	
    	if( !password.equals(repeatPassword) ) throw new InvalidInputException("Passwords do not match");
    	
    	TanlsUser user = null;
    	user = new TanlsUser();
    	
    	user.setEmail(email);
    	user.setPassword(encryptPassword(password));
    	
    	//TanlsGrantedAuthority auth = authDAO.getRoleUser();
    	user.add(authDAO.getRoleUser());
    	
    	if( asLawyer != null && asLawyer )
    	{
    		user.add(authDAO.getRolePractitioner());
    	}
    	
    	user.setIp(ip);
    	user.setSignupTime(Calendar.getInstance().getTimeInMillis());
    	user.setLawyer(asLawyer);
    	
    	tanlsUserDAO.put(user);
    	
    	if( asLawyer != null && asLawyer )
    	{
    		lawyerService.createLawyerFromUser(user);
    	}
    	
    	return user;
    }
    
    @Transactional(isolation=Isolation.SERIALIZABLE, rollbackFor={InvalidInputException.class})
    public TanlsUser update(TanlsUser user, String email, String password, String repeatPassword) throws InvalidInputException
    {
    	if( user == null ) throw new InvalidInputException("You must be logged in to change your details");
    	
    	boolean haveChanged = false;
    	
    	if( email != null && !email.equals(user.getEmail()) && email.length() > 0 )
    	{
    		TanlsUser existingUser = tanlsUserDAO.get(email);
    		if( existingUser != null ) throw new InvalidInputException("An account already exists with the email: " + email);
    		user.setEmail(email);
    		haveChanged = true;
    	}
    	if( password != null && password.length() > 0 ) // changing the password
    	{
    		if( password.length() < 6 ) throw new InvalidInputException("Password must be at least 6 characters");
        	if( repeatPassword == null ) throw new InvalidInputException("Please repeat the password");
        	password = password.trim();
        	repeatPassword = repeatPassword.trim();
        	if( !password.equals(repeatPassword) ) throw new InvalidInputException("Passwords do not match");
        	user.setPassword(encryptPassword(password));
        	haveChanged = true;
    	}
    	if( haveChanged )
    	{
    		tanlsUserDAO.update(user);
    	}
    	
    	return user;
    }
    
    public String encryptPassword(String password)
	{
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		encoder.setEncodeHashAsBase64(false);
		return  encoder.encodePassword(password,null);	
	}
    
    public TanlsUser get(String email)
    {
    	return tanlsUserDAO.get(email);
    }
    
    public TanlsUser get(Integer id)
    {
    	return tanlsUserDAO.get(id);
    }
}

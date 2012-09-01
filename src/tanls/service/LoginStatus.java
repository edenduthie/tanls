package tanls.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import tanls.entity.TanlsGrantedAuthority;
import tanls.entity.TanlsUser;

@Service
public class LoginStatus 
{
	
	public boolean isLoggedInService()
	{
		return isLoggedIn();
	}
	
	public TanlsUser getUserService()
	{
		return getUser();
	}
	
    public static boolean isLoggedIn()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean loggedIn = false;
        if( auth == null ) return false;
        for (GrantedAuthority authority : auth.getAuthorities()) 
        {
        	if(authority.getAuthority().equals(TanlsGrantedAuthority.ROLE_USER)) 
        	    loggedIn = true;
        }
        return loggedIn;
    }
    
    public static boolean isAdmin()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean admin = false;
        for (GrantedAuthority authority : auth.getAuthorities()) 
        {
        	if(authority.getAuthority().equals(TanlsGrantedAuthority.ADMIN_ROLE)) 
        	    admin = true;
        }
        return admin;
    }
    
    public static TanlsUser getUser()
    {
    	Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if( o != null && o instanceof TanlsUser )
    	{
            TanlsUser user = (TanlsUser) o;
            return user;
    	}
    	else
    	{
    		return null;
    	}
    }
    
    public static int getUserId()
    {
    	if( getUser() != null ) return getUser().getId();
    	else return -1;
    }
    
    /**
     * Returns true if the given user id matches the current logged in user
     * @return
     */
    public static boolean isUser(Integer id)
    {
    	TanlsUser user = getUser();
    	if( user == null ) return false;
    	if( user.getId().equals(id) ) return true;
    	else return false;
    }
}
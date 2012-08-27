package tanls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.database.CustomerDAO;
import tanls.database.LawyerDAO;
import tanls.database.ProfileItemDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.exception.TanlsSecurityException;
import tanls.util.Parser;

@Service
public class ProfileItemService 
{
	public static final int POINTS_FOR_APPROVED_ARTICLE = 5;
	
	@Autowired ProfileItemDAO profileItemDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired LawyerDAO lawyerDAO;
	@Autowired CustomerService customerService;
	@Autowired CustomerDAO customerDAO;
	
	public ProfileItem get(Integer id)
	{
		return profileItemDAO.getWithLawyer(id);
	}
	
	public ProfileItem get(String title)
	{
		return profileItemDAO.getWithLawyer(title);
	}
	
    public void create(ProfileItem profileItem, TanlsUser user) throws TanlsSecurityException, InvalidInputException
    {
    	profileItem.setStatus(ProfileItem.NEW_STATUS);
    	allowedUpdate(profileItem,user);
    	checkTitle(profileItem);
    	profileItemDAO.put(profileItem);
    }
    
    public List<ProfileItem> search(Integer lawyerId, String type, Long lastTimelineTime, Integer limit, String status)
    {
    	return profileItemDAO.search(lawyerId, type, lastTimelineTime, limit, status);
    }
    
    public List<ProfileItem> searchNoText(Integer lawyerId, String type, Long lastTimelineTime, Integer limit, String status)
    {
    	return profileItemDAO.searchNoText(lawyerId, type, lastTimelineTime, limit, status);
    }
    
    public void update(ProfileItem profileItem, TanlsUser user) throws TanlsSecurityException, InvalidInputException
    {	
    	allowedUpdate(profileItem,user);
    	checkTitle(profileItem);
    	profileItemDAO.update(profileItem);
    }
    
    public void updateStatus(ProfileItem profileItem, TanlsUser user) throws TanlsSecurityException, InvalidInputException
    {	
    	ProfileItem existingItem = profileItemDAO.getWithLawyer(profileItem.getId());
    	allowedUpdateStatus(existingItem,profileItem,user);
    	if( existingItem.getStatus().equals(ProfileItem.NEW_STATUS) && profileItem.getStatus().equals(ProfileItem.APPROVED_STATUS) )
    	{
    		existingItem.getLawyer().setPoints(existingItem.getLawyer().getPoints()+POINTS_FOR_APPROVED_ARTICLE);
    		lawyerDAO.update(existingItem.getLawyer());
    		
    		// TODO posible inconsistency here but very unlikely
    	}
    	existingItem.setStatus(profileItem.getStatus());
    	profileItemDAO.update(existingItem);
    }
    
    public void delete(ProfileItem profileItem, TanlsUser user) throws TanlsSecurityException, InvalidInputException
    {
    	if( profileItem.getType().equals(ProfileItem.FEEDBACK) ) throw new InvalidInputException("You cannot delete feedback items");
    	allowedUpdate(profileItem,user);
    	profileItemDAO.delete(profileItem);
    }
    
    public void allowedUpdate(ProfileItem profileItem, TanlsUser user) throws TanlsSecurityException, InvalidInputException
    {
    	if( user == null ) throw new TanlsSecurityException("You must be logged in to use this function",true);
    	if( profileItem == null || profileItem.getLawyer() == null || profileItem.getLawyer().getId() == null ) 
            throw new InvalidInputException("The provided profile item is missing a lawyer");
    	
    	ProfileItem existingItem = profileItemDAO.getWithLawyer(profileItem.getId());
    	if( existingItem != null ) profileItem.setStatus(existingItem.getStatus()); // don't allow status updates
    	
    	Lawyer retrievedLawyer = tanlsUserDAO.getLawyer(user);
    	if( retrievedLawyer == null && user.isAdmin() && existingItem != null )
    	{
    		retrievedLawyer = existingItem.getLawyer();
    	}
    	if( retrievedLawyer == null || (!retrievedLawyer.getId().equals(profileItem.getLawyer().getId()) && !user.isAdmin() ) )
    	{
    	    throw new InvalidInputException("You do not have permission to add/edit/remove items from this lawyer's profile");
    	}
    	profileItem.setLawyer(retrievedLawyer);
    }
   
    
    public void allowedUpdateStatus(ProfileItem existingItem, ProfileItem profileItem, TanlsUser user) throws TanlsSecurityException, InvalidInputException
    {
    	if( user == null ) throw new TanlsSecurityException("You must be logged in to use this function",true);
		if( !user.isAdmin() && profileItem.getStatus() != null && !existingItem.getStatus().equals(profileItem.getStatus()) )
		{
		    throw new TanlsSecurityException("You do not have admin permissions to approve or reject articles", false);
		}
    }
    
    public void checkTitle(ProfileItem newProfileItem) throws InvalidInputException
    {
    	newProfileItem.setTitle(Parser.removeNonAlphaSpaceOrDash(newProfileItem.getTitle()));
    	if( newProfileItem.getType().equals(ProfileItem.ARTICLE) ) {
	    	ProfileItem existingProfileItem = profileItemDAO.getWithLawyer(newProfileItem.getTitle());
	    	if( existingProfileItem != null && !existingProfileItem.getId().equals(newProfileItem.getId()) )
	    	{
	    		throw new InvalidInputException("An article already exists with the title: " + newProfileItem.getTitle());
	    	}
    	}
    }
}

package tanls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.database.AreaOfPractiseDAO;
import tanls.database.CustomerDAO;
import tanls.database.LawyerDAO;
import tanls.database.ProfileItemDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

@Service
public class RecommendationService 
{
	@Autowired CustomerService customerService;
	@Autowired CustomerDAO customerDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired ProfileItemDAO profileItemDAO;
	@Autowired AreaOfPractiseDAO areaOfPractiseDAO;
	@Autowired LawyerDAO lawyerDAO;
	
    public ProfileItem create(TanlsUser user, ProfileItem profileItem) throws InvalidInputException
    {
		Customer customer = tanlsUserDAO.getCustomer(user);
		if( customer == null)
		{
			customer = profileItem.getCustomer();
			customer.setUser(user);
			customerService.put(customer);
		}
		else
		{
			customerService.update(customer, profileItem.getCustomer());
		}
		profileItem.setCustomer(customer);
		
		// set area of practice
		if( profileItem.getAreaOfPractise() != null && profileItem.getAreaOfPractise().getId() != null )
		{
			profileItem.setAreaOfPractise(areaOfPractiseDAO.get(profileItem.getAreaOfPractise().getId()));
		}
		
		// check profile item has a lawyer and set it
		if( profileItem.getLawyer() == null || profileItem.getLawyer().getId() == null ) throw new InvalidInputException("Please profile a lawyer to recommend");
		profileItem.setLawyer(lawyerDAO.get(profileItem.getLawyer().getId()));
		
		// check no other profile items for customer / lawyer combination
		List<ProfileItem> existingItems = profileItemDAO.getTypeByCustomerLawyer(profileItem.getCustomer(),profileItem.getLawyer(),profileItem.RECOMMENDATION);
		if( existingItems.size() > 0 ) throw new InvalidInputException("You have already provided a recommendation for this lawyer");
		
		// check not the actual lawyer
		Lawyer myLawyerProfile = tanlsUserDAO.getLawyer(user);
		if( myLawyerProfile != null && 
			myLawyerProfile.getId().equals(profileItem.getLawyer().getId()) ) throw new InvalidInputException("You cannot recommend yourself!");
		
		profileItemDAO.put(profileItem);
		
		return profileItem;
    }
    
    public ProfileItem edit(TanlsUser user, ProfileItem profileItem) throws InvalidInputException
    {
    	Customer customer = tanlsUserDAO.getCustomer(user);
    	if( customer == null ) throw new InvalidInputException("Your customer record could not be found");
    	ProfileItem existingItem = profileItemDAO.getWithLawyer(profileItem.getId());
    	if( existingItem == null ) throw new InvalidInputException("Could not find profile item to edit");
    	if( !existingItem.getType().equals(ProfileItem.RECOMMENDATION) ) throw new InvalidInputException("You only have permissions to edit recommendations");
    	if( !existingItem.getCustomer().getId().equals(customer.getId()) ) throw new InvalidInputException("You can only edit recommendations you have written");
		if( !existingItem.getText().equals(profileItem.getText()) )
		{
			existingItem.setText(profileItem.getText());
			profileItemDAO.update(existingItem);
		}
		return existingItem;
    }
    
    public void delete(TanlsUser user, ProfileItem profileItem) throws InvalidInputException
    {
    	ProfileItem existingItem = profileItemDAO.getWithLawyer(profileItem.getId());
    	if( existingItem != null )
    	{
    		boolean permitted = false;
    		Lawyer lawyer = tanlsUserDAO.getLawyer(user);
    		if( lawyer != null && lawyer.getId().equals(existingItem.getLawyer().getId()) ) permitted = true;
    		else
    		{
    			Customer customer = tanlsUserDAO.getCustomer(user);
    			if( customer != null && customer.getId().equals(existingItem.getCustomer().getId())) permitted = true;
    		}
    		if( permitted )
    		{
    			profileItemDAO.delete(profileItem);
    		}
    		else throw new InvalidInputException("You are not permitted to delete this item");
    	}
    }
}

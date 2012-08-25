package tanls.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tanls.database.AddressDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Address;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

@Service(value="tanlsUserDetailsService")
public class TanlsUserDetailsService implements UserDetailsService
{
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired AddressDAO addressDAO;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException 
	{
		TanlsUser user = tanlsUserDAO.get(username);
		if( user == null ) throw new UsernameNotFoundException("No user was found with the email: " + username);
		else return user;
	}
	
//	public TanlsUser createOrUpdateAddress(TanlsUser user, Address newAddress) throws InvalidInputException
//	{
//        newAddress.validate();
//        user.setName(newAddress.getFirstName() + " " + newAddress.getLastName());
//        Address currentAddress = user.getHomeAddress();
//        if( currentAddress != null )
//        {
//        	currentAddress.update(newAddress);
//        	addressDAO.update(currentAddress);
//        }
//        else
//        {
//        	addressDAO.put(newAddress);
//        	user.setHomeAddress(newAddress);
//        }
//        tanlsUserDAO.update(user);
//        return user;
//	}

	public Object get(Integer id) 
	{
		return tanlsUserDAO.get(id);
	}

	public Customer getCustomer(TanlsUser user) 
	{
		return tanlsUserDAO.getCustomer(user);
	}

}

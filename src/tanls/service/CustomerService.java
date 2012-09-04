package tanls.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.database.CustomerDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Customer;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

@Service
public class CustomerService 
{
	@Autowired CustomerDAO customerDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	
    public Customer update(Customer existingCustomer, Customer newCustomer) throws InvalidInputException
    {
    	newCustomer.validate();
    	String[] skip = {"id","user","billingAddress","numberOfQuestionsAsked","creditCardToken","customerId"};
        BeanUtils.copyProperties(newCustomer,existingCustomer,skip);
        customerDAO.update(existingCustomer);
        return existingCustomer;
    }
    
    public Customer put(Customer newCustomer) throws InvalidInputException
    {
		newCustomer.setEmailNotifications(true);
		newCustomer.setNumberOfQuestionsAsked(1);
    	newCustomer.validate();
    	customerDAO.put(newCustomer);
    	return newCustomer;
    }
    
    public Customer updateFromUser(TanlsUser user, Customer newCustomer) throws InvalidInputException
    {
    	Customer existingCustomer = tanlsUserDAO.getCustomer(user);
    	if( existingCustomer  == null ) throw new InvalidInputException("Sorry, you don't currently have a customer record, please contact eden@easylaw.com.au");
    	update(existingCustomer,newCustomer);
    	return existingCustomer;
    }
}

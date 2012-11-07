package tanls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tanls.dto.CustomerDTO;
import tanls.entity.Customer;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.CustomerService;
import tanls.service.LoginStatus;
import tanls.service.TanlsUserDetailsService;

@Controller
public class CustomerController 
{
	@Autowired LoginStatus loginStatus;
	@Autowired TanlsUserDetailsService tanlsUserDetailsService;
	@Autowired CustomerService customerService;
	
	@RequestMapping(value="/customer", method = RequestMethod.GET)
	public @ResponseBody CustomerDTO get() 
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new CustomerDTO("Please log in.",false,true);
		Customer customer = tanlsUserDetailsService.getCustomer(user);
		if( customer != null ) return new CustomerDTO(customer);
		else return new CustomerDTO("No customer found",false);
	}
	
	@RequestMapping(value="/customer", method = RequestMethod.PUT)
	public @ResponseBody CustomerDTO update(@RequestBody CustomerDTO customer) 
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new CustomerDTO("Please log in.",false,true);
		if( customer == null ) return new CustomerDTO("No customer supplied.",false);
		try 
		{
			Customer result = customerService.updateFromUser(user, new Customer(customer));
			return new CustomerDTO(result);
		} 
		catch (InvalidInputException e) 
		{
			return new CustomerDTO(e.getMessage(),false);
		}
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}
}

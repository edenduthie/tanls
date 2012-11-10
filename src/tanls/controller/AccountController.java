package tanls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tanls.dto.TanlsUserDTO;
import tanls.dto.UpdateEmailPasswordDTO;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.AccountService;
import tanls.service.LoginStatus;

@Controller
public class AccountController 
{
    @Autowired AccountService accountService;
    @Autowired LoginStatus loginStatus;
    
    @RequestMapping(value="/account", method = RequestMethod.PUT)
    public @ResponseBody TanlsUserDTO updateEmailPassword(@RequestBody UpdateEmailPasswordDTO request)
    {
    	TanlsUser user = loginStatus.getUserService();
    	if( user == null ) return new TanlsUserDTO("Please log in.",false);
    	try 
    	{
			user = accountService.update(user, request.getEmail(), request.getPassword(), request.getRepeat());
			return new TanlsUserDTO(user);
		} 
    	catch (InvalidInputException e) 
    	{
			return new TanlsUserDTO(e.getMessage(),false);
		}
    }

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}
}

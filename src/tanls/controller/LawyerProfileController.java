package tanls.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tanls.dto.LawyerDTO;
import tanls.dto.ResponseDTO;
import tanls.entity.Lawyer;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.LawyerService;
import tanls.service.LoginStatus;

@Controller
public class LawyerProfileController 
{
	@Autowired LoginStatus loginStatus;
	@Autowired LawyerService lawyerService;
	
	public static final Logger log = Logger.getLogger(LawyerProfileController.class);
	
	@RequestMapping(value="/lawyer/profile", method = RequestMethod.GET)
	public @ResponseBody LawyerDTO profile() 
	{
		TanlsUser user = loginStatus.getUserService();
		if( user != null ) 
		{
			Lawyer lawyer;
			try 
			{
				lawyer = lawyerService.getLawyerFromUser(user);
			} 
			catch (InvalidInputException e) 
			{
				return new LawyerDTO(e.getMessage(),false);
			}
			if( lawyer != null ) return new LawyerDTO(lawyer);
		}
		return new LawyerDTO("Your lawyer profile could not be found",false);
	}
	
	@RequestMapping(value="/lawyer/profile", method = RequestMethod.PUT)
	public @ResponseBody LawyerDTO edit(@RequestBody LawyerDTO lawyerDTO) 
	{
		TanlsUser user = loginStatus.getUserService();
		if( user != null ) 
		{
			Lawyer lawyer = new Lawyer(lawyerDTO);
			lawyer.setUser(user);
			try
			{
			    lawyerService.update(lawyer);
			    return new LawyerDTO(lawyer);
			}
			catch(InvalidInputException e)
			{
				return new LawyerDTO(e.getMessage(),false);
			}
		}
		return new LawyerDTO("Please log in before editing.",false);
	}
	
	@RequestMapping(value="/lawyer/photo/background", method = RequestMethod.POST, produces="text/html")
	public @ResponseBody String uploadBackgroundPhoto(
			@RequestParam MultipartFile qqfile) 
	{
		ResponseDTO response = uploadPhoto(qqfile,LawyerService.BACKGROUND_PHOTO);
		if( response.isSuccess() ) return  "{\"message\":\"success\",\"success\":\"true\"}";
		else return "{\"message\":\""+response.getMessage()+"\",\"success\":\"false\"}";
	}
	
	@RequestMapping(value="/lawyer/photo/profile", method = RequestMethod.POST, produces="text/html")
	public @ResponseBody String uploadProfilePhoto(
			@RequestParam MultipartFile qqfile) 
	{
		ResponseDTO response = uploadPhoto(qqfile,LawyerService.PROFILE_PHOTO);
		if( response.isSuccess() ) return  "{\"message\":\"success\",\"success\":\"true\"}";
		else return "{\"message\":\""+response.getMessage()+"\",\"success\":\"false\"}";
	}
	
	public ResponseDTO uploadPhoto(MultipartFile qqfile, String type)
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new ResponseDTO("Please log in to upload files.",false);
		try 
		{
			lawyerService.uploadPhoto(qqfile,user,type);
		}
		catch (InvalidInputException e) 
		{
			return new ResponseDTO();
		} 
		catch (IOException e) 
		{
			return new ResponseDTO();
		}
		return new ResponseDTO();
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}
}

package tanls.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tanls.dto.FileDTO;
import tanls.dto.ProfileItemDTO;
import tanls.entity.File;
import tanls.entity.ProfileItem;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.exception.PaymentException;
import tanls.service.FileService;
import tanls.service.LoginStatus;

@Controller
public class FileController 
{
	public static final Logger log = Logger.getLogger(FileController.class);
	
	@Autowired LoginStatus loginStatus;
	@Autowired FileService fileService;
	
	@RequestMapping(value="/customer/book", method = RequestMethod.POST)
	public @ResponseBody FileDTO create(@RequestBody FileDTO fileDTO) 
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new FileDTO("Please log in",false);
		if( fileDTO != null ) 
		{
			try 
			{
				File file = new File(fileDTO);
				file = fileService.create(file, user);
				return new FileDTO(file);
			} 
			catch (PaymentException e) 
			{
				log.error(e);
				return new FileDTO(e.getMessage(),false);
			} 
			catch (InvalidInputException e) 
			{
				log.error(e);
				return new FileDTO(e.getMessage(), false);
			}
		}
		else
		{
			return new FileDTO("No file item provided to book",false);
		}
	}
	
	@RequestMapping(value="/customer/files", method = RequestMethod.GET)
	public @ResponseBody List<FileDTO> getCustomer(
	    @RequestParam(value="limit", required = false ) Integer limit,
	    @RequestParam(value="offset", required = false ) Integer offset)
	{
	    TanlsUser user = loginStatus.getUserService();
	    List<FileDTO> resultsDTO = new ArrayList<FileDTO>();
	    if(user == null) return resultsDTO;
	    List<File> results = fileService.getCustomer(limit, offset, user);
        for( File file : results ) resultsDTO.add(new FileDTO(file) );
        return resultsDTO;
	}
	
	@RequestMapping(value="/lawyer/files", method = RequestMethod.GET)
	public @ResponseBody List<FileDTO> getLawyer(
	    @RequestParam(value="limit", required = false ) Integer limit,
	    @RequestParam(value="offset", required = false ) Integer offset)
	{
	    TanlsUser user = loginStatus.getUserService();
	    List<FileDTO> resultsDTO = new ArrayList<FileDTO>();
	    if(user == null) return resultsDTO;
	    List<File> results = fileService.getLawyer(limit, offset, user);
        for( File file : results ) resultsDTO.add(new FileDTO(file) );
        return resultsDTO;	
	}
	
	@RequestMapping(value="/customer/files/{fileId}", method = RequestMethod.GET)
	public @ResponseBody FileDTO getCustomerSingle(@PathVariable Integer fileId)
	{
	    TanlsUser user = loginStatus.getUserService();
	    if(user == null) return null;
	    File result = fileService.getCustomerSingle(fileId, user);
        if( result != null ) return new FileDTO(result);
        else return null;
	}
	
	@RequestMapping(value="/lawyer/files/{fileId}", method = RequestMethod.GET)
	public @ResponseBody FileDTO getLawyerSingle(@PathVariable Integer fileId)
	{
	    TanlsUser user = loginStatus.getUserService();
	    if(user == null) return null;
	    File result = fileService.getLawyerSingle(fileId, user);
        if( result != null ) return new FileDTO(result);
        else return null;
	}
	
	@RequestMapping(value="/customer/files/{fileId}/feedback", method = RequestMethod.POST)
	public @ResponseBody ProfileItemDTO submitFeedback(@RequestBody ProfileItemDTO feedback,
	    @PathVariable Integer fileId)
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new ProfileItemDTO("Please log in",false);
		try 
		{
			ProfileItem feedbackResult = fileService.submitFeedback(fileId, new ProfileItem(feedback), user);
			return new ProfileItemDTO(feedbackResult);
		} 
		catch (InvalidInputException e) 
		{
			return new ProfileItemDTO(e.getMessage(),false);
		}
	}
	
	@RequestMapping(value="/lawyer/files/{fileId}/complete", method = RequestMethod.GET)
	public @ResponseBody FileDTO markComplete(@PathVariable Integer fileId)
	{

		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new FileDTO("Please log in",false);
		try 
		{
			
			File result = fileService.markAsComplete(fileId, user);
			if( result != null ) return new FileDTO(result);
			else return new FileDTO("Oops, there was an error. Please try again.",false);
		} 
		catch (InvalidInputException e) 
		{
			return new FileDTO(e.getMessage(),false);
		}
	}
 
	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}
}

package tanls.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import tanls.dto.ProfileItemDTO;
import tanls.dto.ProfileItemSearchResponse;
import tanls.dto.ResponseDTO;
import tanls.entity.ProfileItem;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.exception.TanlsSecurityException;
import tanls.service.LoginStatus;
import tanls.service.ProfileItemService;

@Controller
public class ProfileItemController 
{
	public static final Logger log = Logger.getLogger(ProfileItemController.class);
	
	@Autowired LoginStatus loginStatus;
	@Autowired ProfileItemService profileItemService;
	
	@RequestMapping(value="/items/{title}", method = RequestMethod.GET)
	public @ResponseBody ProfileItemDTO get(
	    @PathVariable String title
	) 
	{
		ProfileItem result = profileItemService.get(title);
	    if( result != null ) return new ProfileItemDTO(result);
	    else return new ProfileItemDTO("Could not find item: " + title,false);
	}
	
	@RequestMapping(value="/lawyer/{id}/items", method = RequestMethod.GET)
	public @ResponseBody ProfileItemSearchResponse items(
	    @PathVariable Integer id,
	    @RequestParam(value="type",required = false) String type,
	    @RequestParam(value="lastTimelineTime", required = false ) Long lastTimelineTime,
	    @RequestParam(value="limit", required = false ) Integer limit,
	    @RequestParam(value="status",required = false) String status
	) 
	{
		List<ProfileItem> profileItems = profileItemService.search(id, type, lastTimelineTime, limit, status);
		List<ProfileItemDTO> profileItemDTOs = new ArrayList<ProfileItemDTO>();
		for( ProfileItem profileItem : profileItems ) profileItemDTOs.add(new ProfileItemDTO(profileItem));
		return new ProfileItemSearchResponse(profileItemDTOs);
	
	}
	
	@RequestMapping(value="/items", method = RequestMethod.GET)
	public @ResponseBody ProfileItemSearchResponse itemsNoLawyer(
	    @RequestParam(value="type",required = false) String type,
	    @RequestParam(value="lastTimelineTime", required = false ) Long lastTimelineTime,
	    @RequestParam(value="limit", required = false ) Integer limit,
	    @RequestParam(value="status",required = false) String status
	) 
	{
		List<ProfileItem> profileItems = profileItemService.search(null, type, lastTimelineTime, limit, status);
		List<ProfileItemDTO> profileItemDTOs = new ArrayList<ProfileItemDTO>();
		for( ProfileItem profileItem : profileItems ) profileItemDTOs.add(new ProfileItemDTO(profileItem));
		return new ProfileItemSearchResponse(profileItemDTOs);
	
	}
	
	@RequestMapping(value="/itemsNoText", method = RequestMethod.GET)
	public @ResponseBody ProfileItemSearchResponse itemsNoText(
	    @RequestParam(value="type",required = false) String type,
	    @RequestParam(value="lastTimelineTime", required = false ) Long lastTimelineTime,
	    @RequestParam(value="limit", required = false ) Integer limit,
	    @RequestParam(value="status",required = false) String status
	) 
	{
		List<ProfileItem> profileItems = profileItemService.searchNoText(null, type, lastTimelineTime, limit, status);
		List<ProfileItemDTO> profileItemDTOs = new ArrayList<ProfileItemDTO>();
		for( ProfileItem profileItem : profileItems ) profileItemDTOs.add(new ProfileItemDTO(profileItem));
		return new ProfileItemSearchResponse(profileItemDTOs);
	
	}
	
	@RequestMapping(value="/lawyer/items", method = RequestMethod.POST)
	public @ResponseBody ProfileItemDTO create(
	    @RequestBody ProfileItemDTO profileItemDTO
	) 
	{
		TanlsUser user = loginStatus.getUserService();
		if( profileItemDTO != null ) 
		{
			try 
			{
				ProfileItem profileItem = new ProfileItem(profileItemDTO);
				profileItemService.create(profileItem, user);
				return new ProfileItemDTO(profileItem);
			} 
			catch (TanlsSecurityException e) 
			{
				log.error(e);
				return new ProfileItemDTO(e.getMessage(),false,e.isRedirectToLogin());
			} 
			catch (InvalidInputException e) 
			{
				log.error(e);
				return new ProfileItemDTO(e.getMessage(), false);
			}
		}
		else
		{
			return new ProfileItemDTO("No profile item supplied",false);
		}
	}
	
	@RequestMapping(value="/lawyer/items", method = RequestMethod.PUT)
	public @ResponseBody ProfileItemDTO update(
	    @RequestBody ProfileItemDTO profileItemDTO
	) 
	{
		TanlsUser user = loginStatus.getUserService();
		if( profileItemDTO != null ) 
		{
			try 
			{
				ProfileItem profileItem = new ProfileItem(profileItemDTO);
				profileItemService.update(profileItem, user);
				return new ProfileItemDTO(profileItem);
			} 
			catch (TanlsSecurityException e) 
			{
				log.error(e);
				return new ProfileItemDTO(e.getMessage(),false,e.isRedirectToLogin());
			} 
			catch (InvalidInputException e) 
			{
				log.error(e);
				return new ProfileItemDTO(e.getMessage(), false);
			}
		}
		else
		{
			return new ProfileItemDTO("No profile item supplied",false);
		}
	}
	
	@RequestMapping(value="/lawyer/items/status", method = RequestMethod.PUT)
	public @ResponseBody ProfileItemDTO updateStatus(
	    @RequestBody ProfileItemDTO profileItemDTO
	) 
	{
		TanlsUser user = loginStatus.getUserService();
		if( profileItemDTO != null ) 
		{
			try 
			{
				ProfileItem profileItem = new ProfileItem(profileItemDTO);
				profileItemService.updateStatus(profileItem, user);
				return new ProfileItemDTO(profileItem);
			} 
			catch (TanlsSecurityException e) 
			{
				log.error(e);
				return new ProfileItemDTO(e.getMessage(),false,e.isRedirectToLogin());
			} 
			catch (InvalidInputException e) 
			{
				log.error(e);
				return new ProfileItemDTO(e.getMessage(), false);
			}
		}
		else
		{
			return new ProfileItemDTO("No profile item supplied",false);
		}
	}
	
	@RequestMapping(value="/lawyer/items/delete", method = RequestMethod.POST)
	public @ResponseBody ResponseDTO delete(@RequestBody ProfileItemDTO profileItemDTO) 
	{
		TanlsUser user = loginStatus.getUserService();
		if( profileItemDTO != null ) 
		{
			try 
			{
				ProfileItem profileItem = new ProfileItem(profileItemDTO);
				profileItemService.delete(profileItem, user);
				return new ResponseDTO();
			} 
			catch (TanlsSecurityException e) 
			{
				log.error(e);
				return new ResponseDTO(e.getMessage(),false,e.isRedirectToLogin());
			} 
			catch (InvalidInputException e) 
			{
				log.error(e);
				return new ResponseDTO(e.getMessage(), false);
			}
		}
		else
		{
			return new ResponseDTO("No profile item supplied",false);
		}
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	} 
}

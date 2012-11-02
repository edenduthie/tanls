package tanls.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tanls.dto.LawyerDTO;
import tanls.dto.LawyersSearchResponse;
import tanls.entity.Lawyer;
import tanls.service.LawyerService;

@Controller
public class LawyersController 
{
	@Autowired LawyerService lawyerService;

	@RequestMapping(value="/lawyers/{username}", method = RequestMethod.GET)
	public @ResponseBody LawyerDTO lawyer(@PathVariable String username)
	{
		Lawyer lawyer = lawyerService.getByUsername(username);
		if( lawyer == null ) return new LawyerDTO("Could not find the lawyer: " + username,false);
		else return new LawyerDTO(lawyer);
	}
	
	@RequestMapping(value="/lawyers", method = RequestMethod.GET)
	public @ResponseBody LawyersSearchResponse search(@RequestParam(value="offset",required=false) Integer offset, @RequestParam(value="limit",required=false) Integer limit  )
	{
		List<Lawyer> lawyers = lawyerService.search(offset, limit);
		return new LawyersSearchResponse(lawyers);
	}
}

package tanls.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tanls.database.SuburbDAO;
import tanls.dto.AreaOfPractiseDTO;
import tanls.entity.AreaOfPractise;
import tanls.service.AreaOfPractiseService;

@Controller
public class AutocompleteController 
{
	@Autowired SuburbDAO suburbDAO;
	@Autowired AreaOfPractiseService areaOfPractiseService;
	
	@RequestMapping(value="/autocomplete/suburb", method = RequestMethod.GET)
	public @ResponseBody List<String> suburbs(@RequestParam("query") String query) 
	{
		return suburbDAO.searchByName(query);
	}
	
	@RequestMapping(value="/autocomplete/areasofpractise", method = RequestMethod.GET)
	public @ResponseBody List<AreaOfPractiseDTO> areasofpractise() 
	{
		List<AreaOfPractise> areas = areaOfPractiseService.getAll();
		List<AreaOfPractiseDTO> dtos = new ArrayList<AreaOfPractiseDTO>();
		for( AreaOfPractise area : areas ) dtos.add(new AreaOfPractiseDTO(area) );
		return dtos;
	}
}

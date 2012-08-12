package tanls.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AreaOfPractiseDAO;
import tanls.database.SuburbDAO;
import tanls.dto.AreaOfPractiseDTO;
import tanls.entity.AreaOfPractise;
import tanls.entity.Suburb;

public class AutocompleteControllerTest extends BaseTest 
{
    @Autowired SuburbDAO suburbDAO;
    @Autowired AutocompleteController contorller;
    @Autowired AreaOfPractiseDAO areaDAO;
    
    @Test
    public void testSuburb()
    {
    	Suburb suburb = Generator.suburb();
    	suburbDAO.put(suburb);
    	suburb = Generator.suburb();
    	suburb.setName("KHEW");
    	suburbDAO.put(suburb);
    	
    	List<String> results = contorller.suburbs("k");
    	Assert.assertEquals(results.size(),2);
    	
    	suburbDAO.deleteAll();
    }
    
    @Test
    public void testAreas()
    {
    	AreaOfPractise area = Generator.areaOfPractise();
    	areaDAO.put(area);
    	
    	List<AreaOfPractiseDTO> results = contorller.areasofpractise();
    	Assert.assertEquals(results.size(),1);
    	Assert.assertEquals(results.get(0).getId(),area.getId());
    	
    	areaDAO.deleteAll();
    }
}

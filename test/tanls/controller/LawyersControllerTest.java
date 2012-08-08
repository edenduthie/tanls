package tanls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.LawyerDAO;
import tanls.dto.LawyerDTO;
import tanls.dto.LawyersSearchResponse;
import tanls.entity.Lawyer;

public class LawyersControllerTest extends BaseTest
{
	@Autowired LawyerDAO lawyerDAO;
	@Autowired LawyersController controller;
	
    @Test
    public void testLawyer()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	LawyerDTO result = controller.lawyer(lawyer.getUsername());
    	Assert.assertEquals(result.getId(),lawyer.getId());
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testSearchPoints()
    {
    	for( int i=0; i < 10; ++i )
    	{
    		Lawyer lawyer = Generator.lawyer();
    		lawyer.setPoints(i);
    		lawyerDAO.put(lawyer);
    	}
    	
    	LawyersSearchResponse response = controller.search(5, 3);
    	Assert.assertEquals(response.getLawyers().size(),3);
    	Assert.assertEquals(response.getLawyers().get(0).getPoints().intValue(),4);
    	Assert.assertEquals(response.getLawyers().get(1).getPoints().intValue(),3);
    	Assert.assertEquals(response.getLawyers().get(2).getPoints().intValue(),2);
    	
    	lawyerDAO.deleteAll();
    }
}

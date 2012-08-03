package tanls.database;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.entity.Lawyer;
import tanls.entity.TanlsUser;

public class LawyerDAOTest extends BaseTest
{
	@Autowired LawyerDAO lawyerDAO;
	@Autowired TanlsUserDAO tanlsUserDAO;
	
    @Test
    public void testGetByUsername()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	Lawyer result = lawyerDAO.getByUsername(lawyer.getUsername());
    	Assert.assertEquals(result.getId(),lawyer.getId());
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testGetByUUID()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	Lawyer result = lawyerDAO.getByUuid(lawyer.getUuid());
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
    	
    	List<Lawyer> results = lawyerDAO.search(5, 3);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getPoints().intValue(),4);
    	Assert.assertEquals(results.get(1).getPoints().intValue(),3);
    	Assert.assertEquals(results.get(2).getPoints().intValue(),2);
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testSearchPercentage()
    {
    	for( int i=0; i < 10; ++i )
    	{
    		Lawyer lawyer = Generator.lawyer();
    		lawyer.setPercentagePositive(new BigDecimal(i));
    		lawyerDAO.put(lawyer);
    	}
    	
    	List<Lawyer> results = lawyerDAO.search(5, 3);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getPercentagePositive().intValue(),4);
    	Assert.assertEquals(results.get(1).getPercentagePositive().intValue(),3);
    	Assert.assertEquals(results.get(2).getPercentagePositive().intValue(),2);
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testSearchNoUsername()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUsername(null);
    	lawyerDAO.put(lawyer);
    	
    	List<Lawyer> results = lawyerDAO.search(null,null);
    	Assert.assertEquals(results.size(),0);
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testSearchNoName()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setName(null);
    	lawyerDAO.put(lawyer);
    	
    	List<Lawyer> results = lawyerDAO.search(null,null);
    	Assert.assertEquals(results.size(),0);
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testSearchNoPractitionerNumber()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setPractitionerNumber(null);
    	lawyerDAO.put(lawyer);
    	
    	List<Lawyer> results = lawyerDAO.search(null,null);
    	Assert.assertEquals(results.size(),0);
    	
    	lawyerDAO.deleteAll();
    }
    
    @Test
    public void testSearchWithUser()
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		Lawyer lawyer = Generator.lawyer();
    		lawyer.setUser(user);
    		lawyerDAO.put(lawyer);
    	}
    	
    	List<Lawyer> results = lawyerDAO.searchWithUser(5, 3);
    	for( Lawyer lawyer : results ) Assert.assertEquals(lawyer.getUser().getId(),user.getId());
    	
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

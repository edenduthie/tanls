package tanls.database;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;

public class ProfileItemDAOTest extends BaseTest
{
    @Autowired LawyerDAO lawyerDAO;
    @Autowired ProfileItemDAO profileItemDAO;
    @Autowired CustomerDAO customerDAO;
    
    @Test
    public void search()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		ProfileItem profileItem = Generator.profileItem();
    		profileItem.setLawyer(lawyer);
    		profileItem.setTimelineTime(new Long(i));
    		profileItemDAO.put(profileItem);
    	}
    	
    	List<ProfileItem> results = profileItemDAO.search(lawyer.getId(),ProfileItem.JOB,7l,3,null);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTimelineTime().intValue(),6);
    	Assert.assertEquals(results.get(1).getTimelineTime().intValue(),5);
    	Assert.assertEquals(results.get(2).getTimelineTime().intValue(),4);
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void searchNoLawyerId()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		ProfileItem profileItem = Generator.profileItem();
    		profileItem.setLawyer(lawyer);
    		profileItem.setTimelineTime(new Long(i));
    		profileItemDAO.put(profileItem);
    	}
    	
    	List<ProfileItem> results = profileItemDAO.search(null,null,7l,3,null);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTimelineTime().intValue(),6);
    	Assert.assertEquals(results.get(1).getTimelineTime().intValue(),5);
    	Assert.assertEquals(results.get(2).getTimelineTime().intValue(),4);
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void searchWithStatus()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		ProfileItem profileItem = Generator.profileItem();
    		profileItem.setLawyer(lawyer);
    		profileItem.setTimelineTime(new Long(i));
    		profileItem.setStatus(ProfileItem.APPROVED_STATUS);
    		profileItemDAO.put(profileItem);
    	}
    	
    	List<ProfileItem> results = profileItemDAO.search(lawyer.getId(),ProfileItem.JOB,7l,3,ProfileItem.NEW_STATUS);
    	Assert.assertEquals(results.size(),0);
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void searchNoText()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		ProfileItem profileItem = Generator.profileItem();
    		profileItem.setLawyer(lawyer);
    		profileItem.setTimelineTime(new Long(i));
    		profileItemDAO.put(profileItem);
    	}
    	
    	List<ProfileItem> results = profileItemDAO.searchNoText(lawyer.getId(),ProfileItem.JOB,7l,3,null);
    	Assert.assertEquals(results.size(),3);
    	Assert.assertEquals(results.get(0).getTimelineTime().intValue(),6);
    	Assert.assertEquals(results.get(1).getTimelineTime().intValue(),5);
    	Assert.assertEquals(results.get(2).getTimelineTime().intValue(),4);
    	for( ProfileItem result : results ) Assert.assertNull(result.getText() );
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void getWithLawyer()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
		ProfileItem profileItem = Generator.profileItem();
		profileItem.setLawyer(lawyer);
		profileItemDAO.put(profileItem);
    	
    	ProfileItem result = profileItemDAO.getWithLawyer(profileItem.getTitle());
    	Assert.assertEquals(result.getTitle(),profileItem.getTitle());
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void getCustomerLawyer()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	Customer customer = Generator.customer();
    	customerDAO.put(customer);
    	
		ProfileItem profileItem = Generator.profileItem();
		profileItem.setType(ProfileItem.RECOMMENDATION);
		profileItem.setLawyer(lawyer);
		profileItem.setCustomer(customer);
		profileItemDAO.put(profileItem);
    	
    	List<ProfileItem> result = profileItemDAO.getTypeByCustomerLawyer(customer, lawyer, ProfileItem.RECOMMENDATION);
    	Assert.assertEquals(result.size(),1);
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
        customerDAO.deleteAll();
    }
    
    @Test
    public void getPercentagePositive()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	for( int i=0; i < 10; ++i )
    	{
    		ProfileItem profileItem = Generator.profileItem();
    		profileItem.setLawyer(lawyer);
    		profileItem.setRating(i);
    		profileItem.setType(ProfileItem.FEEDBACK);
    		profileItemDAO.put(profileItem);
    	}
    	
    	BigDecimal result = profileItemDAO.getPercentagePositive(lawyer);
    	Assert.assertEquals(result.intValue(),50);
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
    
    @Test
    public void getPercentagePositiveFraction()
    {
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	
    	for( int i=0; i < 2; ++i )
    	{
    		ProfileItem profileItem = Generator.profileItem();
    		profileItem.setLawyer(lawyer);
    		profileItem.setRating(10);
    		profileItem.setType(ProfileItem.FEEDBACK);
    		profileItemDAO.put(profileItem);
    	}
    	for( int i=0; i < 1; ++i )
    	{
    		ProfileItem profileItem = Generator.profileItem();
    		profileItem.setLawyer(lawyer);
    		profileItem.setRating(1);
    		profileItem.setType(ProfileItem.FEEDBACK);
    		profileItemDAO.put(profileItem);
    	}
    	
    	BigDecimal result = profileItemDAO.getPercentagePositive(lawyer);
    	Assert.assertEquals(result.intValue(),66);
    
    	profileItemDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
}

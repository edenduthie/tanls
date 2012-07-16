package tanls.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.ProfileItemDTO;

public class ProfileTest 
{
    @Test
    public void constructor()
    {
    	ProfileItem pi = Generator.profileItem();
    	pi.setId(666);
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setId(999);
    	pi.setLawyer(lawyer);
    	Customer customer = Generator.customer();
    	customer.setId(999);
    	pi.setCustomer(customer);
    	AreaOfPractise area = Generator.areaOfPractise();
    	area.setId(444);
    	pi.setAreaOfPractise(area);
    	
    	ProfileItemDTO dto = new ProfileItemDTO(pi);
    	
    	
    	ProfileItem result = new ProfileItem(dto);
        Assert.assertEquals(result.getId(),pi.getId());
        Assert.assertNotNull(result.getLawyer());
        Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
        Assert.assertEquals(result.getCustomer().getId(),customer.getId());
        Assert.assertEquals(result.getAreaOfPractise().getId(),area.getId());
    }
}

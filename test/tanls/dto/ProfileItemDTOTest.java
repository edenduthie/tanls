package tanls.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.entity.AreaOfPractise;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;

public class ProfileItemDTOTest 
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
    	Assert.assertEquals(dto.getId(),pi.getId());
    	Assert.assertNotNull(dto.getLawyer());
    	Assert.assertEquals(dto.getLawyer().getId(),lawyer.getId());
    	Assert.assertEquals(dto.getCustomer().getId(),customer.getId());
    	Assert.assertEquals(dto.getAreaOfPractise().getId(),area.getId());
    }
}

package tanls.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.TanlsUserDTO;
import tanls.entity.TanlsUser;

public class TanlsUserDTOTest 
{
    @Test
    public void testConstructor()
    {
    	TanlsUser user = Generator.tanlsUser();
    	TanlsUserDTO dto = new TanlsUserDTO(user);
    	Assert.assertEquals(dto.getEmail(),user.getEmail());
    	Assert.assertEquals(dto.getLawyer(),user.getLawyer());
    }
}

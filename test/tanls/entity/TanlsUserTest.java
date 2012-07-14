package tanls.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.TanlsUserDTO;

public class TanlsUserTest 
{
    @Test
    public void constructor()
    {
    	TanlsUser user = Generator.tanlsUser();
    	TanlsUserDTO dto = new TanlsUserDTO(user);
    	TanlsUser newUser = new TanlsUser(dto);
    	Assert.assertEquals(newUser.getEmail(),user.getEmail());
    	Assert.assertEquals(newUser.getLawyer(),user.getLawyer());
    }
}

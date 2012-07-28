package tanls.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.entity.TanlsGrantedAuthority;

public class TanlsGrantedAuthorityDAOTest extends BaseTest
{
    @Autowired TanlsGrantedAuthorityDAO dao;
    
    @Test
    public void getRoleUser()
    {
    	TanlsGrantedAuthority auth = Generator.tanlsGrantedAuthority();
    	dao.put(auth);
    	TanlsGrantedAuthority result = dao.getRoleUser();
    	Assert.assertEquals(result.getAuthority(),auth.getAuthority());
    	Assert.assertEquals(result.getId(),auth.getId());
    	dao.deleteAll();
    }
    
    @Test
    public void getRoleUserNotFound()
    {
    	TanlsGrantedAuthority result = dao.getRoleUser();
    	Assert.assertNotNull(result);
    	Assert.assertEquals(result.getAuthority(),TanlsGrantedAuthority.ROLE_USER);
    	dao.deleteAll();
    }
    
    @Test
    public void getRolePractitioner()
    {
    	TanlsGrantedAuthority auth = Generator.tanlsGrantedAuthority();
    	auth.setAuthority(TanlsGrantedAuthority.ROLE_PRACTITIONER);
    	dao.put(auth);
    	TanlsGrantedAuthority result = dao.getRolePractitioner();
    	Assert.assertEquals(result.getAuthority(),auth.getAuthority());
    	Assert.assertEquals(result.getId(),auth.getId());
    	dao.deleteAll();
    }
    
    @Test
    public void getRolePractitionerNotFound()
    {
    	TanlsGrantedAuthority result = dao.getRolePractitioner();
    	Assert.assertNotNull(result);
    	Assert.assertEquals(result.getAuthority(),TanlsGrantedAuthority.ROLE_PRACTITIONER);
    	dao.deleteAll();
    }
}

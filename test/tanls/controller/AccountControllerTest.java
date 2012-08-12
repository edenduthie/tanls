package tanls.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.TanlsUserDAO;
import tanls.dto.UpdateEmailPasswordDTO;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.AccountService;
import tanls.service.LoginStatus;

public class AccountControllerTest extends BaseTest
{
    @Autowired AccountController controller;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired AccountService accountService;
    
    public static final String newEmail = "ya@gmail.com";
    public static final String newPassword = "holygoat";
    
    @Test
    public void testUpdate() throws InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
    	
    	
    	controller.updateEmailPassword( new UpdateEmailPasswordDTO(newEmail, newPassword, newPassword));
    	TanlsUser result = accountService.get(user.getId());
    	Assert.assertEquals(result.getEmail(),newEmail);
    	Assert.assertEquals(result.getPassword(),accountService.encryptPassword(newPassword));
    	
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
}

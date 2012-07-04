package tanls.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WebUtilTest 
{
    @Test
    public void isANumberYes()
    {
    	Assert.assertTrue(WebUtil.isANumber("1231234123123"));
    }
    
    @Test
    public void isANumberNo()
    {
    	Assert.assertFalse(WebUtil.isANumber("12312SDDS123123"));
    }
}

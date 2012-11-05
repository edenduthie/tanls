package tanls.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FriendlyController 
{
	public static final Logger log = Logger.getLogger(FriendlyController.class);
	
	@RequestMapping(value="/friendly/lawyers/{username}", method = RequestMethod.GET)
	public void lawyer(@PathVariable String username,
			HttpServletRequest request, HttpServletResponse response)
	{
		String url = request.getContextPath() + "/#!/lawyers/" + username;
		try 
		{
			response.sendRedirect(url);
		} 
		catch (IOException e) 
		{
			log.error(e);
		}
	}
}

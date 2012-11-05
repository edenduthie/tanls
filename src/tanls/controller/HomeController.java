package tanls.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController 
{

	public static final String HOME_VIEW = "home";
	public static final String TWITTER_VIEW = "twitterwidget";
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(HOME_VIEW);
		return mav;
    }
	
	@RequestMapping(value="/twitter", method = RequestMethod.GET)
	public ModelAndView twitter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(TWITTER_VIEW);
		return mav;
    }
}

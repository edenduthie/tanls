package tanls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tanls.dto.QuoteDTO;
import tanls.entity.Quote;
import tanls.entity.TanlsUser;
import tanls.service.LoginStatus;
import tanls.service.QuoteService;

@Controller
public class QuoteController 
{
	@Autowired LoginStatus loginStatus;
	@Autowired QuoteService quoteService;
	
	@RequestMapping(value="/customer/quotes/{quoteId}", method = RequestMethod.GET)
	public @ResponseBody QuoteDTO get(@PathVariable Integer quoteId)
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new QuoteDTO("You need to be logged in to book a quote",false,true);
		Quote quote = quoteService.getQuote(user, quoteId);
		if( quote != null ) return new QuoteDTO(quote);
		else return null;
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}
}

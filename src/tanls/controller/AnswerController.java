package tanls.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tanls.dto.AnswerDTO;
import tanls.entity.Answer;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.AnswerService;
import tanls.service.LoginStatus;

@Controller
public class AnswerController 
{
	@Autowired AnswerService answerService;
	@Autowired LoginStatus loginStatus;
	
	private static final Logger log = Logger.getLogger(AnswerController.class);
	
	@RequestMapping(value="/answers", method = RequestMethod.POST)
	public @ResponseBody AnswerDTO answerQuestion(@RequestBody AnswerDTO answer)
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new AnswerDTO("You must be logged in to ask a question",false,true);
		if( answer == null ) return new AnswerDTO("Please provide an answer",false);
		Answer result;
		try 
		{
			result = answerService.createAnswer(user, new Answer(answer));
			return new AnswerDTO(result);
		} 
		catch (InvalidInputException e) 
		{
			return new AnswerDTO(e.getMessage(),false);
		}
	}
	
	@RequestMapping(value="/answers", method = RequestMethod.PUT)
	public @ResponseBody AnswerDTO updateAnswerText(@RequestBody AnswerDTO answer)
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new AnswerDTO("You must be logged in to ask a question",false,true);
		if( answer == null ) return new AnswerDTO("Please provide an answer",false);
		Answer result;
		try 
		{
			result = answerService.updateAnswerText(user, new Answer(answer));
			return new AnswerDTO(result);
		} 
		catch (InvalidInputException e) 
		{
			return new AnswerDTO(e.getMessage(),false);
		}
	}
	
	@RequestMapping(value="/answers/status", method = RequestMethod.PUT)
	public @ResponseBody AnswerDTO updateAnswerStatus(@RequestBody AnswerDTO answer)
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new AnswerDTO("You must be logged in to ask a question",false,true);
		if( answer == null ) return new AnswerDTO("Please provide an answer",false);
		Answer result;
		try 
		{
			result = answerService.updateAnswerStatus(user, new Answer(answer));
			return new AnswerDTO(result);
		} 
		catch (InvalidInputException e) 
		{
			return new AnswerDTO(e.getMessage(),false);
		}
	}
	
	@RequestMapping(value="/answers/{questionId}", method = RequestMethod.GET)
	public @ResponseBody List<AnswerDTO> get(
	    @PathVariable Integer questionId,
	    @RequestParam(value="limit", required = false ) Integer limit,
	    @RequestParam(value="offset",required = false) Integer offset,
	    @RequestParam(value="status",required = false) String status
	) 
	{
		List<AnswerDTO> results = new ArrayList<AnswerDTO>();
		
		TanlsUser user = loginStatus.getUserService();
		if( user == null )
		{
			log.error("Unauthorised access of answers");
			return results;
		}
		
		try 
		{
			List<Answer> answers = answerService.getAnswers(user, questionId, limit, offset, status);
			for( Answer answer : answers )
			{
				results.add(new AnswerDTO(answer));
			}
			return results;
		} 
		catch (InvalidInputException e) 
		{
			log.error(e.getMessage());
			return results;
		}
	}
	
	@RequestMapping(value="/answers/all/{questionId}", method = RequestMethod.GET)
	public @ResponseBody List<AnswerDTO> getAll(
	    @PathVariable Integer questionId,
	    @RequestParam(value="limit", required = false ) Integer limit,
	    @RequestParam(value="offset",required = false) Integer offset
	) 
	{
		List<AnswerDTO> results = new ArrayList<AnswerDTO>();
		
		TanlsUser user = loginStatus.getUserService();
		if( user == null )
		{
			log.error("Unauthorised access of answers");
			return results;
		}
		
		try 
		{
			List<Answer> answers = answerService.getAllAnswers(user, questionId, limit, offset);
			for( Answer answer : answers )
			{
				results.add(new AnswerDTO(answer));
			}
			return results;
		} 
		catch (InvalidInputException e) 
		{
			log.error(e.getMessage());
			return results;
		}
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}
}

package tanls.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tanls.dto.QuestionDTO;
import tanls.dto.QuestionListDTO;
import tanls.entity.Question;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.LoginStatus;
import tanls.service.QuestionService;

@Controller
public class QuestionController 
{
	@Autowired LoginStatus loginStatus;
	@Autowired QuestionService questionService;
	
	@RequestMapping(value="/questions", method = RequestMethod.POST)
	public @ResponseBody QuestionDTO askQuestion(@RequestBody QuestionDTO question)
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new QuestionDTO("You must be logged in to ask a question",false,true);
		Question result;
		try 
		{
			result = questionService.askQuestion(new Question(question), user);
		} 
		catch (InvalidInputException e) 
		{
			return new QuestionDTO(e.getMessage(),false);
		}
		return new QuestionDTO(result);
	}
	
	@RequestMapping(value="/customer/questions", method = RequestMethod.GET)
	public @ResponseBody List<QuestionDTO> getCustomerQuestions(@RequestParam(value="limit",required=false) Integer limit,
			@RequestParam(value="offset",required=false) Integer offset)
	{
		TanlsUser user = loginStatus.getUserService();
		List<QuestionDTO> results = new ArrayList<QuestionDTO>();
		if( user == null ) return results;
		List<Question> questions = questionService.getCustomerQuestions(user, limit, offset);
		for( Question question : questions )
		{
			results.add(new QuestionDTO(question) );	
		}
		QuestionListDTO result = new QuestionListDTO();
		return results;
	}
	
	@RequestMapping(value="/customer/questions/{questionId}", method = RequestMethod.GET)
	public @ResponseBody QuestionDTO getFromCustomer(@PathVariable Integer questionId)
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new QuestionDTO("You need to be logged into view a question",false,true);
		Question question = questionService.get(user, questionId);
		if( question != null ) return new QuestionDTO(question);
		else return null;
	}
	
	@RequestMapping(value="/lawyer/questions", method = RequestMethod.GET)
	public @ResponseBody List<QuestionDTO> getLawyerQuestions(@RequestParam(value="limit",required=false) Integer limit,
			@RequestParam(value="offset",required=false) Integer offset)
	{
		TanlsUser user = loginStatus.getUserService();
		List<QuestionDTO> results = new ArrayList<QuestionDTO>();
		if( user == null ) return results;
		List<Question> questions = questionService.getLawyerQuestions(user, limit, offset);
		for( Question question : questions )
		{
			results.add(new QuestionDTO(question) );	
		}
		return results;
	}
	
	@RequestMapping(value="/lawyer/questions/{questionId}", method = RequestMethod.GET)
	public @ResponseBody QuestionDTO getAsLawyer(@PathVariable Integer questionId)
	{
		TanlsUser user = loginStatus.getUserService();
		if( user == null ) return new QuestionDTO("You need to be logged into view a question",false,true);
		Question question = questionService.getAsLawyer(user, questionId);
		if( question != null ) return new QuestionDTO(question);
		else return null;
	}
	

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}

	public QuestionService getQuestionService() {
		return questionService;
	}

	public void setQuestionService(QuestionService questionService) {
		this.questionService = questionService;
	}
}

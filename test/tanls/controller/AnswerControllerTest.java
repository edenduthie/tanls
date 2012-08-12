package tanls.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AnswerDAO;
import tanls.database.CustomerDAO;
import tanls.database.LawyerDAO;
import tanls.database.QuestionDAO;
import tanls.database.TanlsUserDAO;
import tanls.dto.AnswerDTO;
import tanls.entity.Answer;
import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.service.LoginStatus;

public class AnswerControllerTest extends BaseTest
{
	@Autowired TanlsUserDAO tanlsUserDAO;
	@Autowired QuestionDAO questionDAO;
	@Autowired LawyerDAO lawyerDAO;
	@Autowired AnswerController controller;
	@Autowired AnswerDAO answerDAO;
	@Autowired CustomerDAO customerDAO;
	
	@Test
    public void createAnswer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		
		AnswerDTO result = controller.answerQuestion(new AnswerDTO(answer));
		Assert.assertEquals(result.getQuestion().getId(),question.getId());
		Assert.assertEquals(result.getQuestion().getText(),question.getText());
		Assert.assertEquals(result.getStatus(),Answer.STATUS_NEW);
		Assert.assertEquals(result.getLawyer().getId(),lawyer.getId());
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
		controller.setLoginStatus(oldLoginStatus);
	}
	
	@Test
    public void createAnswerNoLawyerFound() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		
		AnswerDTO result = controller.answerQuestion(new AnswerDTO(answer));
		Assert.assertFalse(result.isSuccess());
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		tanlsUserDAO.deleteAll();
		controller.setLoginStatus(oldLoginStatus);
	}
	
	@Test
    public void updateAnswerText() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Lawyer lawyer = Generator.lawyer();
		lawyer.setUser(user);
		lawyerDAO.put(lawyer);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		questionDAO.put(question);

		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		answer.setLawyer(lawyer);
		answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setText("new text");
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		AnswerDTO result = controller.updateAnswerText(new AnswerDTO(newAnswer));
	    Assert.assertEquals(result.getText(),"new text");
	    Assert.assertEquals(result.getQuestion().getText(),question.getText());
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
		controller.setLoginStatus(oldLoginStatus);
	}
	
	@Test
    public void updateAnswerTextNoLawyer() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		Question dummyQuestion = Generator.question();
		dummyQuestion.setText("changed");
		dummyQuestion.setId(question.getId());
		answer.setQuestion(dummyQuestion);
		answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setText("new text");
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		AnswerDTO result = controller.updateAnswerText(new AnswerDTO(newAnswer));
		Assert.assertFalse(result.isSuccess());
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
		controller.setLoginStatus(oldLoginStatus);
	}
	
	@Test
    public void updateAnswerStatus() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		answer.setQuestion(question);
		answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setStatus(Answer.STATUS_USEFUL);
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		AnswerDTO result = controller.updateAnswerStatus(new AnswerDTO(newAnswer));
	    Assert.assertEquals(result.getStatus(),newAnswer.getStatus());
	    Assert.assertEquals(result.getQuestion().getText(),question.getText());
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
		controller.setLoginStatus(oldLoginStatus);
	}
	
	@Test
    public void updateAnswerStatusNoCustomerFound() throws InvalidInputException
	{
		TanlsUser user = Generator.tanlsUser();
		user.setLawyer(true);
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customerDAO.put(customer);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
		Answer answer = Generator.answer();
		answer.setQuestion(question);
		answerDAO.put(answer);
		
		Answer newAnswer = Generator.answer();
		newAnswer.setStatus(Answer.STATUS_USEFUL);
		newAnswer.setId(answer.getId());
		Question newDummyQuestion = Generator.question();
		newDummyQuestion.setId(question.getId());
		newDummyQuestion.setText("yaya");
		newAnswer.setQuestion(newDummyQuestion);
		
		AnswerDTO result = controller.updateAnswerStatus(new AnswerDTO(newAnswer));
	    Assert.assertFalse(result.isSuccess());
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		customerDAO.deleteAll();
		tanlsUserDAO.deleteAll();
		controller.setLoginStatus(oldLoginStatus);
	}
	
	@Test
    public void getAnswersWithStatus() throws InvalidInputException
    {
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<AnswerDTO> newAnswers = controller.get(question.getId(), 2, 1, Answer.STATUS_NEW);
    	Assert.assertEquals(newAnswers.size(),2);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(), 3);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(), 2);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
	
	@Test
    public void getAnswersWithStatusNoUser() throws InvalidInputException
    {
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(null);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<AnswerDTO> newAnswers = controller.get(question.getId(), 2, 1, Answer.STATUS_NEW);
    	Assert.assertEquals(newAnswers.size(),0);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
	
	@Test
    public void getAnswersWithStatusNoCustomer() throws InvalidInputException
    {
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		//customer.setUser(user);
		customerDAO.put(customer);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<AnswerDTO> newAnswers = controller.get(question.getId(), 2, 1, Answer.STATUS_NEW);
    	Assert.assertEquals(newAnswers.size(),0);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
	
	@Test
    public void getAllAnswers() throws InvalidInputException
    {
		TanlsUser user = Generator.tanlsUser();
		tanlsUserDAO.put(user);
		Customer customer = Generator.customer();
		customer.setUser(user);
		customerDAO.put(customer);
		
       	LoginStatus loginStatus = createMock(LoginStatus.class);
    	expect(loginStatus.getUserService()).andReturn(user);
    	expect(loginStatus.isLoggedInService()).andReturn(true);
    	LoginStatus oldLoginStatus = controller.getLoginStatus();
    	controller.setLoginStatus(loginStatus);
    	replay(loginStatus);
		
		Question question = Generator.question();
		question.setCustomer(customer);
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<AnswerDTO> newAnswers = controller.getAll(question.getId(), 2, 1);
    	Assert.assertEquals(newAnswers.size(),2);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(), 3);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(), 2);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	customerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    	controller.setLoginStatus(oldLoginStatus);
    }
}

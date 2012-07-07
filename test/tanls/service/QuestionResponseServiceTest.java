package tanls.service;

import java.io.File;
import java.io.IOException;

import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AnswerDAO;
import tanls.database.LawyerDAO;
import tanls.database.QuestionDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Answer;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

public class QuestionResponseServiceTest extends BaseTest
{
    @Autowired QuestionResponseService service;
    @Autowired LawyerDAO lawyerDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired QuestionDAO questionDAO;
    @Autowired AnswerDAO answerDAO;
    
    String testFile = "testdata/testemail.txt";
    String testContent = "testdata/testemailcontent.txt";
    
//    @Test
//    public void processEmails()
//    {
//    	service.processEmails();
//    }
    
    @Test
    public void processContent() throws IOException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Question question = Generator.question();
    	questionDAO.put(question);
    	
    	String subject = "Re: Easy Law Question #"+question.getId()+" #" + lawyer.getUuid();
    	String fromAddress = "eden@easylaw.com.au";
    	String content = FileUtils.readFileToString(new File(testContent));
    	
    	service.processContent(subject,fromAddress,content);
    	
    	Answer answer = answerDAO.getLawyerAnswer(lawyer, question);
    	Assert.assertNotNull(answer);
    	
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void processContentQuestionNotFound() throws IOException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Question question = Generator.question();
    	questionDAO.put(question);
    	
    	String subject = "Re: Easy Law Question #666666 #" + lawyer.getUuid();
    	String fromAddress = "eden@easylaw.com.au";
    	String content = FileUtils.readFileToString(new File(testContent));
    	
    	try
    	{
    	    service.processContent(subject,fromAddress,content);
    	    Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    	
    	Answer answer = answerDAO.getLawyerAnswer(lawyer, question);
    	Assert.assertNull(answer);
    	
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void processContentLawyerNotFound() throws IOException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Question question = Generator.question();
    	questionDAO.put(question);
    	
    	String subject = "Re: Easy Law Question #"+question.getId()+" #666666";
    	String fromAddress = "eden@easylaw.com.au";
    	String content = FileUtils.readFileToString(new File(testContent));

    	try
    	{
    	    service.processContent(subject,fromAddress,content);
    	}
    	catch(InvalidInputException e) {}
    	
    	Answer answer = answerDAO.getLawyerAnswer(lawyer, question);
    	Assert.assertNull(answer);

    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void processContentWrongEmail() throws IOException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Question question = Generator.question();
    	questionDAO.put(question);
    	
    	String subject = "Re: Easy Law Question #"+question.getId()+" #" + lawyer.getUuid();
    	String fromAddress = "eden@wrong.com";
    	String content = FileUtils.readFileToString(new File(testContent));
    	
    	try
    	{
    	    service.processContent(subject,fromAddress,content);
    	}
    	catch( InvalidInputException e ) {}
    	
    	Answer answer = answerDAO.getLawyerAnswer(lawyer, question);
    	Assert.assertNull(answer);
    	
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void getQuestionNumber() throws InvalidInputException
    {
    	String subject = "Re: Easy Law Question #23434 #0df9cb47-6d8b-4a4d-b914-4d2ac6e7b9ab";
    	Integer questionNumber = service.getQuestionNumber(subject);
    	Assert.assertEquals(questionNumber.intValue(),23434);
    }
    
    @Test
    public void getQuestionNumberNoHash() throws InvalidInputException
    {
    	String subject = "Re: Easy Law Question 23434 0df9cb47-6d8b-4a4d-b914-4d2ac6e7b9ab";
    	try
    	{
    		service.getQuestionNumber(subject);
    		Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    }
    
    @Test
    public void getQuestionNumberNoSpaceAfterHash() throws InvalidInputException
    {
    	String subject = "Re: Easy Law Question #234340df9cb47-6d8b-4a4d-b914-4d2ac6e7b9ab";
    	try
    	{
    		service.getQuestionNumber(subject);
    		Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    }
    
    @Test
    public void getQuestionNumberInvalidNumber() throws InvalidInputException
    {
    	String subject = "Re: Easy Law Question #234340ddd #df9cb47-6d8b-4a4d-b914-4d2ac6e7b9ab ";
    	try
    	{
    		service.getQuestionNumber(subject);
    		Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    }
    
    @Test
    public void getLawyerUUID() throws InvalidInputException
    {
    	String subject = "Re: Easy Law Question #23434 #0df9cb47-6d8b-4a4d-b914-4d2ac6e7b9ab";
    	String uuid = service.getLawyerUUID(subject);
    	Assert.assertEquals(uuid,"0df9cb47-6d8b-4a4d-b914-4d2ac6e7b9ab");
    }
    
    @Test
    public void getLawyerUUIDNoFirstHash() throws InvalidInputException
    {
    	String subject = "Re: Easy Law Question 23434 0df9cb47-6d8b-4a4d-b914-4d2ac6e7b9ab";
    	try
    	{
    		service.getLawyerUUID(subject);
    		Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    }
    
    @Test
    public void getLawyerUUIDNoSecondHash() throws InvalidInputException
    {
    	String subject = "Re: Easy Law Question #23434 0df9cb47-6d8b-4a4d-b914-4d2ac6e7b9ab";
    	try
    	{
    		service.getLawyerUUID(subject);
    		Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    }
    
    @Test
    public void getLawyerUUIDNoUUID() throws InvalidInputException
    {
    	String subject = "Re: Easy Law Question #23434 #";
    	try
    	{
    		service.getLawyerUUID(subject);
    		Assert.fail();
    	}
    	catch(InvalidInputException e) {}
    }
    
    @Test
    public void stripReply() throws IOException, InvalidInputException
    {
    	String content = FileUtils.readFileToString(new File(testContent));
    	String stripped = service.stripReply(content);
    }
    
    @Test
    public void stripReplyGmailNoNewlines() throws InvalidInputException
    {
    	String message = "This is my fancy answer It has multiple lines. It comes from gmail. On Tue, May 21, 2013 at 10:52 AM, <questions@easylaw.com.au> wrote: ---Please reply above this line--- Hi Eden Duthie, An Easy Law customer has asked a question! If you know the answer reply to this email and gain 1 Easy Law point! If the customer finds the message useful you will receive 3 points. Go on, give it a go! Customer Name: Eden Duthie Business Name: Easy Law Question: This is the question You can also reply to this question via the web interface if you prefer. Have a great day! If you'd like to unsubscribe and stop receiving these emails click here.";    
    	String stripped = service.stripReply(message);
    	Assert.assertEquals(stripped,"This is my fancy answer It has multiple lines. It comes from gmail. ");
    }
    
    @Test
    public void stripReplyFrom() throws InvalidInputException
    {
    	String message = "This is my fancy answer It has multiple lines.\\r\\nIt comes from gmail.\\r\\nFrom: eden@easylaw.com.au\\r\\n---Please reply above this line---\\r\\nHi Eden Duthie,\\r\\nAn Easy Law customer has asked a question!\\r\\nIfyou know the answer reply to this email and gain 1 Easy Law point!\\r\\nIf the customer finds the message useful you will receive 3 points. Go on, give it a go!\\r\\nCustomer Name: Eden Duthie\\r\\nBusiness Name: Easy Law\\r\\nQuestion: This is the question\\r\\nYou can also reply to this question via the web interface if you prefer.\\r\\nHave a great day!\\r\\nIf you'd like to unsubscribe and stop receiving these emails click here.";    
    	String stripped = service.stripReply(message);
    	Assert.assertEquals(stripped,"This is my fancy answer It has multiple lines.\\r\\nIt comes from gmail.\\r\\n");
    }
    
    @Test
    public void stripReplyJustEmailWIthBrackets() throws InvalidInputException
    {
    	String message = "This is my fancy answer It has multiple lines.\\r\\nIt comes from gmail.\\r\\n<eden@easylaw.com.au>\\r\\n---Please reply above this line---\\r\\nHi Eden Duthie,\\r\\nAn Easy Law customer has asked a question!\\r\\nIfyou know the answer reply to this email and gain 1 Easy Law point!\\r\\nIf the customer finds the message useful you will receive 3 points. Go on, give it a go!\\r\\nCustomer Name: Eden Duthie\\r\\nBusiness Name: Easy Law\\r\\nQuestion: This is the question\\r\\nYou can also reply to this question via the web interface if you prefer.\\r\\nHave a great day!\\r\\nIf you'd like to unsubscribe and stop receiving these emails click here.";    
    	String stripped = service.stripReply(message);
    	Assert.assertEquals(stripped,"This is my fancy answer It has multiple lines.\\r\\nIt comes from gmail.\\r\\n");
    }
    
    @Test
    public void stripReplyJustEmail() throws InvalidInputException
    {
    	String message = "This is my fancy answer It has multiple lines.\\r\\nIt comes from gmail.\\r\\neden@easylaw.com.au\\r\\n---Please reply above this line---\\r\\nHi Eden Duthie,\\r\\nAn Easy Law customer has asked a question!\\r\\nIfyou know the answer reply to this email and gain 1 Easy Law point!\\r\\nIf the customer finds the message useful you will receive 3 points. Go on, give it a go!\\r\\nCustomer Name: Eden Duthie\\r\\nBusiness Name: Easy Law\\r\\nQuestion: This is the question\\r\\nYou can also reply to this question via the web interface if you prefer.\\r\\nHave a great day!\\r\\nIf you'd like to unsubscribe and stop receiving these emails click here.";    
    	String stripped = service.stripReply(message);
    	Assert.assertEquals(stripped,"This is my fancy answer It has multiple lines.\\r\\nIt comes from gmail.\\r\\n");
    }
    
    @Test
    public void stripReplyOriginalMessage() throws InvalidInputException
    {
    	String message = "This is my fancy answer It has multiple lines.\\r\\nIt comes from gmail.\\r\\n--original message--\\r\\n---Please reply above this line---\\r\\nHi Eden Duthie,\\r\\nAn Easy Law customer has asked a question!\\r\\nIfyou know the answer reply to this email and gain 1 Easy Law point!\\r\\nIf the customer finds the message useful you will receive 3 points. Go on, give it a go!\\r\\nCustomer Name: Eden Duthie\\r\\nBusiness Name: Easy Law\\r\\nQuestion: This is the question\\r\\nYou can also reply to this question via the web interface if you prefer.\\r\\nHave a great day!\\r\\nIf you'd like to unsubscribe and stop receiving these emails click here.";    
    	String stripped = service.stripReply(message);
    	Assert.assertEquals(stripped,"This is my fancy answer It has multiple lines.\\r\\nIt comes from gmail.\\r\\n");
    }
    
    @Test
    public void sendMessageOnError() throws MessagingException
    {
    	String from = "eden@easylaw.com.au";
    	String subject = "This was the subject of the message";
    	Exception error = new Exception("This is the exception message");
    	Message message = EasyMock.createMock(Message.class);
    	message.setFlag(Flag.DELETED, true);
    	EasyMock.replay(message);
    	
    	service.sendMessageOnError(from,subject,error,message);
    }
    
    @Test
    public void dodgySubject()
    {
    	Assert.assertTrue(service.dodgySubject("[Auto-Reply] This is the auto reply"));
    	Assert.assertTrue(service.dodgySubject("In the middle [Auto-Reply] This is the auto reply"));
    	Assert.assertTrue(service.dodgySubject("Case insensitive [auto-reply] This is the auto reply"));
    	Assert.assertFalse(service.dodgySubject("not dodgy [Auto-Repl This is the auto reply"));
    }
    
    @Test
    public void processContentQuestionRequiresAQuote() throws IOException, InvalidInputException
    {
    	TanlsUser user = Generator.tanlsUser();
    	user.setLawyer(true);
    	tanlsUserDAO.put(user);
    	
    	Lawyer lawyer = Generator.lawyer();
    	lawyer.setUser(user);
    	lawyerDAO.put(lawyer);
    	
    	Question question = Generator.question();
    	question.setQuoteRequired(true);
    	questionDAO.put(question);
    	
    	String subject = "Re: Easy Law Question #"+question.getId()+" #" + lawyer.getUuid();
    	String fromAddress = "eden@easylaw.com.au";
    	String content = FileUtils.readFileToString(new File(testContent));
    	
    	try
    	{
    	    service.processContent(subject,fromAddress,content);
    	}
    	catch(InvalidInputException e) {}
    	
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

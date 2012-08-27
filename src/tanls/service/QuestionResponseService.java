package tanls.service;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.database.LawyerDAO;
import tanls.database.QuestionDAO;
import tanls.entity.Answer;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.exception.InvalidInputException;

import com.sun.mail.imap.IMAPBodyPart;
import com.sun.mail.imap.IMAPFolder;

@Service
public class QuestionResponseService 
{
	public static final Logger log = Logger.getLogger(QuestionResponseService.class);
	
	public static final int MAX_REPLY_CHARACTERS = 10000;
	
	@Autowired QuestionDAO questionDAO;
	@Autowired LawyerDAO lawyerDAO;
	@Autowired AnswerService answerService;
	@Autowired MailService mailService;
	
	private String imapServer;
	private String questionsEmail;
	private String questionsPassword;
	
    public void processEmails()
    {
    	Properties props = System.getProperties();
    	props.setProperty("mail.store.protocol", "imaps");
    	try 
    	{
    	    Session session = Session.getDefaultInstance(props, null);
    	    Store store = session.getStore("imaps");
    	    store.connect(getImapServer(), getQuestionsEmail(), getQuestionsPassword());
            Folder folder = (IMAPFolder) store.getFolder("inbox");
            if(!folder.isOpen())
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            //FlagTerm unread = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            log.info("No of Messages : " + messages.length);
            for( Message message : messages )
            {
            	processMessage(message);
            }
            folder.close(true);
            store.close();
    	} 
    	catch (NoSuchProviderException e) 
    	{
    	    log.error(e);
    	} 
    	catch (MessagingException e) 
    	{
    	     log.error(e);
    	}
    }
    
    public void processMessage(Message message)
    {
    	String subject = "";
    	String fromAddress = null;
    	
    	try 
    	{
    		subject = message.getSubject();
    		if( dodgySubject(subject) ) return;
    		Address[] from = message.getFrom();
    		if( from.length <= 0 ) throw new InvalidInputException("No from address");
    		fromAddress = ((InternetAddress) from[0]).getAddress();
    		Object content = message.getContent();
    		String contentString = null;
    		if( content instanceof String ) contentString = (String) content;
    		else if( content instanceof javax.mail.internet.MimeMultipart )
    		{
    			MimeMultipart multipartContent = (MimeMultipart) content;
    			for( int i=0; i < multipartContent.getCount(); ++i )
    			{
    				IMAPBodyPart part = (IMAPBodyPart) multipartContent.getBodyPart(i);
    				if( part.isMimeType("text/plain") ) 
    				{
    					Object object = part.getContent();
    					if( object instanceof String ) contentString = (String) object;
    					break;
    				}
    				else if( part.isMimeType("text/html") ) 
    				{
    					Object object = part.getContent();
    					String htmlString = null;
    					if( object instanceof String ) htmlString = (String) object;
    					else log.error("Input class is not a string: " + object.getClass());
    					if( htmlString != null )
    					{
    						Document doc = Jsoup.parse(htmlString);
    						contentString = doc.body().text();
    					}
    				}
    				else
    				{
    					log.warn("Unsupported mime type: " + part.getContentType());
    				}
    			}
    		}
    		if( contentString != null ) processContent(subject,fromAddress,contentString);
			message.setFlag(Flags.Flag.DELETED, true);
		} 
    	catch (MessagingException e) 
    	{
			log.error("Failed to delete message: " + subject + " " + e.getMessage());
		} 
    	catch (InvalidInputException e) 
    	{
			log.error(e);
			sendMessageOnError(fromAddress,subject,e,message);
		} 
    	catch (IOException e) 
    	{
    		log.error(e);
    		sendMessageOnError(fromAddress,subject,e,message);
		}
    	catch(Exception e)
    	{
    		log.error(e);
    		sendMessageOnError(fromAddress,subject,e,message);
    	}
    }
    
    public boolean dodgySubject(String subject) 
    {
    	Pattern pattern = Pattern.compile("Auto-Reply", Pattern.CASE_INSENSITIVE);
    	Matcher matcher = pattern.matcher(subject);
    	if( matcher.find() ) return true;
    	else return false;
	}

	public void sendMessageOnError(String from, String subject, Exception error, Message message)
    {
    	if( from != null )
    	{
    		String responseSubject = "Easy Law message processing error";
    		String body = "<p>Hi!</p>";
    		body += "<p>Unfortunately there was an error processing your question response.</p>";
    		if( subject != null ) body += "<p><strong>Email Subject: </strong> " + subject + "</p>";
    		if( error.getMessage() != null ) body += "<p><strong>Error Message: </strong>" + error.getMessage() + "</p>";
    		body += "<p>Please try again, use the <a href='";
    		body += mailService.getBaseUrl();
    		body += "/#!/lawyer/questions";
    		body += "'>web interface</a>, or contact eden@easylaw.com.au for assistance.</p>";
    		body += "<p>Regards,</p><p>Easy Law</p>";
    		
    		try 
    		{
    			log.info("Sending error message to: " + from);
				mailService.sendMessageHTML(from, responseSubject, body);
				if( message != null ) message.setFlag(Flags.Flag.DELETED, true);
			} 
    		catch (AddressException e) 
    		{
				log.error(e);
			} 
    		catch (MessagingException e) 
    		{
				log.error(e);
			}
    	}
    }
    
    public void processContent(String subject, String from, String content) throws IOException, InvalidInputException
    {
    	Integer questionNumber = getQuestionNumber(subject);
    	Question question = questionDAO.get(questionNumber);
    	if( question == null ) throw new InvalidInputException("Could not find question: " + questionNumber);
    	if( question.getQuoteRequired() != null && question.getQuoteRequired() ) throw new InvalidInputException("This question requires a quote and can only be answered via the web interface.");
    	String uuid = getLawyerUUID(subject);
    	Lawyer lawyer = lawyerDAO.getByUuid(uuid);
    	if( lawyer == null ) throw new InvalidInputException("Could not find lawyer: " + lawyer);
    	if( !lawyer.getUser().getEmail().equalsIgnoreCase(from) ) throw new InvalidInputException("Sent from " + from + " expected " + lawyer.getUser().getEmail());
    	Answer answer = new Answer();
    	answer.setQuestion(question);
    	answer.setText(stripReply(content));
    	answerService.createAnswer(lawyer.getUser(), answer);
    }
    
    public Integer getQuestionNumber(String subject) throws InvalidInputException
    {
    	if( subject == null ) throw new InvalidInputException("No subject found");
    	subject = subject.trim();
    	if( subject.length() <= 0 ) throw new InvalidInputException("Subject is zero length");
    	int firstHash = subject.indexOf('#');
    	if( firstHash < 0 ) throw new InvalidInputException("No question number: " + subject);
    	int firstSpace = subject.indexOf(' ',firstHash);
    	if( firstSpace < 0 ) throw new InvalidInputException("First space not found: " + subject);
    	String questionNumberText = subject.substring(firstHash+1, firstSpace);
    	Integer questionNumberInt = null;
    	try
    	{
    		questionNumberInt = Integer.parseInt(questionNumberText);
    	}
    	catch(NumberFormatException e)
    	{
    		throw new InvalidInputException(e.getMessage());
    	}
    	return questionNumberInt;
    }
    
    public String getLawyerUUID(String subject) throws InvalidInputException
    {
    	if( subject == null ) throw new InvalidInputException("No subject found");
    	subject = subject.trim();
    	if( subject.length() <= 0 ) throw new InvalidInputException("Subject is zero length");
    	int firstHash = subject.indexOf('#');
    	if( firstHash < 0 ) throw new InvalidInputException("No question number: " + subject);
    	int secondHash = subject.indexOf('#', firstHash+1);
    	if( secondHash < 0 ) throw new InvalidInputException("No uuid: " + subject);
    	if( secondHash >= subject.length() - 1 ) throw new InvalidInputException("No uuid: " + subject);
    	return subject.substring(secondHash+1);
    }
    
    public String stripReply(String reply) throws InvalidInputException
    {
    	if( reply == null ) throw new InvalidInputException("Reply is null");
    	reply = reply.trim();
    	if( reply.length() <= 0 ) throw new InvalidInputException("Reply is zero length");
    	int replyIndex = reply.indexOf(QuestionService.REPLY_KEY);
    	if( replyIndex >= 0 ) reply = reply.substring(0, replyIndex);
    	
    	reply = removedMatchedPattern("\\n.*On.*(\\r\\n)?wrote:\\r\\n", reply);
    	reply = removedMatchedPattern("On.*?wrote:", reply);
    	reply = removedMatchedPattern("From:\\s*" + Pattern.quote(getQuestionsEmail()), reply);
    	reply = removedMatchedPattern("<"+Pattern.quote(getQuestionsEmail())+">", reply);
    	reply = removedMatchedPattern(Pattern.quote(getQuestionsEmail()), reply);
    	reply = removedMatchedPattern("-+original\\s+message-+",reply);
    	reply = removedMatchedPattern("from:\\s*$", reply);
    	
    	if( reply.length() > MAX_REPLY_CHARACTERS ) reply = reply.substring(0,MAX_REPLY_CHARACTERS);
    	
    	return reply;
    }
    
    public String removedMatchedPattern(String regex, String value)
    {
    	Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    	Matcher matcher = pattern.matcher(value);
    	if( matcher.find() )
    	{
    		int start = matcher.start();
    		if( start > 0 ) value = value.substring(0, start);
    	}
    	return value;
    }

	public String getImapServer() {
		return imapServer;
	}

	public void setImapServer(String imapServer) {
		this.imapServer = imapServer;
	}

	public String getQuestionsEmail() {
		return questionsEmail;
	}

	public void setQuestionsEmail(String questionsEmail) {
		this.questionsEmail = questionsEmail;
	}

	public String getQuestionsPassword() {
		return questionsPassword;
	}

	public void setQuestionsPassword(String questionsPassword) {
		this.questionsPassword = questionsPassword;
	}
}

package tanls.util;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tanls.service.QuestionResponseService;

public class QuestionServer {

	public static final Logger log = Logger.getLogger(QuestionServer.class);

	public static void main(String[] args) 
	{
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/mvc-dispatcher-servlet.question-server.xml");
        QuestionResponseService service = (QuestionResponseService) context.getBean("questionResponseService");
        
        if( service == null ) 
        {
        	log.error("Question Service is null");
        	return;
        }
        
        while(true)
        {
        	service.processEmails();
        	try 
        	{
				Thread.sleep(60000);
			} 
        	catch (InterruptedException e)
        	{
				log.error(e);
			}
        }
	}

}

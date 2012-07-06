package tanls.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.LawyerDAO;
import tanls.database.QuestionDAO;
import tanls.database.QuoteDAO;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.entity.Quote;
import tanls.exception.InvalidInputException;

public class QuoteServiceTest extends BaseTest
{
	@Autowired QuoteDAO quoteDAO;
	@Autowired QuestionDAO questionDAO;
	@Autowired LawyerDAO lawyerDAO;
	@Autowired QuoteService quoteService;
	
	
	@Test
	public void validatePaidNoPrice()
	{
		Question question = Generator.question();
		question.setProBono(false);
		Quote quote = Generator.quote();
		quote.setLegalFees(null);
		Lawyer lawyer = Generator.lawyer();
		
		try
		{
		    quoteService.createQuote(quote, lawyer, question);
		    Assert.fail();
		}
		catch(InvalidInputException e) {}
	}
	
	@Test
	public void validateNoText()
	{
		Question question = Generator.question();
		question.setProBono(false);
		Quote quote = Generator.quote();
		quote.setText(null);
		Lawyer lawyer = Generator.lawyer();
		
		try
		{
		    quoteService.createQuote(quote, lawyer, question);
		    Assert.fail();
		}
		catch(InvalidInputException e) {}
	}
	
	@Test
	public void validateZeroLengthText()
	{
		Question question = Generator.question();
		question.setProBono(false);
		Quote quote = Generator.quote();
		quote.setText("   ");
		Lawyer lawyer = Generator.lawyer();
		
		try
		{
		    quoteService.createQuote(quote, lawyer, question);
		    Assert.fail();
		}
		catch(InvalidInputException e) {}
	}
	
    @Test
    public void create() throws InvalidInputException
    {
    	Question question = Generator.question();
    	questionDAO.put(question);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	Quote quote = Generator.quote();
    	
    	quoteService.createQuote(quote, lawyer, question);
    	
    	Quote result = quoteDAO.get(quote.getId());
    	Assert.assertEquals(result.getDisbursments().intValue(),quote.getDisbursments().intValue());
    	
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    }
    
	@Test
	public void validatePaidNoPriceProBonoNull()
	{
		Question question = Generator.question();
		question.setProBono(null);
		Quote quote = Generator.quote();
		quote.setLegalFees(null);
		Lawyer lawyer = Generator.lawyer();
		
		try
		{
		    quoteService.createQuote(quote, lawyer, question);
		    Assert.fail();
		}
		catch(InvalidInputException e) {}
	}
	
    @Test
    public void createProBonoNoPrice() throws InvalidInputException
    {
    	Question question = Generator.question();
    	question.setProBono(true);
    	questionDAO.put(question);
    	Lawyer lawyer = Generator.lawyer();
    	lawyerDAO.put(lawyer);
    	Quote quote = Generator.quote();
    	quote.setLegalFees(null);
    	
    	quoteService.createQuote(quote, lawyer, question);
    	
    	Quote result = quoteDAO.get(quote.getId());
    	Assert.assertEquals(result.getDisbursments().intValue(),quote.getDisbursments().intValue());
    	
    	quoteDAO.deleteAll();
    	questionDAO.deleteAll();
    	lawyerDAO.deleteAll();
    }
}

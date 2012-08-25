package tanls.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.database.QuoteDAO;
import tanls.entity.Lawyer;
import tanls.entity.Question;
import tanls.entity.Quote;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;

@Service
public class QuoteService 
{
    @Autowired QuoteDAO quoteDAO;
    
    public Quote getQuote(TanlsUser user, Integer quoteId)
    {
    	return quoteDAO.getFromUser(user,quoteId);
    }
    
	public Quote createQuote(Quote quote, Lawyer lawyer, Question question) throws InvalidInputException
	{
		validate(quote,question);
		quote.setLawyer(lawyer);
		quote.setQuestion(question);
		quote.setTime(Calendar.getInstance().getTimeInMillis());
		quoteDAO.put(quote);
		return quote;
	}

	public void validate(Quote quote, Question question) throws InvalidInputException
	{
		if( (question.getProBono() == null || !question.getProBono()) && quote.getLegalFees() == null ) throw new InvalidInputException("Please provide the amount of legal fees for your quote");
		if( quote.getText() == null || quote.getText().trim().length() <= 0 ) throw new InvalidInputException("Please provide a description of the legal services with your quote");
	}
}

package tanls.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.QuoteDTO;

public class QuoteTest 
{
	@Test
    public void constructor()
    {
    	Quote quote = Generator.quote();
    	quote.setQuestion(Generator.question());
    	quote.setLawyer(Generator.lawyer());
    	File file = Generator.file();
    	file.setQuote(quote); // check circular dependencies
    	quote.setFile(file);
    	QuoteDTO quoteDTO = new QuoteDTO(quote);
    	Quote newQuote = new Quote(quoteDTO);
    	Assert.assertEquals(newQuote.getId(),quote.getId());
    	Assert.assertEquals(newQuote.getQuestion().getId(),quote.getQuestion().getId());
    	Assert.assertEquals(newQuote.getLawyer().getId(),quote.getLawyer().getId());
    	Assert.assertEquals(newQuote.getFile().getTime(),quote.getFile().getTime());
    }
	
	@Test
	public void getLegalFeesMinusComission()
	{
		Quote quote = Generator.quote();
		quote.setLegalFees(new BigDecimal(100));
		BigDecimal legalFeesMinusComission = quote.getLegalFeesMinusComission();
		legalFeesMinusComission = legalFeesMinusComission.setScale(2, RoundingMode.HALF_EVEN);
		Assert.assertEquals(legalFeesMinusComission.toPlainString(),"85.00");
	}
	
	@Test
	public void getDisbursmentsMinusCreditCard()
	{
		Quote quote = Generator.quote();
		quote.setDisbursments(new BigDecimal(100));
		BigDecimal d = quote.getDisbursmentsMinusCreditCard();
		d = d.setScale(2, RoundingMode.HALF_EVEN);
		Assert.assertEquals(d.toPlainString(),"97.00");
	}
	
	@Test
	public void getAmountMinusFees()
	{
		Quote quote = Generator.quote();
		quote.setLegalFees(new BigDecimal(100));
		quote.setDisbursments(new BigDecimal(100));
		BigDecimal result = quote.getAmountMinusFees();
		result = result.setScale(2, RoundingMode.HALF_EVEN);
		Assert.assertEquals(result.toPlainString(),"182.00");
	}
}

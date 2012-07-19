package tanls.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.AnswerDTO;

public class AnswerTest 
{
    @Test
    public void constructor()
    {
    	Answer answer = Generator.answer();
    	Question question = Generator.question();
    	answer.setQuestion(question);
    	Lawyer lawyer = Generator.lawyer();
    	answer.setLawyer(lawyer);
    	answer.setQuote(Generator.quote());
    	
    	AnswerDTO dto = new AnswerDTO(answer);
    	Answer result = new Answer(dto);
    	Assert.assertEquals(result.getText(),answer.getText());
    	Assert.assertEquals(result.getLawyer().getName(),lawyer.getName());
    	Assert.assertEquals(result.getQuestion().getText(),question.getText());
    	Assert.assertEquals(result.getQuote().getId(),answer.getQuote().getId());
    }
}

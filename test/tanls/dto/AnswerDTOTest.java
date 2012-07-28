package tanls.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.AnswerDTO;
import tanls.entity.Answer;
import tanls.entity.Lawyer;
import tanls.entity.Question;

public class AnswerDTOTest 
{
    @Test
    public void constructor()
    {
    	Answer answer = Generator.answer();
    	Question question = Generator.question();
    	answer.setQuestion(question);
    	Lawyer lawyer = Generator.lawyer();
    	answer.setLawyer(lawyer);
    	
    	AnswerDTO dto = new AnswerDTO(answer);
    	Assert.assertEquals(dto.getStatus(),answer.getStatus());
    	Assert.assertEquals(dto.getLawyer().getId(),lawyer.getId());
    	Assert.assertEquals(dto.getQuestion().getId(),question.getId());
    }
}

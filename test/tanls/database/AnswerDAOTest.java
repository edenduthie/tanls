package tanls.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.entity.Answer;
import tanls.entity.Lawyer;
import tanls.entity.Question;

public class AnswerDAOTest extends BaseTest
{
	@Autowired AnswerDAO answerDAO;
	@Autowired QuestionDAO questionDAO;
	@Autowired LawyerDAO lawyerDAO;
	
	@Test
    public void countAnswersWithStatus()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answerDAO.put(answer);
    	}
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answerDAO.put(answer);
    	}
    	
    	Assert.assertEquals(answerDAO.countAnswersWithStatus(Answer.STATUS_NEW, question).intValue(),5);
    	Assert.assertEquals(answerDAO.countAnswersWithStatus(Answer.STATUS_NOT_USEFUL, question).intValue(),5);
    	Assert.assertEquals(answerDAO.countAnswersWithStatus(null, question).intValue(),10);
    }
	
	@Test
    public void getLawyerAnswer()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
		Lawyer lawyer = Generator.lawyer();
		lawyerDAO.put(lawyer);
		
		Answer answer = Generator.answer();
		answer.setStatus(Answer.STATUS_NEW);
		answer.setQuestion(question);
		answer.setLawyer(lawyer);
		answerDAO.put(answer);
		
		Answer result = answerDAO.getLawyerAnswer(lawyer, question);
		Assert.assertNotNull(result);
		Assert.assertEquals(result.getId(),answer.getId());
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
    }
	
	@Test
    public void getLawyerAnswerNoLawyer()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
		Lawyer lawyer = Generator.lawyer();
		lawyerDAO.put(lawyer);
		
		Answer answer = Generator.answer();
		answer.setStatus(Answer.STATUS_NEW);
		answer.setQuestion(question);
		answerDAO.put(answer);
		
		Answer result = answerDAO.getLawyerAnswer(lawyer, question);
		Assert.assertNull(result);
		
		answerDAO.deleteAll();
		questionDAO.deleteAll();
		lawyerDAO.deleteAll();
    }
	
	@Test
    public void getAnswersWithStatus()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAnswersWithStatus(question, 2, 1, Answer.STATUS_NEW);
    	Assert.assertEquals(newAnswers.size(),2);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(), 3);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(), 2);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAnswersWithStatusWrongStatus()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAnswersWithStatus(question, 2, 1, Answer.STATUS_NEW);
    	Assert.assertEquals(newAnswers.size(),0);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAnswersWithStatusAndLawyer()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
		Lawyer lawyer = Generator.lawyer();
		lawyerDAO.put(lawyer);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answer.setLawyer(lawyer);
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAnswersWithStatus(question, 2, 1, Answer.STATUS_NEW);
    	Assert.assertEquals(newAnswers.size(),2);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(), 3);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(), 2);
    	Assert.assertEquals(newAnswers.get(0).getLawyer().getId(),lawyer.getId());
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
        lawyerDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswers()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=5; i < 10; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 10, 3);
    	Assert.assertEquals(newAnswers.size(),10);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(),1);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(),0);
    	Assert.assertEquals(newAnswers.get(2).getTime().intValue(),9);
    	Assert.assertEquals(newAnswers.get(3).getTime().intValue(),8);
    	Assert.assertEquals(newAnswers.get(4).getTime().intValue(),7);
    	Assert.assertEquals(newAnswers.get(5).getTime().intValue(),6);
    	Assert.assertEquals(newAnswers.get(6).getTime().intValue(),5);
    	Assert.assertEquals(newAnswers.get(7).getTime().intValue(),14);
    	Assert.assertEquals(newAnswers.get(8).getTime().intValue(),13);
    	Assert.assertEquals(newAnswers.get(9).getTime().intValue(),12);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswersSmackBangOnUseful()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=5; i < 10; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 9, 5);
    	Assert.assertEquals(newAnswers.size(),9);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(),9);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(),8);
    	Assert.assertEquals(newAnswers.get(2).getTime().intValue(),7);
    	Assert.assertEquals(newAnswers.get(3).getTime().intValue(),6);
    	Assert.assertEquals(newAnswers.get(4).getTime().intValue(),5);
    	Assert.assertEquals(newAnswers.get(5).getTime().intValue(),14);
    	Assert.assertEquals(newAnswers.get(6).getTime().intValue(),13);
    	Assert.assertEquals(newAnswers.get(7).getTime().intValue(),12);
    	Assert.assertEquals(newAnswers.get(8).getTime().intValue(),11);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswersInUseful()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=5; i < 10; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 7, 7);
    	Assert.assertEquals(newAnswers.size(),7);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(),7);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(),6);
    	Assert.assertEquals(newAnswers.get(2).getTime().intValue(),5);
    	Assert.assertEquals(newAnswers.get(3).getTime().intValue(),14);
    	Assert.assertEquals(newAnswers.get(4).getTime().intValue(),13);
    	Assert.assertEquals(newAnswers.get(5).getTime().intValue(),12);
    	Assert.assertEquals(newAnswers.get(6).getTime().intValue(),11);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswersSmackBangNotUseful()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=5; i < 10; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 3, 10);
    	Assert.assertEquals(newAnswers.size(),3);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(),14);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(),13);
    	Assert.assertEquals(newAnswers.get(2).getTime().intValue(),12);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswersNoLimit()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=5; i < 10; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, null, 3);
    	Assert.assertEquals(newAnswers.size(),12);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswersNoOffset()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=5; i < 10; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 15, null);
    	Assert.assertEquals(newAnswers.size(),15);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswersLastOfNew()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=5; i < 10; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 10, 4);
    	Assert.assertEquals(newAnswers.size(),10);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(),0);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(),9);
    	Assert.assertEquals(newAnswers.get(2).getTime().intValue(),8);
    	Assert.assertEquals(newAnswers.get(3).getTime().intValue(),7);
    	Assert.assertEquals(newAnswers.get(4).getTime().intValue(),6);
    	Assert.assertEquals(newAnswers.get(5).getTime().intValue(),5);
    	Assert.assertEquals(newAnswers.get(6).getTime().intValue(),14);
    	Assert.assertEquals(newAnswers.get(7).getTime().intValue(),13);
    	Assert.assertEquals(newAnswers.get(8).getTime().intValue(),12);
    	Assert.assertEquals(newAnswers.get(9).getTime().intValue(),11);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswersLastOfUseful()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=5; i < 10; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 3, 9);
    	Assert.assertEquals(newAnswers.size(),3);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(),5);
    	Assert.assertEquals(newAnswers.get(1).getTime().intValue(),14);
    	Assert.assertEquals(newAnswers.get(2).getTime().intValue(),13);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswersLast()
    {
		Question question = Generator.question();
		questionDAO.put(question);
		
    	for( int i=0; i < 5; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NEW);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=5; i < 10; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 10, 14);
    	Assert.assertEquals(newAnswers.size(),1);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(),10);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	@Test
    public void getAllAnswersLastNoFirst()
    {
		Question question = Generator.question();
		questionDAO.put(question);
    	
    	for( int i=10; i < 15; ++i )
    	{
    		Answer answer = Generator.answer();
    		answer.setStatus(Answer.STATUS_NOT_USEFUL);
    		answer.setQuestion(question);
    		answer.setTime(new Long(i));
    		answerDAO.put(answer);
    	}
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 10, 4);
    	Assert.assertEquals(newAnswers.size(),1);
    	Assert.assertEquals(newAnswers.get(0).getTime().intValue(),10);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
	
	@Test
    public void getAllAnswersNone()
    {
		Question question = Generator.question();
		questionDAO.put(question);
    	
    	List<Answer> newAnswers = answerDAO.getAllAnswers(question, 10, 4);
    	Assert.assertEquals(newAnswers.size(),0);
    			
    	answerDAO.deleteAll();
    	questionDAO.deleteAll();
    }
}

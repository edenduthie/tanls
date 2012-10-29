package tanls.database;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.Answer;
import tanls.entity.Lawyer;
import tanls.entity.Question;

@Service
public class AnswerDAO 
{
	@Autowired Database db;
    
    public void put(Answer answer)
    {
    	db.persist(answer);
    }
    public Answer get(Integer id)
    {
    	return (Answer) db.getNoNull(Answer.class.getName(), "id", id);
    }
    public Answer getWithQuestion(Integer id)
    {
    	String queryString = "from Answer a left join fetch a.question left join fetch a.lawyer left join fetch a.question.customer where a.id = :id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",id);
    	List<Answer> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    public void update(Answer answer)
    {
    	db.update(answer);
    }
    public void deleteAll()
    {
    	db.deleteAll(Answer.class);
    }
    
    public Long countAnswersWithStatus(String status, Question question)
    {
    	String queryString = "select count(a.id) from Answer a where a.question.id = :questionId";
    	if( status != null ) queryString += " and a.status = :status";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("questionId",question.getId());
    	if( status != null ) query.setParameter("status",status);
    	return (Long) query.getSingleResult();
    }
    
    public Answer getLawyerAnswer(Lawyer lawyer, Question question)
    {
    	String queryString = "from Answer a where a.question.id = :questionId and a.lawyer.id = :lawyerId";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("questionId",question.getId());
    	query.setParameter("lawyerId",lawyer.getId());
    	List<Answer> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public List<Answer> getAnswersWithStatus(Question question, Integer limit, Integer offset, String status)
    {
    	String queryString = "from Answer a left join fetch a.lawyer left join fetch a.quote q left join fetch q.file f where a.question.id = :questionId";
    	if( status != null ) queryString += " and a.status = :status";
    	queryString += " order by a.time desc";
    	Query query = db.getEntityManager().createQuery(queryString);
    	if( status != null ) query.setParameter("status", status);
    	if( limit != null ) query.setMaxResults(limit);
    	if( offset != null ) query.setFirstResult(offset);
    	query.setParameter("questionId", question.getId());
    	return query.getResultList();	
    }
    
    public List<Answer> getAllAnswers(Question question, Integer limit, Integer offset)
    {
    	List<Answer> results = new ArrayList<Answer>();
    	
    	if( offset == null ) offset = 0;
    	
    	Integer left = limit;
    	int localOffset = offset;
    	
    	int numNew = countAnswersWithStatus(Answer.STATUS_NEW,question).intValue();
    	
    	if( numNew > localOffset )
    	{
    		List<Answer> newAnswers = getAnswersWithStatus(question,limit,localOffset,Answer.STATUS_NEW);
    		results.addAll(newAnswers);
    		if( left != null ) left -= newAnswers.size();
    	} 
    	
    	localOffset -= numNew;
    	if( localOffset < 0 ) localOffset = 0;
    	int numUseful = countAnswersWithStatus(Answer.STATUS_USEFUL,question).intValue();
    	
    	if( (left == null || left > 0) && numUseful > localOffset )
    	{
    		List<Answer> usefulAnswers = getAnswersWithStatus(question,left,localOffset,Answer.STATUS_USEFUL);
    		results.addAll(usefulAnswers);
    		if( left != null ) left -= usefulAnswers.size();
    	}
    	
    	localOffset -= numUseful;
    	if( localOffset < 0 ) localOffset = 0;
    	int numNotUseful = countAnswersWithStatus(Answer.STATUS_NOT_USEFUL,question).intValue();
    	
    	if( (left == null || left > 0) && numNotUseful > localOffset )
    	{
    		List<Answer> notUsefulAnswers = getAnswersWithStatus(question,left,localOffset,Answer.STATUS_NOT_USEFUL);
    		results.addAll(notUsefulAnswers);
    	}
    	
    	return results;
    }
}

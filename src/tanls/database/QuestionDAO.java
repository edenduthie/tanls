package tanls.database;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.Customer;
import tanls.entity.Question;

@Service
public class QuestionDAO 
{
    @Autowired Database db;
    
    public Question get(Integer id)
    {
    	return (Question) db.getNoNull(Question.class.getName(), "id", id);
    }
    
    public Question getWithCustomer(Integer id)
    {
    	String queryString = "from Question q left join fetch q.customer c left join fetch c.user u where q.id = :id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",id);
    	List<Question> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public void put(Question question)
    {
    	db.persist(question);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(Question.class);
    }
    
    public void update(Question question)
    {
    	db.update(question);
    }
    
    public List<Question> getAll()
    {
    	return db.getAll(Question.class.getName());
    }
    
    public List<Question> getCustomerQuestions(Customer customer, Integer limit, Integer offset)
    {
    	String queryString = "from Question q left join fetch q.areaOfPractise where q.customer.id = :customerId";
    	queryString += " order by time desc";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("customerId",customer.getId());
    	if( limit != null ) query.setMaxResults(limit);
    	if( offset != null ) query.setFirstResult(offset);
    	return query.getResultList();
    }
    
    public Question getFromCustomer(Customer customer, Integer id)
    {
    	String queryString = "from Question q left join fetch q.areaOfPractise where q.customer.id = :customerId and q.id = :id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("customerId",customer.getId());
    	query.setParameter("id",id);
    	List<Question> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public List<Question> getLawyerQuestions(Integer limit, Integer offset)
    {
    	String queryString = "from Question order by time desc";
    	Query query = db.getEntityManager().createQuery(queryString);
    	if( limit != null ) query.setMaxResults(limit);
    	if( offset != null ) query.setFirstResult(offset);
    	return query.getResultList();
    }
} 

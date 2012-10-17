package tanls.database;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.Quote;
import tanls.entity.TanlsUser;

@Service
public class QuoteDAO 
{
    @Autowired Database db;
    
    public Quote get(Integer id)
    {
    	return (Quote) db.getNoNull(Quote.class.getName(), "id", id);
    }
    
    public void put(Quote quote)
    {
    	db.persist(quote);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(Quote.class);
    }
    
    public void update(Quote quote)
    {
    	db.update(quote);
    }
    
    public List<Quote> getAll()
    {
    	return db.getAll(Quote.class.getName());
    }
    
    public Quote getWithQuestionCustomerAndLawyer(Integer id)
    {
    	String queryString = "from Quote q left join fetch q.question question left join fetch q.lawyer lawyer left join fetch q.lawyer.user lawyerUser left join fetch q.question.customer customer left join fetch customer.user customerUser";
    	queryString += " where q.id = :id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",id);
    	List<Quote> quotes = query.getResultList();
    	if( quotes.size() > 0 ) return quotes.get(0);
    	else return null;
    }
    
    public Quote getFromUser(TanlsUser user, Integer id)
    {
    	String queryString = "from Quote q where q.question.customer.user.id = :userId and q.id = :quoteId";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("userId", user.getId());
    	query.setParameter("quoteId", id);
    	List<Quote> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
} 

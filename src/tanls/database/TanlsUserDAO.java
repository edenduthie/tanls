package tanls.database;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.TanlsUser;

@Service
public class TanlsUserDAO 
{
    @Autowired Database db;
    
    public TanlsUser get(Integer id)
    {
    	return (TanlsUser) db.getNoNull(TanlsUser.class.getName(), "id", id);
    }
    
    public TanlsUser get(String username)
    {
    	return (TanlsUser) db.getNoNull(TanlsUser.class.getName(), "email", username);
    }
    
    public void put(TanlsUser user)
    {
    	db.persist(user);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(TanlsUser.class);
    }
    
    public void update(TanlsUser user)
    {
    	db.update(user);
    }
    
    public List<TanlsUser> getAll()
    {
    	return db.getAll(TanlsUser.class.getName());
    }
    
    public Lawyer getLawyer(TanlsUser user)
    {
    	String queryString = "from Lawyer l where l.user.id = :userId";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("userId", user.getId());
    	List<Lawyer> lawyers = query.getResultList();
    	if( lawyers.size() > 0 ) return lawyers.get(0);
    	else return null;
    }
    
    public Customer getCustomer(TanlsUser user)
    {
    	String queryString = "from Customer c where c.user.id = :userId";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("userId", user.getId());
    	List<Customer> customers = query.getResultList();
    	if( customers.size() > 0 ) return customers.get(0);
    	else return null;
    }
}

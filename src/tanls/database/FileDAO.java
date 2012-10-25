package tanls.database;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.File;
import tanls.entity.TanlsUser;

@Service
public class FileDAO 
{
    @Autowired Database db;
    
    public File get(Integer id)
    {
    	return (File) db.getNoNull(File.class.getName(), "id", id);
    }
    
    public void put(File file)
    {
    	db.persist(file);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(File.class);
    }
    
    public void update(File file)
    {
    	db.update(file);
    }
    
    public List<File> getAll()
    {
    	return db.getAll(File.class.getName());
    }
    
    public File getWithPaymentAndQuote(Integer id)
    {
    	String queryString = "from File f left join fetch f.payment p left join fetch f.quote q left join fetch f.customer c left join fetch f.lawyer l where f.id = :id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",id);
    	List<File> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public File getByQuote(Integer quoteId)
    {
    	String queryString = "from File f where f.quote.id = :quoteId";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("quoteId",quoteId);
    	List<File> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public List<File> getCustomer(Integer limit, Integer offset, TanlsUser user)
    {
    	String queryString = "from File f left join fetch f.quote q left join fetch q.question qs left join fetch f.lawyer l left join fetch l.user lawyerUser left join fetch f.customer c left join fetch c.user customerUser left join fetch f.feedback fe";
    	queryString += " where customerUser.id = :userId";
    	queryString += " order by f.time desc";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("userId",user.getId());
    	if( limit != null ) query.setMaxResults(limit);
    	if( offset != null ) query.setFirstResult(offset);
    	return query.getResultList();
    }
    
    public List<File> getLawyer(Integer limit, Integer offset, TanlsUser user)
    {
    	String queryString = "from File f left join fetch f.quote q left join fetch q.question qs left join fetch f.lawyer l left join fetch l.user lawyerUser left join fetch f.customer c left join fetch c.user customerUser left join fetch f.feedback fe";
    	queryString += " where lawyerUser.id = :userId";
    	queryString += " order by f.time desc";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("userId",user.getId());
    	if( limit != null ) query.setMaxResults(limit);
    	if( offset != null ) query.setFirstResult(offset);
    	return query.getResultList();
    }

	public File getCustomerSingle(Integer fileId, TanlsUser user) {
		String queryString = "from File f left join fetch f.quote q left join fetch q.question qs left join fetch f.lawyer l left join fetch l.user lawyerUser left join fetch f.customer c left join fetch c.user customerUser left join fetch f.feedback fe";
    	queryString += " where customerUser.id = :userId";
    	queryString += " and f.id = :fileId";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("userId",user.getId());
    	query.setParameter("fileId",fileId);
    	List<File> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
	}
	
	public File getLawyerSingle(Integer fileId, TanlsUser user) {
		String queryString = "from File f left join fetch f.quote q left join fetch q.question qs left join fetch f.lawyer l left join fetch l.user lawyerUser left join fetch f.customer c left join fetch c.user customerUser left join fetch f.feedback fe";
		queryString += " where lawyerUser.id = :userId";
    	queryString += " and f.id = :fileId";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("userId",user.getId());
    	query.setParameter("fileId",fileId);
    	List<File> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
	}
}

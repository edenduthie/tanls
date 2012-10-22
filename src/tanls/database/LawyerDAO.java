package tanls.database;

import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.Lawyer;

@Service
public class LawyerDAO 
{
    @Autowired Database db;
    
    public Lawyer get(Integer id)
    {
    	return (Lawyer) db.getNoNull(Lawyer.class.getName(), "id", id);
    }
    
    public void put(Lawyer user)
    {
    	db.persist(user);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(Lawyer.class);
    }
    
    public void update(Lawyer user)
    {
    	db.update(user);
    }
    
    public List<Lawyer> getAll()
    {
    	return db.getAll(Lawyer.class.getName());
    }

	public Lawyer getByUsername(String username) {
		String queryString = "from Lawyer where username= :username";
		Query query = db.getEntityManager().createQuery(queryString);
		query.setParameter("username",username);
		List<Lawyer> lawyers = query.getResultList();
		if( lawyers.size() > 0 ) return lawyers.get(0);
		else return null;
	}
	
	public List<Lawyer> search(Integer offset, Integer limit) {
		String queryString = "from Lawyer";
		queryString += " where username is not null and name is not null and practitionerNumber is not null";
		queryString += " order by points desc, percentagePositive desc, timeAdmitted asc";
		Query query = db.getEntityManager().createQuery(queryString);
		if( offset != null ) query.setFirstResult(offset);
		if( limit != null ) query.setMaxResults(limit);
		return query.getResultList();
	}
	
	public List<Lawyer> searchWithUser(Integer offset, Integer limit) {
		String queryString = "from Lawyer l left join fetch l.user";
		queryString += " where l.username is not null and l.name is not null and l.practitionerNumber is not null";
		queryString += " order by l.points desc, l.percentagePositive desc, l.timeAdmitted asc";
		Query query = db.getEntityManager().createQuery(queryString);
		if( offset != null ) query.setFirstResult(offset);
		if( limit != null ) query.setMaxResults(limit);
		return query.getResultList();
	}
	
    public Lawyer getByUuid(String uuid)
    {
    	String queryString = "from Lawyer l left join fetch l.user where l.uuid = :uuid";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("uuid",uuid);
    	List<Lawyer> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public Lawyer getWithUser(Integer id)
    {
    	String queryString = "from Lawyer l left join fetch l.user u where l.id = :id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",id);
    	List<Lawyer> results = query.getResultList();
    	if( results.size() > 0  ) return results.get(0);
    	else return null;
    }
}

package tanls.database;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.TanlsGrantedAuthority;

@Service
public class TanlsGrantedAuthorityDAO 
{
    @Autowired Database db;
    
    public static final Logger log = Logger.getLogger(TanlsGrantedAuthorityDAO.class);
    
	public TanlsGrantedAuthority getRoleUser()
	{
		String queryString = "from TanlsGrantedAuthority where authority=:roleUser";
		Query query = db.getEntityManager().createQuery(queryString);
		query.setParameter("roleUser",TanlsGrantedAuthority.ROLE_USER);
		List<TanlsGrantedAuthority> results = query.getResultList();
		if( results.size() > 0 ) return results.get(0);
		else 
		{
			log.info("ROLE_USER not found in database, creating a new one");
			TanlsGrantedAuthority auth = new TanlsGrantedAuthority();
			auth.setAuthority(TanlsGrantedAuthority.ROLE_USER);
			db.persist(auth);
			return auth;
		}
	}
	
	public TanlsGrantedAuthority getRolePractitioner()
	{
		String queryString = "from TanlsGrantedAuthority where authority=:rolePractitioner";
		Query query = db.getEntityManager().createQuery(queryString);
		query.setParameter("rolePractitioner",TanlsGrantedAuthority.ROLE_PRACTITIONER);
		List<TanlsGrantedAuthority> results = query.getResultList();
		if( results.size() > 0 ) return results.get(0);
		else 
		{
			log.info("ROLE_PRACTITIONER not found in database, creating a new one");
			TanlsGrantedAuthority auth = new TanlsGrantedAuthority();
			auth.setAuthority(TanlsGrantedAuthority.ROLE_PRACTITIONER);
			db.persist(auth);
			return auth;
		}
	}
	
	public TanlsGrantedAuthority getRoleAdmin()
	{
		String queryString = "from TanlsGrantedAuthority where authority=:roleAdmin";
		Query query = db.getEntityManager().createQuery(queryString);
		query.setParameter("roleAdmin",TanlsGrantedAuthority.ADMIN_ROLE);
		List<TanlsGrantedAuthority> results = query.getResultList();
		if( results.size() > 0 ) return results.get(0);
		else 
		{
			log.info("ROLE_ADMIN not found in database, creating a new one");
			TanlsGrantedAuthority auth = new TanlsGrantedAuthority();
			auth.setAuthority(TanlsGrantedAuthority.ADMIN_ROLE);
			db.persist(auth);
			return auth;
		}
	}
	
	public void put(TanlsGrantedAuthority auth)
	{
		db.persist(auth);
	}
	
	public void deleteAll()
	{
		db.deleteAll(TanlsGrantedAuthority.class);
	}
}

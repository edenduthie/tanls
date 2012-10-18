package tanls.database;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.Customer;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;

@Service
public class ProfileItemDAO 
{
    @Autowired Database db;
    
    public ProfileItem getWithLawyer(Integer id)
    {
    	String queryString = "from ProfileItem pi left join fetch pi.lawyer left join fetch pi.customer left join fetch pi.areaOfPractise where pi.id = :id";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("id",id);
    	List<ProfileItem> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public ProfileItem getWithLawyer(String title)
    {
    	String queryString = "from ProfileItem pi left join fetch pi.lawyer left join fetch pi.customer left join fetch pi.areaOfPractise where pi.title = :title";
    	Query query = db.getEntityManager().createQuery(queryString);
    	query.setParameter("title",title);
    	List<ProfileItem> results = query.getResultList();
    	if( results.size() > 0 ) return results.get(0);
    	else return null;
    }
    
    public void put(ProfileItem profileItem)
    {
    	db.persist(profileItem);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(ProfileItem.class);
    }
    
    public void update(ProfileItem profileItem)
    {
    	db.update(profileItem);
    }
    
    public List<ProfileItem> getAll()
    {
    	return db.getAll(ProfileItem.class.getName());
    }

	public List<ProfileItem> search(Integer lawyerId, String type, Long lastTimelineTime, Integer limit, String status) 
	{
		String queryString = "from ProfileItem pi";
		String currentPrefix = " where ";
		if( lawyerId != null )
		{
			 queryString += currentPrefix + "pi.lawyer.id = :lawyerId";
			 currentPrefix = " and ";
		}
		if( type != null && (type.equals(ProfileItem.JOB) || type.equals(ProfileItem.EDUCATION)) ) 
		{
			queryString += currentPrefix + "(pi.type = :job or pi.type = :education)";
			currentPrefix = " and ";
		}
		else if( type != null ) 
		{
			queryString += currentPrefix + "pi.type = :type";
			currentPrefix = " and ";
		}
		if( lastTimelineTime != null )
		{
			queryString += currentPrefix + "timelineTime < :lastTimelineTime";
			currentPrefix = " and ";
		}
		if( status != null )
		{
			queryString += currentPrefix + "status = :status";
			currentPrefix = " and ";
		}
		queryString += " order by timelineTime desc";
		
		Query query = db.getEntityManager().createQuery(queryString);
		
		if( lawyerId != null )
		{
		    query.setParameter("lawyerId",lawyerId);
		}
		if( type != null && (type.equals(ProfileItem.JOB) || type.equals(ProfileItem.EDUCATION)) ) 
		{
			query.setParameter("job",ProfileItem.JOB);
			query.setParameter("education",ProfileItem.EDUCATION);
		}
		else if( type != null ) query.setParameter("type", type);
		if( lastTimelineTime != null ) query.setParameter("lastTimelineTime",lastTimelineTime);
		if( status != null ) query.setParameter("status", status);
		
		if(limit != null) query.setMaxResults(limit);
		
		return query.getResultList();
	}
	
	public List<ProfileItem> searchNoText(Integer lawyerId, String type, Long lastTimelineTime, Integer limit, String status) 
	{
		String queryString = "select new ProfileItem(id,title,timelineTime) from ProfileItem pi";
		String currentPrefix = " where ";
		if( lawyerId != null )
		{
			 queryString += currentPrefix + "pi.lawyer.id = :lawyerId";
			 currentPrefix = " and ";
		}
		if( type != null && (type.equals(ProfileItem.JOB) || type.equals(ProfileItem.EDUCATION)) ) 
		{
			queryString += currentPrefix + "(pi.type = :job or pi.type = :education)";
			currentPrefix = " and ";
		}
		else if( type != null ) 
		{
			queryString += currentPrefix + "pi.type = :type";
			currentPrefix = " and ";
		}
		if( lastTimelineTime != null )
		{
			queryString += currentPrefix + "timelineTime < :lastTimelineTime";
			currentPrefix = " and ";
		}
		if( status != null )
		{
			queryString += currentPrefix + "status = :status";
			currentPrefix = " and ";
		}
		queryString += " order by timelineTime desc";
		
		Query query = db.getEntityManager().createQuery(queryString);
		
		if( lawyerId != null )
		{
		    query.setParameter("lawyerId",lawyerId);
		}
		if( type != null && (type.equals(ProfileItem.JOB) || type.equals(ProfileItem.EDUCATION)) ) 
		{
			query.setParameter("job",ProfileItem.JOB);
			query.setParameter("education",ProfileItem.EDUCATION);
		}
		else if( type != null ) query.setParameter("type", type);
		if( lastTimelineTime != null ) query.setParameter("lastTimelineTime",lastTimelineTime);
		if( status != null ) query.setParameter("status", status);
		
		if(limit != null) query.setMaxResults(limit);
		
		return query.getResultList();
	}
	
	public List<ProfileItem> getTypeByCustomerLawyer(Customer customer, Lawyer lawyer, String type)
	{
		String queryString = "from ProfileItem p where p.customer.id = :customerId and p.lawyer.id = :lawyerId and p.type = :type";
		Query query = db.getEntityManager().createQuery(queryString);
		query.setParameter("customerId", customer.getId());
		query.setParameter("lawyerId",lawyer.getId());
		query.setParameter("type", type);
		return query.getResultList();
	}

	public void delete(ProfileItem profileItem) 
	{
		db.delete(ProfileItem.class.getName(), "id", profileItem.getId());
	}
	
	public BigDecimal getPercentagePositive(Lawyer lawyer)
	{
		String queryString = "select count(p.id) from ProfileItem p where p.lawyer.id = :lawyerId and p.type = :type";
		Query query = db.getEntityManager().createQuery(queryString);
		query.setParameter("lawyerId",lawyer.getId());
		query.setParameter("type", ProfileItem.FEEDBACK);
		Long count = (Long) query.getSingleResult();
		
		queryString = "select count(p.id) from ProfileItem p where p.lawyer.id = :lawyerId and p.type = :type and p.rating >= 5";
		query = db.getEntityManager().createQuery(queryString);
		query.setParameter("lawyerId",lawyer.getId());
		query.setParameter("type", ProfileItem.FEEDBACK);
		Long countPositive = (Long) query.getSingleResult();
		
		BigDecimal average= new BigDecimal("100");
		if( count == 0 ) return average;
		Double result = (countPositive.doubleValue() / count.doubleValue())*100;
		return new BigDecimal(result);
	}
}

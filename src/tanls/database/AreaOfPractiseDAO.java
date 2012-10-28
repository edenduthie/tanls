package tanls.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.AreaOfPractise;

@Service
public class AreaOfPractiseDAO 
{
    @Autowired Database db;
    
    public AreaOfPractise get(Integer id)
    {
    	return (AreaOfPractise) db.getNoNull(AreaOfPractise.class.getName(), "id", id);
    }
    
    public void put(AreaOfPractise areaOfPractice)
    {
    	db.persist(areaOfPractice);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(AreaOfPractise.class);
    }
    
    public void update(AreaOfPractise areaOfPractice)
    {
    	db.update(areaOfPractice);
    }
    
    public List<AreaOfPractise> getAll()
    {
    	return db.getAll(AreaOfPractise.class.getName());
    }
}

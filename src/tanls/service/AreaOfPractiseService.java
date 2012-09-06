package tanls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.database.AreaOfPractiseDAO;
import tanls.entity.AreaOfPractise;

@Service
public class AreaOfPractiseService
{
	@Autowired AreaOfPractiseDAO dao;
	
    public List<AreaOfPractise> getAll() {
    	return dao.getAll();
    }
}

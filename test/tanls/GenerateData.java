package tanls;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import tanls.database.AreaOfPractiseDAO;
import tanls.database.SuburbDAO;
import tanls.database.TanlsGrantedAuthorityDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.AreaOfPractise;
import tanls.entity.Suburb;
import tanls.entity.TanlsGrantedAuthority;
import tanls.exception.InvalidInputException;

public class GenerateData extends BaseTest
{
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired TanlsGrantedAuthorityDAO tanlsGrantedAuthorityDAO;
    @Autowired SuburbDAO suburbDAO;
    @Autowired AreaOfPractiseDAO areaOfPractiseDAO;

    
    public void createSuburbs()
    {
    	try
    	{
    	    BufferedReader fileReader = new BufferedReader(new FileReader(new File("data/suburbs.csv")));
    	    String line = fileReader.readLine();
    	    while( line != null )
    	    {
    	    	Suburb suburb = new Suburb(line.trim());
    	    	suburbDAO.put(suburb);
    	    	line = fileReader.readLine();
    	    }
    	}
    	catch( FileNotFoundException e )
    	{
    		e.printStackTrace();
    	} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    //@Test
    public void areasOfPractise()
    {
    	try
    	{
    	    BufferedReader fileReader = new BufferedReader(new FileReader(new File("data/areasofpractise.csv")));
    	    String line = fileReader.readLine();
    	    while( line != null )
    	    {
    	    	AreaOfPractise area = new AreaOfPractise();
    	    	area.setName(line.trim());
    	    	areaOfPractiseDAO.put(area);
    	    	line = fileReader.readLine();
    	    }
    	}
    	catch( FileNotFoundException e )
    	{
    		e.printStackTrace();
    	} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    
//    @Test
    public void createProductionData() throws IOException, InvalidInputException
    {
    	TanlsGrantedAuthority auth = Generator.tanlsGrantedAuthority();
    	auth.setAuthority(TanlsGrantedAuthority.ADMIN_ROLE);
    	tanlsGrantedAuthorityDAO.put(auth);
    	
//    	createSuburbs();
    }
}

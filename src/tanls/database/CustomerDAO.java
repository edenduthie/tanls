package tanls.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.Customer;

@Service
public class CustomerDAO 
{
    @Autowired Database db;
    
    public Customer get(Integer id)
    {
    	return (Customer) db.getNoNull(Customer.class.getName(), "id", id);
    }
    
    public void put(Customer customer)
    {
    	db.persist(customer);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(Customer.class);
    }
    
    public void update(Customer customer)
    {
    	db.update(customer);
    }
    
    public List<Customer> getAll()
    {
    	return db.getAll(Customer.class.getName());
    }
}

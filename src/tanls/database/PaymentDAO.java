package tanls.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.entity.Payment;

@Service
public class PaymentDAO 
{
    @Autowired Database db;
    
    public Payment get(Integer id)
    {
    	return (Payment) db.getNoNull(Payment.class.getName(), "id", id);
    }
    
    public void put(Payment payment)
    {
    	db.persist(payment);
    }
    
    public void deleteAll()
    {
    	db.deleteAll(Payment.class);
    }
    
    public void update(Payment payment)
    {
    	db.update(payment);
    }
    
    public List<Payment> getAll()
    {
    	return db.getAll(Payment.class.getName());
    }
}

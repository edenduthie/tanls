package tanls.service;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.Generator;
import tanls.database.AddressDAO;
import tanls.database.CustomerDAO;
import tanls.database.PaymentDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Address;
import tanls.entity.Customer;
import tanls.entity.Payment;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.exception.PaymentException;

public class PinServiceTest extends BaseTest
{
    @Autowired PinService pinService;
    
    @Autowired CustomerDAO customerDAO;
    @Autowired AddressDAO addressDAO;
    @Autowired PaymentDAO paymentDAO;
    @Autowired TanlsUserDAO tanlsUserDAO;
    
    @Test
    public void createCustomer() throws PaymentException, InvalidInputException
    {
    	Customer customer = Generator.customer();
    	Address address = Generator.address();
    	//addressDAO.put(address);
    	customer.setBillingAddress(address);
    	Payment payment = Generator.payment();
    	payment.setCustomer(customer);
    	payment.setAmount(new BigDecimal(100.52));
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
    	Customer result = pinService.createCustomer(payment);
    	Assert.assertNotNull(result.getCreditCardToken());
    	Assert.assertNotNull(result.getCreditCardType());
    	Assert.assertNotNull(result.getMaskedNumber());
    	Assert.assertNotNull(result.getCustomerId());
    	Assert.assertNotNull(result.getId());
    	
    	paymentDAO.deleteAll();
    	customerDAO.deleteAll();
    	addressDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
    
    @Test
    public void pay() throws PaymentException, InvalidInputException
    {
    	Customer customer = Generator.customer();
    	Address address = Generator.address();
    	//addressDAO.put(address);
    	customer.setBillingAddress(address);
    	Payment payment = Generator.payment();
    	payment.setCustomer(customer);
    	payment.setAmount(new BigDecimal(100.53));
    	TanlsUser user = Generator.tanlsUser();
    	tanlsUserDAO.put(user);
    	customer.setUser(user);
    	customerDAO.put(customer);
    	
    	pinService.createCustomer(payment);
    	payment = pinService.pay(payment);
    	Assert.assertNotNull(payment.getTransactionId());
    	Assert.assertNotNull(payment.getTime());
    	Assert.assertNotNull(payment.getId());
    	
    	paymentDAO.deleteAll();
    	customerDAO.deleteAll();
    	addressDAO.deleteAll();
    	tanlsUserDAO.deleteAll();
    }
}

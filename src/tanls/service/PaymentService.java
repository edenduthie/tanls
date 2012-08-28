package tanls.service;

import tanls.entity.Customer;
import tanls.entity.Payment;
import tanls.exception.InvalidInputException;
import tanls.exception.PaymentException;

public interface PaymentService 
{	
    public Payment pay(Payment payment) throws PaymentException;
    public Customer createCustomer(Payment payment) throws PaymentException, InvalidInputException;
}

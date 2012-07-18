package tanls.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.PaymentDTO;
import tanls.exception.InvalidInputException;

public class PaymentTest 
{
    @Test
    public void constructor()
    {
    	Payment payment = Generator.payment();
    	payment.setCustomer(Generator.customer());
    	payment.setFile(Generator.file());
    	payment.setId(666);
    	
    	Payment newPayment = new Payment(new PaymentDTO(payment));
    	Assert.assertEquals(newPayment.getId(),payment.getId());
    	Assert.assertEquals(newPayment.getCustomer().getName(),payment.getCustomer().getName());
    }
    
    @Test
    public void validate() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.validate();
    }
    
    @Test
    public void validateNullCreditCard() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setCreditCardNumber(null);
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateCreditCardZeroLength() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setCreditCardNumber("");
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateCreditCardNotANumber() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setCreditCardNumber("sdfsdf");
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateCreditCardTooShort() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setCreditCardNumber("234234");
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateCcvNull() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setCcv(null);
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateCcvWrongLengthLong() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setCcv("12345");
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateCcvWrongLengthShort() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setCcv("12");
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateCcvNotANumber() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setCcv("awefawe");
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateExpiryNull() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setExpiry(null);
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateExpiryTooLong() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setExpiry("10/20134");
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
    
    @Test
    public void validateExpiryTooShort() throws InvalidInputException
    {
    	Payment payment = Generator.payment();
    	payment.setExpiry("1/2013");
    	try { payment.validate(); Assert.fail(); } catch(InvalidInputException e) {}
    }
}

package tanls.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.entity.Payment;

public class PaymentDTOTest 
{
    @Test
    public void constructor()
    {
    	Payment payment = Generator.payment();
    	payment.setCustomer(Generator.customer());
    	payment.setFile(Generator.file());
    	payment.setId(666);
    	
    	PaymentDTO dto = new PaymentDTO(payment);
    	Assert.assertEquals(dto.getId(),payment.getId());
    	Assert.assertEquals(dto.getCustomer().getName(),payment.getCustomer().getName());
    }
}

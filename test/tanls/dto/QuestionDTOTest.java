package tanls.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.QuestionDTO;
import tanls.entity.Address;
import tanls.entity.AreaOfPractise;
import tanls.entity.Customer;
import tanls.entity.Question;

public class QuestionDTOTest 
{
    @Test
    public void constructor()
    {
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	Address address = Generator.address();
    	AreaOfPractise area = Generator.areaOfPractise();
    	
    	question.setCustomer(customer);
    	question.setAreaOfPractise(area);
    	customer.setBillingAddress(address);
    	
    	QuestionDTO dto = new QuestionDTO(question);
    	Assert.assertNotNull(dto.getCustomer());
    	Assert.assertNotNull(dto.getCustomer().getBillingAddress());
    	Assert.assertEquals(dto.getAreaOfPractise().getName(),area.getName());
    }
}

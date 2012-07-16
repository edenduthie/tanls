package tanls.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.QuestionDTO;

public class QuestionTest 
{
    @Test
    public void constructor()
    {
    	Question question = Generator.question();
    	Customer customer = Generator.customer();
    	Address address = Generator.address();
    	AreaOfPractise area = Generator.areaOfPractise();
    	
    	question.setCustomer(customer);
    	customer.setBillingAddress(address);
    	question.setAreaOfPractise(area);
    	
    	QuestionDTO dto = new QuestionDTO(question);
    	
    	Question result = new Question(dto);
    	Assert.assertNotNull(result.getCustomer());
    	Assert.assertNotNull(result.getCustomer().getBillingAddress());
    	Assert.assertEquals(result.getAreaOfPractise().getName(),area.getName());
    }
}

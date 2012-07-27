package tanls.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.entity.File;

public class FileDTOTest 
{
    @Test
    public void constructor()
    {
    	File file = Generator.file();
    	file.setId(666);
    	file.setQuote(Generator.quote());
    	file.setPayment(Generator.payment());
    	file.setCustomer(Generator.customer());
    	file.setLawyer(Generator.lawyer());
    	file.setFeedback(Generator.profileItem());
    	
    	FileDTO dto = new FileDTO(file);
    	Assert.assertEquals(dto.getId(),file.getId());
    	Assert.assertEquals(dto.getPayment().getId(),file.getPayment().getId());
    	Assert.assertEquals(dto.getQuote().getId(),file.getQuote().getId());
    	Assert.assertNotNull(dto.getCustomer());
    	Assert.assertNotNull(dto.getLawyer());
    	Assert.assertNotNull(dto.getFeedback());
    }
}

package tanls.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.FileDTO;

public class FileTest 
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
    	
    	File newFile = new File(new FileDTO(file));
    	Assert.assertEquals(newFile.getId(),file.getId());
    	Assert.assertEquals(newFile.getQuote().getId(),file.getQuote().getId());
    	Assert.assertEquals(newFile.getPayment().getId(),file.getPayment().getId());
    	Assert.assertNotNull(newFile.getCustomer());
    	Assert.assertNotNull(newFile.getLawyer());
    	Assert.assertNotNull(newFile.getFeedback());
    }
}

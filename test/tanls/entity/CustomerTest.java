package tanls.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.CustomerDTO;
import tanls.exception.InvalidInputException;

public class CustomerTest 
{
    @Test
    public void validate() throws InvalidInputException
    {
    	Customer customer = Generator.customer();
    	customer.validate();
    }
    
    @Test
    public void validateNameMissing()
    {
    	Customer customer = Generator.customer();
    	customer.setName(null);
    	try
    	{
    	    customer.validate();
    	    Assert.fail();
    	}
    	catch(InvalidInputException e ) {}
    }
    
    @Test
    public void validateNameZeroLength()
    {
    	Customer customer = Generator.customer();
    	customer.setName("  ");
    	try
    	{
    	    customer.validate();
    	    Assert.fail();
    	}
    	catch(InvalidInputException e ) {}
    }
    
    @Test
    public void validateNameTooLong()
    {
    	Customer customer = Generator.customer();
    	customer.setName("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum claritatem. Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant sollemnes in futurum.");
    	try
    	{
    	    customer.validate();
    	    Assert.fail();
    	}
    	catch(InvalidInputException e ) {}
    }
    
    @Test
    public void validateCompanyNameTooLong()
    {
    	Customer customer = Generator.customer();
    	customer.setCompanyName("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum claritatem. Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant sollemnes in futurum.");
    	try
    	{
    	    customer.validate();
    	    Assert.fail();
    	}
    	catch(InvalidInputException e ) {}
    }
    
    @Test
    public void constructor()
    {
    	Customer customer = Generator.customer();
    	customer.setBillingAddress(Generator.address());
    	customer.setUser(Generator.tanlsUser());
    	CustomerDTO dto = new CustomerDTO(customer);
    	Customer newCustomer = new Customer(dto);
    	Assert.assertNotNull(newCustomer.getBillingAddress());
    	Assert.assertNotNull(newCustomer.getUser());
    	Assert.assertEquals(newCustomer.getName(),customer.getName());
    }
}

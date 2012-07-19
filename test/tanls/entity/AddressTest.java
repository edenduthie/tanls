package tanls.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.exception.InvalidInputException;

public class AddressTest
{
	public static final String tooLong = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum claritatem. Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant sollemnes in futurum.";
	
    @Test
    public void testUpdate()
    {
    	Address address = Generator.address();
    	address.setId(123);
    	Address newAddress = new Address();
    	address.update(newAddress);
    	Assert.assertNull(address.getPostcode());
    	Assert.assertNull(address.getState());
    	Assert.assertEquals(address.getId().intValue(),123);
    }
    
    @Test
    public void validate() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.validate();
    }
    
    @Test
    public void validateNoName() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setName(null);
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateNameZeroLength() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setName("  ");
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateNameTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setName(generateLongString(201));
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateNoStreetAddress() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setStreetAddress(null);
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateStreetAddressZeroLength() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setStreetAddress("  ");
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateStreetAddressTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setStreetAddress(generateLongString(301));
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateNoSuburb() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setSuburb(null);
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateSuburbZeroLength() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setSuburb("  ");
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateSuburbTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setSuburb(generateLongString(201));
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateNoState() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setState(null);
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateStateZeroLength() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setState("  ");
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateStateTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setState(generateLongString(4));
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validateNoPostcode() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setPostcode(null);
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validatePostcodeZeroLength() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setPostcode("  ");
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    @Test
    public void validatePostcodeTooLong() throws InvalidInputException
    {
    	Address address = Generator.address();
    	address.setPostcode(generateLongString(5));
    	try{ address.validate(); Assert.fail(); } catch(InvalidInputException e){}
    }
    
    public String generateLongString(int length)
    {
    	StringBuffer stringBuffer = new StringBuffer();
    	for( int i=0; i < length; ++i ) stringBuffer.append("a");
    	return stringBuffer.toString();
    }
}

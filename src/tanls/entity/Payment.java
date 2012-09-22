package tanls.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;

import tanls.dto.PaymentDTO;
import tanls.exception.InvalidInputException;

@Entity
public class Payment 
{
	@Id @GeneratedValue
	private Integer id;
    @OneToOne(mappedBy="payment") private File file;
    private Long time;
    private BigDecimal amount;
    @Transient private String creditCardNumber;
    @Transient private String ccv;
    @Transient private String expiry;
    @Transient private Boolean useExistingCustomer;
    @Transient private Customer customer;
    private String transactionId;
    
    public Payment() {}
    
    public Payment(PaymentDTO payment)
    {
    	String[] ignore = {"customer"};
    	BeanUtils.copyProperties(payment, this, ignore);
    	if( payment.getCustomer() != null ) setCustomer(new Customer(payment.getCustomer()));
    }
    
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Transient 
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	@Transient 
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	@Transient 
	public String getCcv() {
		return ccv;
	}
	@Transient 
	public void setCcv(String ccv) {
		this.ccv = ccv;
	}
	@Transient 
	public String getExpiry() {
		return expiry;
	}
	@Transient
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public Boolean getUseExistingCustomer() {
		return useExistingCustomer;
	}
	public void setUseExistingCustomer(Boolean useExistingCustomer) {
		this.useExistingCustomer = useExistingCustomer;
	}
	@Transient
	public Customer getCustomer() {
		return customer;
	}
	@Transient
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

    @Transient
    public void validate() throws InvalidInputException
    {
    	if( getCreditCardNumber() == null || getCreditCardNumber().length() <= 0 ) throw new InvalidInputException("Credit card number is required");
    	try
    	{
    	    if( !checkCreditCard(getCreditCardNumber()) ) throw new InvalidInputException("Credit card number is invalid");
    	}
    	catch(NumberFormatException e)
    	{
    		throw new InvalidInputException("Credit card must be a number");
    	}
    	if( getCcv() == null || !(getCcv().length()==3 || getCcv().length()==4) ) throw new InvalidInputException("CCV is required and must be 3 or 4 digits");
    	try
    	{
    		Integer.parseInt(getCcv());
    	}
    	catch( NumberFormatException e )
    	{
    		throw new InvalidInputException("CCV must be a number");
    	}
    	if( getExpiry() == null || getExpiry().length() != 7 ) throw new InvalidInputException("Expiry date is required and must be 7 characters long");
    }
    
    public static boolean checkCreditCard(String ccNumber)
    {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--)
        {
                int n = Integer.parseInt(ccNumber.substring(i, i + 1));
                if (alternate)
                {
                        n *= 2;
                        if (n > 9)
                        {
                                n = (n % 10) + 1;
                        }
                }
                sum += n;
                alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}

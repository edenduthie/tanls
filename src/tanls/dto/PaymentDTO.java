package tanls.dto;

import java.math.BigDecimal;

import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;

import tanls.entity.Payment;

public class PaymentDTO 
{
	private Integer id;
    private Long time;
    private BigDecimal amount;
    private String creditCardNumber;
    private String ccv;
    private String expiry;
    private Boolean useExistingCustomer;
    private String transactionId;
    private CustomerDTO customer;
    
    public PaymentDTO() {}
    
    public PaymentDTO(Payment payment)
    {
    	String[] ignore = {"customer","creditCardNumber","ccv","expiry"};
    	BeanUtils.copyProperties(payment, this, ignore);
    	if( payment.getCustomer() != null ) setCustomer(new CustomerDTO(payment.getCustomer()));
    }
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Boolean getUseExistingCustomer() {
		return useExistingCustomer;
	}
	public void setUseExistingCustomer(Boolean useExistingCustomer) {
		this.useExistingCustomer = useExistingCustomer;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getCcv() {
		return ccv;
	}

	public void setCcv(String ccv) {
		this.ccv = ccv;
	}

	public String getExpiry() {
		return expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
}

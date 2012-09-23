package tanls.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;

import tanls.dto.CustomerDTO;
import tanls.exception.InvalidInputException;

@Entity
public class Customer implements Serializable
{
	private static final long serialVersionUID = -527712670786677837L;

	public static final Integer MAX_LENGTH = 200;
	
	@Id @GeneratedValue
	private Integer id;
	@OneToOne private TanlsUser user;
	private String name;
	private String companyName;
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL) private Address billingAddress;
	private Integer numberOfQuestionsAsked = 0;
	private Boolean emailNotifications;
	private String creditCardToken;
	private String customerId;
	private String maskedNumber;
	private String creditCardType;
	
	public Customer(CustomerDTO customer) 
	{
		String[] ignore = {"billingAddress","user","creditCardToken","customerId"};
		BeanUtils.copyProperties(customer, this, ignore);
		if( customer.getBillingAddress() != null ) setBillingAddress(new Address(customer.getBillingAddress()));
		if( customer.getUser() != null ) setUser(new TanlsUser(customer.getUser()));
	}
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public TanlsUser getUser() {
		return user;
	}
	public void setUser(TanlsUser user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Address getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}
	public Integer getNumberOfQuestionsAsked() {
		return numberOfQuestionsAsked;
	}
	public void setNumberOfQuestionsAsked(Integer numberOfQuestionsAsked) {
		this.numberOfQuestionsAsked = numberOfQuestionsAsked;
	}
	
	@Transient
	public void validate() throws InvalidInputException {
		if( getName() == null || getName().trim().length() <= 0  ) throw new InvalidInputException("Your name is required");
		if( getName().length() > MAX_LENGTH ) throw new InvalidInputException("Your name cannot be longer than " + MAX_LENGTH + " characters");
		if( getCompanyName() != null && getCompanyName().length() > MAX_LENGTH ) throw new InvalidInputException("Your company name cannot be longer than " + MAX_LENGTH + " characters");
	}
	public Boolean getEmailNotifications() {
		return emailNotifications;
	}
	public void setEmailNotifications(Boolean emailNotifications) {
		this.emailNotifications = emailNotifications;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCreditCardToken() {
		return creditCardToken;
	}
	public void setCreditCardToken(String creditCardToken) {
		this.creditCardToken = creditCardToken;
	}
	public String getMaskedNumber() {
		return maskedNumber;
	}
	public void setMaskedNumber(String maskedNumber) {
		this.maskedNumber = maskedNumber;
	}
	public String getCreditCardType() {
		return creditCardType;
	}
	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}
	
}

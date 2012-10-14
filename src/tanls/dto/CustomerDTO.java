package tanls.dto;

import org.springframework.beans.BeanUtils;

import tanls.entity.Customer;

public class CustomerDTO extends ResponseDTO
{
	private Integer id;
	private TanlsUserDTO user;
	private String name;
	private String companyName;
	private AddressDTO billingAddress;
	private Integer numberOfQuestionsAsked;
	private Boolean emailNotifications;
	private String maskedNumber;
	private String creditCardType;
	private String abn;
	private String bsb;
	private String accountNumber;
	private Boolean gst;
	
	public CustomerDTO() {super();};
	
	public CustomerDTO(String message, Boolean error)
	{
		super(message, error);
	}
	
	public CustomerDTO(String message, Boolean error, Boolean redirectToLogin)
	{
		super(message, error, redirectToLogin);
	}
	
	public CustomerDTO(Customer customer) 
	{
		String[] ignore = {"billingAddress","user","creditCardToken","customerId"};
		BeanUtils.copyProperties(customer, this, ignore);
		if( customer.getBillingAddress() != null ) setBillingAddress(new AddressDTO(customer.getBillingAddress()));
		if( customer.getUser() != null ) setUser(new TanlsUserDTO(customer.getUser()));
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public TanlsUserDTO getUser() {
		return user;
	}
	public void setUser(TanlsUserDTO user) {
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
	public AddressDTO getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(AddressDTO billingAddress) {
		this.billingAddress = billingAddress;
	}
	public Integer getNumberOfQuestionsAsked() {
		return numberOfQuestionsAsked;
	}
	public void setNumberOfQuestionsAsked(Integer numberOfQuestionsAsked) {
		this.numberOfQuestionsAsked = numberOfQuestionsAsked;
	}

	public Boolean getEmailNotifications() {
		return emailNotifications;
	}

	public void setEmailNotifications(Boolean emailNotifications) {
		this.emailNotifications = emailNotifications;
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

	public String getAbn() {
		return abn;
	}

	public void setAbn(String abn) {
		this.abn = abn;
	}

	public String getBsb() {
		return bsb;
	}

	public void setBsb(String bsb) {
		this.bsb = bsb;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Boolean getGst() {
		return gst;
	}

	public void setGst(Boolean gst) {
		this.gst = gst;
	}
}

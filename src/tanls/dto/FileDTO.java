package tanls.dto;

import org.springframework.beans.BeanUtils;

import tanls.entity.File;

public class FileDTO extends ResponseDTO
{
	private Integer id;
	private QuoteDTO quote;
	private Long time;
	private PaymentDTO payment;
	private String status;
	private CustomerDTO customer;
	private LawyerDTO lawyer;
	private ProfileItemDTO feedback;
	private Boolean holdPayment;
	
	public FileDTO(File file)
	{
		String[] ignore = {"quote","payment","customer","lawyer","feedback"};
		BeanUtils.copyProperties(file, this, ignore);
		if( file.getQuote() != null ) setQuote(new QuoteDTO(file.getQuote()));
		if( file.getPayment() != null ) setPayment(new PaymentDTO(file.getPayment()));
		if( file.getCustomer() != null ) setCustomer(new CustomerDTO(file.getCustomer()));
		if( file.getLawyer() != null ) setLawyer(new LawyerDTO(file.getLawyer()));
		if( file.getFeedback() != null ) setFeedback(new ProfileItemDTO(file.getFeedback()));
	}
	
	public FileDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public FileDTO(String message, boolean success, boolean redirectToLogin) {
		super(message, success, redirectToLogin);
		// TODO Auto-generated constructor stub
	}
	public FileDTO(String message, boolean success) {
		super(message, success);
		// TODO Auto-generated constructor stub
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
	public String getStatus() {
		return status;
	}
	public QuoteDTO getQuote() {
		return quote;
	}
	public void setQuote(QuoteDTO quote) {
		this.quote = quote;
	}
	public PaymentDTO getPayment() {
		return payment;
	}
	public void setPayment(PaymentDTO payment) {
		this.payment = payment;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public LawyerDTO getLawyer() {
		return lawyer;
	}

	public void setLawyer(LawyerDTO lawyer) {
		this.lawyer = lawyer;
	}

	public ProfileItemDTO getFeedback() {
		return feedback;
	}

	public void setFeedback(ProfileItemDTO feedback) {
		this.feedback = feedback;
	}

	public Boolean getHoldPayment() {
		return holdPayment;
	}

	public void setHoldPayment(Boolean holdPayment) {
		this.holdPayment = holdPayment;
	}
}

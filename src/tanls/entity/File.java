package tanls.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.beans.BeanUtils;

import tanls.dto.FileDTO;

@Entity
public class File 
{
	public static final String STATUS_IN_PROGRESS = "IN PROGRESS";
	public static final String STATUS_COMPLETE = "COMPLETE PENDING FEEDBACK";
	public static final String FEEDBACK_RECEIVED = "FEEDBACK RECEIVED";
	
	@Id @GeneratedValue
	private Integer id;
	@OneToOne private Quote quote;
	private Long time;
	@OneToOne private Payment payment;
	private String status;
	@ManyToOne Customer customer;
	@ManyToOne Lawyer lawyer;
	private Long completionTime;
	@ManyToOne private ProfileItem feedback;
	private Boolean holdPayment;
	
	public File() {}
	
	public File(FileDTO file)
	{
		String[] ignore = {"quote","payment","customer","lawyer","feedback"};
		BeanUtils.copyProperties(file, this, ignore);
		if( file.getQuote() != null ) setQuote(new Quote(file.getQuote()));
		if( file.getPayment() != null ) setPayment(new Payment(file.getPayment()));
		if( file.getCustomer() != null ) setCustomer(new Customer(file.getCustomer()));
		if( file.getLawyer() != null ) setLawyer(new Lawyer(file.getLawyer()));
		if( file.getFeedback() != null ) setFeedback(new ProfileItem(file.getFeedback()));
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Quote getQuote() {
		return quote;
	}
	public void setQuote(Quote quote) {
		this.quote = quote;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public static String getStatusNew() {
		return STATUS_IN_PROGRESS;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Lawyer getLawyer() {
		return lawyer;
	}

	public void setLawyer(Lawyer lawyer) {
		this.lawyer = lawyer;
	}

	public Long getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Long completionTime) {
		this.completionTime = completionTime;
	}

	public ProfileItem getFeedback() {
		return feedback;
	}

	public void setFeedback(ProfileItem feedback) {
		this.feedback = feedback;
	}

	public Boolean getHoldPayment() {
		return holdPayment;
	}

	public void setHoldPayment(Boolean holdPayment) {
		this.holdPayment = holdPayment;
	}
}

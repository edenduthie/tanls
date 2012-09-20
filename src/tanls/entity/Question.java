package tanls.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;

import tanls.dto.QuestionDTO;

@Entity
public class Question 
{
	@Id @GeneratedValue
	private Integer id;
	@Column(columnDefinition="TEXT") private String text;
	private Boolean businessQuestion;
	@ManyToOne private Customer customer;
	private Long time;
	@ManyToOne private AreaOfPractise areaOfPractise;
	@Transient private Long answers;
	@Transient private Long useful;
	@Transient private Long notUseful;
	@Transient private Answer answer;
	private Boolean quoteRequired;
	private Boolean proBono;
	
	public Question() {}
	
	public Question(QuestionDTO question)
	{
		String[] ignore = {"customer","areaOfPractise","answer"};
		BeanUtils.copyProperties(question, this, ignore);
		if( question.getCustomer() != null ) setCustomer(new Customer(question.getCustomer()));
		if( question.getAreaOfPractise() != null ) setAreaOfPractise(new AreaOfPractise(question.getAreaOfPractise()));
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Boolean getBusinessQuestion() {
		return businessQuestion;
	}
	public void setBusinessQuestion(Boolean businessQuestion) {
		this.businessQuestion = businessQuestion;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public AreaOfPractise getAreaOfPractise() {
		return areaOfPractise;
	}

	public void setAreaOfPractise(AreaOfPractise areaOfPractise) {
		this.areaOfPractise = areaOfPractise;
	}
	@Transient public Long getAnswers() {
		return answers;
	}

	@Transient public void setAnswers(Long answers) {
		this.answers = answers;
	}

	@Transient public Long getUseful() {
		return useful;
	}

	@Transient public void setUseful(Long useful) {
		this.useful = useful;
	}

	@Transient public Long getNotUseful() {
		return notUseful;
	}

	@Transient public void setNotUseful(Long notUseful) {
		this.notUseful = notUseful;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public Boolean getQuoteRequired() {
		return quoteRequired;
	}

	public void setQuoteRequired(Boolean quoteRequired) {
		this.quoteRequired = quoteRequired;
	}

	public Boolean getProBono() {
		return proBono;
	}

	public void setProBono(Boolean proBono) {
		this.proBono = proBono;
	}
}

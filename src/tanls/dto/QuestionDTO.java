package tanls.dto;

import javax.persistence.ManyToOne;

import org.springframework.beans.BeanUtils;

import tanls.entity.Answer;
import tanls.entity.Question;


public class QuestionDTO extends ResponseDTO
{
	private Integer id;
	private String text;
	private Boolean businessQuestion;
	private CustomerDTO customer;
	private Long time;
	@ManyToOne private AreaOfPractiseDTO areaOfPractise;
	private Long answers;
	private Long useful;
	private Long notUseful;
	private AnswerDTO answer;
	private Boolean quoteRequired;
	private Boolean proBono;
	
	public QuestionDTO() {
		super();
	}
	
	public QuestionDTO(String message, Boolean error)
	{
		super(message,error);
	}
	
	public QuestionDTO(String message, Boolean error, Boolean redirectToLogin)
	{
		super(message,error,redirectToLogin);
	}
	
	public QuestionDTO(Question question)
	{
		String[] ignore = {"customer","areaOfPractise","answer"};
		BeanUtils.copyProperties(question, this, ignore);
		if( question.getCustomer() != null ) setCustomer(new CustomerDTO(question.getCustomer()));
		if( question.getAreaOfPractise() != null ) setAreaOfPractise(new AreaOfPractiseDTO(question.getAreaOfPractise()));
		if( question.getAnswer() != null ) setAnswer(new AnswerDTO(question.getAnswer()));
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
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public AreaOfPractiseDTO getAreaOfPractise() {
		return areaOfPractise;
	}

	public void setAreaOfPractise(AreaOfPractiseDTO areaOfPractise) {
		this.areaOfPractise = areaOfPractise;
	}

	public Long getAnswers() {
		return answers;
	}

	public void setAnswers(Long answers) {
		this.answers = answers;
	}

	public Long getUseful() {
		return useful;
	}

	public void setUseful(Long useful) {
		this.useful = useful;
	}

	public Long getNotUseful() {
		return notUseful;
	}

	public void setNotUseful(Long notUseful) {
		this.notUseful = notUseful;
	}

	public AnswerDTO getAnswer() {
		return answer;
	}

	public void setAnswer(AnswerDTO answer) {
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

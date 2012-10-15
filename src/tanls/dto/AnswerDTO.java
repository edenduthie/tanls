package tanls.dto;

import org.springframework.beans.BeanUtils;

import tanls.entity.Answer;

public class AnswerDTO extends ResponseDTO
{
	private Integer id;
	private QuestionDTO question;
	private LawyerDTO lawyer;
	private String text;
	private String status;
	private Long time;
	private QuoteDTO quote;
	
	public AnswerDTO(Answer answer)
	{
		super();
		String[] skip = {"question","lawyer","quote"};
		BeanUtils.copyProperties(answer,this,skip);
		if( answer.getLawyer() != null ) setLawyer(new LawyerDTO(answer.getLawyer()));
		if( answer.getQuestion() != null ) setQuestion(new QuestionDTO(answer.getQuestion()));
		if( answer.getQuote() != null ) setQuote(new QuoteDTO(answer.getQuote()));
	}
	
	
	public AnswerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AnswerDTO(String message, boolean success, boolean redirectToLogin) {
		super(message, success, redirectToLogin);
		// TODO Auto-generated constructor stub
	}
	public AnswerDTO(String message, boolean success) {
		super(message, success);
		// TODO Auto-generated constructor stub
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public QuestionDTO getQuestion() {
		return question;
	}
	public void setQuestion(QuestionDTO question) {
		this.question = question;
	}
	public LawyerDTO getLawyer() {
		return lawyer;
	}
	public void setLawyer(LawyerDTO lawyer) {
		this.lawyer = lawyer;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


	public Long getTime() {
		return time;
	}


	public void setTime(Long time) {
		this.time = time;
	}


	public QuoteDTO getQuote() {
		return quote;
	}


	public void setQuote(QuoteDTO quote) {
		this.quote = quote;
	}
}

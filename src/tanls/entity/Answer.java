package tanls.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.beans.BeanUtils;

import tanls.dto.AnswerDTO;

@Entity
public class Answer 
{
	public static final String STATUS_NEW = "NEW";
	public static final String STATUS_USEFUL = "USEFUL";
	public static final String STATUS_NOT_USEFUL = "NOT_USEFUL";
	
	@Id @GeneratedValue
	private Integer id;
	@ManyToOne private Question question;
	@ManyToOne private Lawyer lawyer;
	@Column(columnDefinition="TEXT") private String text;
	private String status;
	private Long time;
	@OneToOne private Quote quote;
	
	public Answer() {}
	
	public Answer(AnswerDTO answer)
	{
		String[] skip = {"question","lawyer","quote"};
		BeanUtils.copyProperties(answer, this, skip);
		if( answer.getLawyer() != null ) setLawyer(new Lawyer(answer.getLawyer()));
		if( answer.getQuestion() != null ) setQuestion(new Question(answer.getQuestion()));
		if( answer.getQuote() != null ) setQuote(new Quote(answer.getQuote()));
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public Lawyer getLawyer() {
		return lawyer;
	}
	public void setLawyer(Lawyer lawyer) {
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

	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}
}

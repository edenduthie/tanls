package tanls.dto;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.beans.BeanUtils;

import tanls.entity.Quote;

public class QuoteDTO extends ResponseDTO
{
	private Integer id;
	private QuestionDTO question;
	private LawyerDTO lawyer;
	private String text;
	private Long time;
	private BigDecimal legalFees;
	private BigDecimal disbursments;
	private FileDTO file;
	
	public QuoteDTO(Quote quote)
	{
		String[] ignore = {"question","lawyer","answer","file"};
		BeanUtils.copyProperties(quote, this, ignore);
		if( quote.getQuestion() != null ) setQuestion(new QuestionDTO(quote.getQuestion()));
		if( quote.getLawyer() != null ) setLawyer(new LawyerDTO(quote.getLawyer()));
		if( quote.getFile() != null ) 
		{
			Quote existingQuote = quote.getFile().getQuote();
			quote.getFile().setQuote(null); // avoid circular dependencies
			setFile(new FileDTO(quote.getFile()));
			quote.getFile().setQuote(existingQuote);
		}
	}
	
	public QuoteDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuoteDTO(String message, boolean success, boolean redirectToLogin) {
		super(message, success, redirectToLogin);
		// TODO Auto-generated constructor stub
	}

	public QuoteDTO(String message, boolean success) {
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

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public BigDecimal getLegalFees() {
		return legalFees;
	}

	public void setLegalFees(BigDecimal legalFees) {
		this.legalFees = legalFees;
	}

	public BigDecimal getDisbursments() {
		return disbursments;
	}

	public void setDisbursments(BigDecimal disbursments) {
		this.disbursments = disbursments;
	}

	public FileDTO getFile() {
		return file;
	}

	public void setFile(FileDTO file) {
		this.file = file;
	}

}

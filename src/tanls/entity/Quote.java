package tanls.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;

import tanls.dto.FileDTO;
import tanls.dto.QuoteDTO;

@Entity
public class Quote
{
	public static final BigDecimal LEGAL_FEE_COMISSION = new BigDecimal(0.15);
	public static final BigDecimal CREDIT_CARD_FEE = new BigDecimal(0.03);
	
	@Id @GeneratedValue
	private Integer id;
	@ManyToOne private Question question;
	@ManyToOne private Lawyer lawyer;
	@Column(columnDefinition="TEXT") private String text;
	private Long time;
	private BigDecimal legalFees;
	private BigDecimal disbursments;
	@OneToOne(mappedBy="quote") private Answer answer;
	@OneToOne(mappedBy="quote") private File file;
	
	public Quote() {}
	
	public Quote(QuoteDTO quote)
	{
		String[] ignore = {"question","lawyer","answer","file"};
		BeanUtils.copyProperties(quote, this, ignore);
		if( quote.getQuestion() != null ) setQuestion(new Question(quote.getQuestion()));
		if( quote.getLawyer() != null ) setLawyer(new Lawyer(quote.getLawyer()));
		if( quote.getFile() != null ) 
		{
			QuoteDTO existingQuote = quote.getFile().getQuote();
			quote.getFile().setQuote(null); // avoid circular dependencies
			setFile(new File(quote.getFile()));
			quote.getFile().setQuote(existingQuote);
		}
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

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	@Transient
	public BigDecimal getAmount() {
        BigDecimal amount = getLegalFees();
        if( amount == null ) amount = getDisbursments();
        else if( getDisbursments() != null ) amount = amount.add(getDisbursments());
        return amount;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Transient
	public BigDecimal getLegalFeesMinusComission() 
	{
		if( getLegalFees() == null ) return null;
		BigDecimal comission = getLegalFees().multiply(LEGAL_FEE_COMISSION);
		return getLegalFees().subtract(comission);
	}

	@Transient
	public BigDecimal getDisbursmentsMinusCreditCard()
	{
		if( getDisbursments() == null ) return null;
		BigDecimal creditCardFee = getDisbursments().multiply(CREDIT_CARD_FEE);
		return getDisbursments().subtract(creditCardFee);
	}
	
	@Transient
	public BigDecimal getAmountMinusFees()
	{
        BigDecimal amount = getLegalFeesMinusComission();
        if( amount == null ) amount = getDisbursmentsMinusCreditCard();
        else if( getDisbursmentsMinusCreditCard() != null ) amount = amount.add(getDisbursmentsMinusCreditCard());
        return amount;
	}
}

package tanls.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import tanls.database.CustomerDAO;
import tanls.database.FileDAO;
import tanls.database.LawyerDAO;
import tanls.database.ProfileItemDAO;
import tanls.database.QuoteDAO;
import tanls.database.TanlsUserDAO;
import tanls.entity.Customer;
import tanls.entity.File;
import tanls.entity.Lawyer;
import tanls.entity.ProfileItem;
import tanls.entity.Quote;
import tanls.entity.TanlsUser;
import tanls.exception.InvalidInputException;
import tanls.exception.PaymentException;

public class FileService 
{
	public static final Logger log = Logger.getLogger(FileService.class);
	
    @Autowired FileDAO fileDAO;
    @Autowired QuoteDAO quoteDAO;
    PaymentService paymentService;
    @Autowired TanlsUserDAO tanlsUserDAO;
    @Autowired CustomerDAO customerDAO;
    @Autowired MailService mailService;
    @Autowired LawyerDAO lawyerDAO;
    @Autowired ProfileItemDAO profileItemDAO;
    
    private Boolean easyLawGST;
    
    @Transactional(isolation=Isolation.SERIALIZABLE,rollbackFor={InvalidInputException.class,PaymentException.class})
    public File create(File file, TanlsUser user) throws InvalidInputException, PaymentException
    {
    	// load quote, set lawyer and customer from quote
    	if( file.getQuote() == null ) throw new InvalidInputException("A quote is required to book a lawyer");
    	Quote quote = quoteDAO.getWithQuestionCustomerAndLawyer(file.getQuote().getId());
    	if( quote == null ) throw new InvalidInputException("Quote not found");
    	Customer customer = quote.getQuestion().getCustomer();
    	if( customer == null ) throw new InvalidInputException("Customer not found");
    	if( !customer.getUser().getId().equals(user.getId()) ) throw new InvalidInputException("Attempt to book another user's quote");
    	file.setQuote(quote);
    	
    	File existingFile = fileDAO.getByQuote(quote.getId());
    	if( existingFile != null ) throw new InvalidInputException("You have already made a booking for the given quote");
    	
    	
    	if( quote.getQuestion().getProBono() == null || !quote.getQuestion().getProBono() )
    	{
	        updateBillingAddress(file,customer);
	    	file.getPayment().setCustomer(customer);    	
	    	
	    	file.getPayment().setAmount(quote.getAmount());
	    	if( file.getPayment().getUseExistingCustomer() == null || !file.getPayment().getUseExistingCustomer() ) paymentService.createCustomer(file.getPayment());
	    	paymentService.pay(file.getPayment());
    	}
    	else
    	{
    		file.setPayment(null);
    	}
    	
    	file.setTime(Calendar.getInstance().getTimeInMillis());
    	file.setStatus(File.STATUS_IN_PROGRESS);
    	file.setCustomer(quote.getQuestion().getCustomer());
    	file.setLawyer(quote.getLawyer());
    	fileDAO.put(file);
    	
    	SendBookingConfirmations thread = new SendBookingConfirmations(file);
    	thread.start();
    	
    	return file;
    }
    
    public void updateBillingAddress(File file, Customer customer) throws InvalidInputException 
    {
    	if( file.getPayment().getUseExistingCustomer() == null || !file.getPayment().getUseExistingCustomer() )
    	{
    		if( file.getPayment().getCustomer() == null || file.getPayment().getCustomer().getBillingAddress() == null ) 
    	    {
    			throw new InvalidInputException("Please provide an address");
    	    }
    		file.getPayment().getCustomer().getBillingAddress().validate();
    		if( customer.getBillingAddress() == null ) 
    		{
    			customer.setBillingAddress(file.getPayment().getCustomer().getBillingAddress());
    		}
    		else
    		{
    			customer.getBillingAddress().update(file.getPayment().getCustomer().getBillingAddress());
    		}
    		customerDAO.update(customer);
    	}
    }
    
    public class SendBookingConfirmations extends Thread
    {
    	private File file;
    	
    	public SendBookingConfirmations(File file)
    	{
    		this.file = file;
    	}
    	
    	@Override
    	public void run()
    	{
    		try 
    		{
				sendEmailToCustomer(file);
			} 
    		catch (AddressException e) 
    		{
				log.error(e);
			} 
    		catch (MessagingException e) 
    		{
				log.error(e);
			}
    		try 
    		{
				sendEmailToLawyer(file);
			} 
    		catch (AddressException e) 
    		{
				log.error(e);
			} 
    		catch (MessagingException e) 
    		{
				log.error(e);
			}
    	}
    }
    
    public void sendEmailToCustomer(File file) throws AddressException, MessagingException
    {
    	String subject = "Easy Law booking confirmation";
		StringBuffer s = new StringBuffer();
		s.append("<p>Hi ");
		s.append(file.getCustomer().getName());
		s.append(",</p>");
		s.append("<p>Congratulations, your Easy Law booking has been confirmed!</p>");
		s.append("<h3>Booking Details</h3>");
		s.append("<p><strong>Lawyer: </strong>");
		s.append(file.getLawyer().getName());
		s.append("</p>");
		s.append("<p><strong>Question: </strong>");
		s.append(file.getQuote().getQuestion().getText());
		s.append("</p>");
		s.append("<p><strong>Description of legal services: </strong>");
		s.append(file.getQuote().getText());
		s.append("</p>");
		if( file.getQuote().getQuestion().getProBono() == null ||  !file.getQuote().getQuestion().getProBono() )
		{
			s.append("<table>");
			s.append("<tr>");
			s.append("<td>Legal Fees:</td>");
			s.append("<td>");
			s.append(file.getQuote().getLegalFees());
			s.append("</td>");
			s.append("</tr>");
			if( file.getQuote().getDisbursments() != null )
			{
				s.append("<tr>");
				s.append("<td>Disbursments:</td>");
				s.append("<td>");
				s.append(file.getQuote().getDisbursments());
				s.append("</td>");
				s.append("</tr>");
			}
			s.append("<tr>");
			s.append("<td>GST paid:</td>");
			s.append("<td>");
			BigDecimal gst = getLegalFeeGST(file.getQuote(),file.getLawyer().getGst());
			gst = gst.setScale(2, RoundingMode.HALF_EVEN);
			s.append(gst.toPlainString());
			s.append("</td>");
			s.append("</tr>");
			s.append("<tr>");
			s.append("<td>Total:</td>");
			s.append("<td>");
			s.append(file.getQuote().getAmount());
			s.append("</td>");
			s.append("</tr>");
			s.append("</table>");
		}
		s.append("<p>Your lawyer will contact you shortly to commence the legal work.</p>");
		s.append("<p>You can view the details of your booking on the <a href='");
		s.append(mailService.getBaseUrl());
		s.append("/#!/customer/files/");
		s.append(file.getId());
		s.append("'>web interface</a>.</p>");
		s.append("<p>Thanks for using Easy Law!</p>");
		s.append("<p>Regards,</p>");
		s.append("<p>Easy Law</p>");
		s.append("<p>Easy Law - Problem Solved Pty. Ltd., ABN: 54162874446<p>");
		if( file.getLawyer().getAbn() != null && file.getLawyer().getAbn().trim().length() > 0 )
		{
			s.append("<p>");
			s.append(file.getLawyer().getName());
			if(file.getLawyer().getFirmName() != null && file.getLawyer().getFirmName().trim().length() > 0 )
			{
				s.append(" - ");
				s.append(file.getLawyer().getFirmName());
			}
			s.append(", ABN: ");
			s.append(file.getLawyer().getAbn());
			s.append("<p>");
		}
		
		mailService.sendMessageHTML(file.getCustomer().getUser().getEmail(), subject, s.toString());
    }
    
    public BigDecimal getLegalFeeGST(Quote quote, Boolean isLawyerRegistered)
    {
    	BigDecimal fees = quote.getLegalFees();
    	if( fees == null || fees.intValue() <= 0 ) return new BigDecimal(0);
    	BigDecimal lawyerProfit = quote.getLegalFeesMinusComission();
    	BigDecimal easyLawProfit = fees.subtract(lawyerProfit);
    	BigDecimal gst = new BigDecimal(0);
    	if( getEasyLawGST() ) gst = gst.add(easyLawProfit.multiply(new BigDecimal(0.1)));
    	if( isLawyerRegistered == null || isLawyerRegistered ) gst = gst.add(lawyerProfit.multiply(new BigDecimal(0.1)));
    	return gst;
    }
    
    public void sendEmailToLawyer(File file) throws AddressException, MessagingException
    {
    	Lawyer lawyer = lawyerDAO.getWithUser(file.getLawyer().getId());
    	
    	String subject = "Easy Law booking received";
		StringBuffer s = new StringBuffer();
		s.append("<p>Hi ");
		s.append(file.getCustomer().getName());
		s.append(",</p>");
		s.append("<p>Congratulations, you have been booked through Easy Law!</p>");
		s.append("<h3>Booking Details</h3>");
		s.append("<p><strong>Customer: </strong>");
		s.append(file.getCustomer().getName());
		s.append("</p>");
		s.append("<p><strong>Customer Contact Email: </strong>");
		s.append(file.getCustomer().getUser().getEmail());
		s.append("</p>");
		s.append("<p><strong>Question: </strong>");
		s.append(file.getQuote().getQuestion().getText());
		s.append("</p>");
		s.append("<p><strong>Description of legal services: </strong>");
		s.append(file.getQuote().getText());
		s.append("</p>");
		if( file.getQuote().getQuestion().getProBono() == null ||  !file.getQuote().getQuestion().getProBono() )
		{
			s.append("<table>");
			s.append("<tr>");
			s.append("<td>Legal Fees:</td>");
			s.append("<td>");
			s.append(file.getQuote().getLegalFees());
			s.append("</td>");
			s.append("</tr>");
			if( file.getQuote().getDisbursments() != null )
			{
				s.append("<tr>");
				s.append("<td>Disbursments:</td>");
				s.append("<td>");
				s.append(file.getQuote().getDisbursments());
				s.append("</td>");
				s.append("</tr>");
			}
			s.append("<tr>");
			s.append("<td>Total:</td>");
			s.append("<td>");
			s.append(file.getQuote().getAmount());
			s.append("</td>");
			s.append("</tr>");
		    s.append("</table>");
		}
		s.append("<p>Please contact your customer as soon as possible at <a href='mailto:");
		s.append(file.getCustomer().getUser().getEmail());
		s.append("'>");
		s.append(file.getCustomer().getUser().getEmail());
		s.append("</a> to commence the legal work.</p>");
		s.append("<p>You can view the details of your booking on the <a href='");
		s.append(mailService.getBaseUrl());
		s.append("/#!/customer/files/");
		s.append(file.getId());
		s.append("'>web interface</a>. Once you have completed the work, use the web interface to mark it as complete and receive payment.</p>");
		s.append("<p>Thanks for using Easy Law!</p>");
		s.append("<p>Regards,</p>");
		s.append("<p>Easy Law</p>");
		s.append("<p>Easy Law - Problem Solved Pty. Ltd., ABN: 54162874446<p>");
		
		mailService.sendMessageHTML(lawyer.getUser().getEmail(), subject, s.toString());
    }
    
    public List<File> getCustomer(Integer limit, Integer offset, TanlsUser user)
    {
    	return fileDAO.getCustomer(limit, offset, user);
    }
    
    public List<File> getLawyer(Integer limit, Integer offset, TanlsUser user)
    {
    	return fileDAO.getLawyer(limit, offset, user);
    }
    
    @Transactional(isolation=Isolation.SERIALIZABLE,rollbackFor=Exception.class)
    public ProfileItem submitFeedback(Integer fileId, ProfileItem feedback, TanlsUser user) throws InvalidInputException
    {
    	feedback.setStatus(ProfileItem.NEW_STATUS);
    	feedback.setType(ProfileItem.FEEDBACK);
    	
    	File file = fileDAO.getWithPaymentAndQuote(fileId);
    	if( file == null ) throw new InvalidInputException("File not found");
    	
    	if( !file.getCustomer().getUser().getId().equals(user.getId()) ) throw new InvalidInputException("You are not the customer of this file, you cannot leave feedback");
    	if( file.getFeedback() != null ) throw new InvalidInputException("Feedback has already been left for this file");
    	
    	validate(feedback);
    	feedback.setCustomer(file.getCustomer());
    	feedback.setLawyer(file.getLawyer());
    	profileItemDAO.put(feedback);
    	
    	file.setFeedback(feedback);
    	file.setStatus(File.FEEDBACK_RECEIVED);
    	fileDAO.update(file);
    	
    	file.getLawyer().setPercentagePositive(profileItemDAO.getPercentagePositive(file.getLawyer()));
    	if( feedback.getRating() >= 5 ) {
    		if( file.getLawyer().getPoints() == null ) file.getLawyer().setPoints(0);
    		file.getLawyer().setPoints(file.getLawyer().getPoints() + feedback.getRating());
    	}
    	lawyerDAO.update(file.getLawyer());
    	
    	SendFeedback thread = new SendFeedback(file);
    	thread.start();
    	
    	return feedback;
    }
    
    public void validate(ProfileItem profileItem) throws InvalidInputException
    {
    	if( profileItem.getText() == null || profileItem.getText().trim().length() <= 0 ) throw new InvalidInputException("Please provide feedback text");
    	if( profileItem.getText().length() > 700 ) throw new InvalidInputException("Feedback text cannot be greater than 700 characters");
    	if( profileItem.getRating() == null || profileItem.getRating() < 1 || profileItem.getRating() > 10 ) throw new InvalidInputException("Please provide a rating between 1 and 10");
    }
    
    public File markAsComplete(Integer fileId, TanlsUser user) throws InvalidInputException
    {
        Lawyer lawyer = tanlsUserDAO.getLawyer(user);
        if( lawyer == null ) throw new InvalidInputException("You must be a lawyer to mark a file as complete");
        File file = fileDAO.getWithPaymentAndQuote(fileId);
        if( file == null ) throw new InvalidInputException("File not found: " + fileId);
        if( !file.getLawyer().getId().equals(lawyer.getId()) ) throw new InvalidInputException("You are not a party to this file");
        if( !file.getStatus().equals(File.STATUS_IN_PROGRESS) ) throw new InvalidInputException("The file is not currently in progress");
    	file.setStatus(File.STATUS_COMPLETE);
    	file.setCompletionTime(Calendar.getInstance().getTimeInMillis());
    	fileDAO.update(file);
    	
    	SendComplete thread = new SendComplete(file);
    	thread.start();
    	
    	return file;
    }
    
    public class SendComplete extends Thread
    {
    	private File file;
    	
    	public SendComplete(File file)
    	{
    		this.file = file;
    	}
    	
    	@Override
    	public void run()
    	{
    		try 
    		{
				sendCompleteToCustomer(file);
			} 
    		catch (AddressException e) 
    		{
				log.error(e);
			} 
    		catch (MessagingException e) 
    		{
				log.error(e);
			}
    		try 
    		{
				sendCompleteToLawyer(file);
			} 
    		catch (AddressException e) 
    		{
				log.error(e);
			} 
    		catch (MessagingException e) 
    		{
				log.error(e);
			}
    	}
    }
    
    public void sendCompleteToCustomer(File file) throws AddressException, MessagingException
    {
    	String subject = "Easy Law booking completion";
		StringBuffer s = new StringBuffer();
		s.append("<p>Hi ");
		s.append(file.getCustomer().getName());
		s.append(",</p>");
		s.append("<p>Thank you for using Easy Law!</p>");
		s.append("<p>Your lawyer ");
		s.append(file.getLawyer().getName());
		s.append(" has notified us that the work has been completed.</p>");
		s.append("<p>We hope that they were outstanding!</p>");
		s.append("<p>Please take a moment to rate your experience and provide some feedback about the lawyer.");
		s.append(" You can do this using the <a href='");
		s.append(mailService.getBaseUrl());
		s.append("/#!/customer/files/");
		s.append(file.getId());
		s.append("'>web interface</a>. Your feedback is important as it gives outstanding lawyers the opportunity to stand out.</p>");
		s.append("<p>In the very unlikely event of any dispute with the lawyer about the completion of the legal services let us");
		s.append(" know immediately by emailing disputes@easylaw.com.au. We must receive your email within 48 hours before funds are released to the lawyer.</p>");
		s.append("Let us know if you have any feedback about the Easy Law platform itself, we aim to provide all your legal needs!");
		s.append("<p>Regards,</p>");
		s.append("<p>Easy Law</p>");
		
		mailService.sendMessageHTML(file.getCustomer().getUser().getEmail(), subject, s.toString());
    }
    
    public void sendCompleteToLawyer(File file) throws AddressException, MessagingException
    {
    	String subject = "Easy Law booking completion";
		StringBuffer s = new StringBuffer();
		s.append("<p>Hi ");
		s.append(file.getCustomer().getName());
		s.append(",</p>");
		s.append("<p>Thank you for using Easy Law!</p>");
		s.append("<p>You have notified us that the work has been completed for file ");
		s.append(file.getId());
		s.append(".</p>");
		s.append("<p>We hope that you had a great experience!</p>");
		s.append("<p>The customer can provide feedback on your work that will appear on your timeline. They also give you a rating of 1-10.");
		s.append(" Ratings of 5-10 give you 5-10 points respectively. Ratings above 5 are considered positive and will improve your percentage positive score.");
		s.append(" You can view the file feedback on the <a href='");
		s.append(mailService.getBaseUrl());
		s.append("/#!/lawyer/files/");
		s.append(file.getId());
		s.append("'>booking page</a> as well as on your profile.");
		if( file.getQuote().getQuestion().getProBono() == null ||  !file.getQuote().getQuestion().getProBono() )
		{
			s.append("<p>Your payment will be transferred into the bank account specified on the <a href='");
			s.append(mailService.getBaseUrl());
			s.append("#!/lawyer/settings'>settings</a> page after 48 hours.<p>");
			s.append("<p>The following payments will be made, showing the 15% Easy Law agent fee on legal fees and 3% credit card processing fee on disbursements:</p>");
			s.append("<table>");
			s.append("<tr>");
			s.append("<th>Item</th>");
			s.append("<th>Amount (AUD)</th>");
			s.append("<th>Net (AUD)</th>");
			s.append("</tr>");
			s.append("<tr>");
			s.append("<td>Legal Fees:</td>");
			s.append("<td>");
			BigDecimal legalFees = file.getQuote().getLegalFees();
			legalFees = legalFees.setScale(2, RoundingMode.HALF_EVEN);
			s.append(legalFees.toPlainString());
			s.append("</td>");
			s.append("<td>");
			BigDecimal fees = file.getQuote().getLegalFeesMinusComission();
			fees = fees.setScale(2, RoundingMode.HALF_EVEN);
			s.append(fees.toPlainString());
			s.append("</td>");
			s.append("</tr>");
			if( file.getQuote().getDisbursments() != null )
			{
				s.append("<tr>");
				s.append("<td>Disbursments:</td>");
				s.append("<td>");
				BigDecimal ds = file.getQuote().getDisbursments();
				ds = ds.setScale(2, RoundingMode.HALF_EVEN);
				s.append(ds.toPlainString());
				s.append("</td>");
				s.append("<td>");
				BigDecimal dsm = file.getQuote().getDisbursmentsMinusCreditCard();
				dsm = dsm.setScale(2, RoundingMode.HALF_EVEN);
				s.append(dsm.toPlainString());
				s.append("</td>");
				s.append("</tr>");
			}
			s.append("<tr>");
			s.append("<td>Total:</td>");
			s.append("<td>");
			BigDecimal total = file.getQuote().getAmount();
			total = total.setScale(2, RoundingMode.HALF_EVEN);
			s.append(total.toPlainString());
			s.append("</td>");
			s.append("<td>");
			BigDecimal a = file.getQuote().getAmountMinusFees();
			a = a.setScale(2, RoundingMode.HALF_EVEN);
			s.append(a.toPlainString());
			s.append("</td>");
			s.append("</tr>");
			s.append("</table>");
		}
		s.append("<p>Let us know if you have any feedback about the Easy Law platform itself, we aim to make working as a lawyer as easy as possible!</p>");
		s.append("<p>Regards,</p>");
		s.append("<p>Easy Law</p>");
		
		mailService.sendMessageHTML(file.getLawyer().getUser().getEmail(), subject, s.toString());
    }
    
    public class SendFeedback extends Thread
    {
    	private File file;
    	
    	public SendFeedback(File file)
    	{
    		this.file = file;
    	}
    	
    	@Override
    	public void run()
    	{
    		try 
    		{
				sendFeedbackToLawyer(file);
			} 
    		catch (AddressException e) 
    		{
				log.error(e);
			} 
    		catch (MessagingException e) 
    		{
				log.error(e);
			}
    	}
    }
    
    public void sendFeedbackToLawyer(File file) throws AddressException, MessagingException
    {
    	Lawyer lawyer = file.getLawyer();
    	
    	String subject = "Easy Law feedback received";
		StringBuffer s = new StringBuffer();
		s.append("<p>Hi ");
		s.append(file.getCustomer().getName());
		s.append(",</p>");
		s.append("<p>Your cusomer has just provided some feedback!</p>");
		s.append("<h3>Feedback</h3>");
		s.append("<p><strong>Rating (1-10): </strong> ");
		s.append(file.getFeedback().getRating());
		s.append("</p>");
		s.append("<p><strong>Comments: </strong> ");
		s.append(file.getFeedback().getText());
		s.append("</p>");
		s.append("<h3>Booking Details</h3>");
		s.append("<p><strong>Customer: </strong>");
		s.append(file.getCustomer().getName());
		s.append("</p>");
		s.append("<p><strong>Customer Contact Email: </strong>");
		s.append(file.getCustomer().getUser().getEmail());
		s.append("</p>");
		s.append("<p><strong>Question: </strong>");
		s.append(file.getQuote().getQuestion().getText());
		s.append("</p>");
		s.append("<p><strong>Description of legal services: </strong>");
		s.append(file.getQuote().getText());
		s.append("</p>");
		s.append("<p>Thanks for using Easy Law!</p>");
		s.append("<p>Regards,</p>");
		s.append("<p>Easy Law</p>");
		s.append("<p>Easy Law - Problem Solved Pty. Ltd., ABN: 54162874446<p>");
		
		mailService.sendMessageHTML(lawyer.getUser().getEmail(), subject, s.toString());
    }

	public PaymentService getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(PaymentService paymentService) 
	{
		this.paymentService = paymentService;
	}

	public File getCustomerSingle(Integer fileId, TanlsUser user) {
		return fileDAO.getCustomerSingle(fileId,user);
	}

	public File getLawyerSingle(Integer fileId, TanlsUser user) {
		return fileDAO.getLawyerSingle(fileId,user);
	}

	public Boolean getEasyLawGST() {
		return easyLawGST;
	}

	public void setEasyLawGST(Boolean easyLawGST) {
		this.easyLawGST = easyLawGST;
	}

}

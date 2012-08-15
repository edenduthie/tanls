package tanls;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;

import tanls.dto.SignupRequest;
import tanls.entity.Address;
import tanls.entity.Answer;
import tanls.entity.AreaOfPractise;
import tanls.entity.Customer;
import tanls.entity.File;
import tanls.entity.Lawyer;
import tanls.entity.Payment;
import tanls.entity.Photo;
import tanls.entity.ProfileItem;
import tanls.entity.Question;
import tanls.entity.Quote;
import tanls.entity.Suburb;
import tanls.entity.TanlsGrantedAuthority;
import tanls.entity.TanlsUser;

public class Generator 
{

	public static TanlsUser tanlsUser() 
	{
    	TanlsUser tanlsUser = new TanlsUser();
    	tanlsUser.setCreatedTime(Calendar.getInstance().getTimeInMillis());
    	tanlsUser.setCredentialsExpired(false);
    	tanlsUser.setEmail("eden@easylaw.com.au");
    	tanlsUser.setEnabled(true);
    	tanlsUser.setExpired(false);
    	tanlsUser.setLocked(false);
    	tanlsUser.setPassword("password");
    	tanlsUser.setLawyer(false);
    	return tanlsUser;
	}

	public static TanlsGrantedAuthority tanlsGrantedAuthority() 
	{
		TanlsGrantedAuthority auth = new TanlsGrantedAuthority();
		auth.setAuthority(TanlsGrantedAuthority.ROLE_USER);
		return auth;
	}

	public static Address address() 
	{
		Address address = new Address();
		address.setName("Eden Duthie");
		address.setState("VIC");
		address.setPostcode("3101");
		address.setStreetAddress("32 Marshall Avenue");
		address.setSuburb("Kew");
		return address;
	}

	public static Suburb suburb() 
	{
		Suburb suburb = new Suburb("KEW");
		return suburb;
	}

	public static SignupRequest signupRequest() 
	{
		SignupRequest request = new SignupRequest();
		request.setEmail("eduthie@gmail.com");
		request.setPassword("password");
		request.setRepeatPassword("password");
		request.setAsLawyer(false);
		return request;
	}

	public static Lawyer lawyer() 
	{
		Lawyer lawyer = new Lawyer();
		lawyer.setFacebook("easylawHQ");
		lawyer.setLinkedin(null);
		lawyer.setName("Eden Duthie");
		lawyer.setPractitionerNumber("123123123");
		lawyer.setState("VIC");
		lawyer.setSummary("This is the summary");
		lawyer.setTimeAdmitted(Calendar.getInstance().getTimeInMillis());
		lawyer.setTwitter("easylawHQ");
		lawyer.setUrl("www.easylaw.com.au");
		lawyer.setUsername("eduthie");
		lawyer.setUuid(UUID.randomUUID().toString());
		lawyer.setQuestionNotifications(true);
		lawyer.setFirmName("Slater and Gordon");
		lawyer.setAbn("54345345345345");
		return lawyer;
	}

	public static Photo photo() 
	{
		Photo photo = new Photo();
		photo.setId(666);
		photo.setPositionX(123);
		photo.setPositionY(234);
		photo.setPicture(new byte[5]);
		return photo;
	}
	
	public static ProfileItem profileItem()
	{
		ProfileItem pi = new ProfileItem();
		pi.setCreatedTime(Calendar.getInstance().getTimeInMillis());
		pi.setEndTime(Calendar.getInstance().getTimeInMillis());
		pi.setPostedTime(Calendar.getInstance().getTimeInMillis());
		pi.setStartTime(Calendar.getInstance().getTimeInMillis());
		pi.setSubtitle("subtitle");
		pi.setText("text");
		pi.setTimelineTime(Calendar.getInstance().getTimeInMillis());
		pi.setTitle("title");
		pi.setType(ProfileItem.JOB);
		return pi;
	}

	public static AreaOfPractise areaOfPractise() 
	{
		AreaOfPractise area = new AreaOfPractise();
		area.setName("area");
		return area;
	}

	public static Customer customer() 
	{
		Customer customer = new Customer();
		customer.setCompanyName("Easy Law");
		customer.setName("Eden Duthie");
		customer.setNumberOfQuestionsAsked(34);
		customer.setEmailNotifications(true);
		return customer;
	}

	public static Question question() 
	{
		Question question = new Question();
		question.setBusinessQuestion(false);
		question.setText("This is the question");
		question.setProBono(false);
		question.setQuoteRequired(false);
		return question;
	}

	public static Answer answer() 
	{
		Answer answer = new Answer();
		answer.setStatus(Answer.STATUS_NOT_USEFUL);
		answer.setText("This is the text");
		return answer;
	}

	public static Quote quote() 
	{
		Quote quote = new Quote();
		quote.setDisbursments(new BigDecimal(1000));
		quote.setLegalFees(new BigDecimal(100));
		quote.setText("this is the quote");
		quote.setTime(Calendar.getInstance().getTimeInMillis());
		return quote;
	}

	public static Payment payment() 
	{
		Payment payment = new Payment();
		payment.setAmount(new BigDecimal(100.50));
		payment.setCcv("123");
		payment.setCreditCardNumber("4200000000000000");
		payment.setExpiry("10/2016");
		payment.setTime(Calendar.getInstance().getTimeInMillis());
		payment.setUseExistingCustomer(false);
		return payment;
	}

	public static File file()
    {
		File file = new File();
		file.setStatus(null);
		file.setTime(null);
		return file;
	}
}

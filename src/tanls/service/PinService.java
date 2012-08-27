package tanls.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tanls.database.CustomerDAO;
import tanls.database.PaymentDAO;
import tanls.entity.Customer;
import tanls.entity.Payment;
import tanls.exception.InvalidInputException;
import tanls.exception.PaymentException;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PinService implements PaymentService
{
	public static final Logger log = Logger.getLogger(PinService.class);
	
	public String endpoint;
	public String secretApiKey;
	
	@Autowired CustomerDAO customerDAO;
	@Autowired PaymentDAO paymentDAO;

	public Payment pay(Payment payment) throws PaymentException 
	{
		if( payment.getAmount() == null ) throw new PaymentException("Amount is required");
		if( payment.getCustomer() == null ) throw new PaymentException("Customer is required");
		if( payment.getCustomer().getUser() == null ) throw new PaymentException("User is required");
		
		BigDecimal amount = payment.getAmount();
		amount = amount.multiply(new BigDecimal(100));
		amount = amount.setScale(0,RoundingMode.HALF_EVEN);
		
		final String chargeRequest = "{" + 
	        "\"email\": \"" + payment.getCustomer().getUser().getEmail() + "\"," +
	        "\"description\": \"" + payment.getCustomer().getUser().getEmail() + "\"," +
	        "\"amount\": \"" + amount.toPlainString() + "\"," +
	        "\"currency\": \"AUD\"," +
	        "\"ip_address\": \"" + payment.getCustomer().getUser().getIp() + "\"," +
	        "\"customer_token\": \"" + payment.getCustomer().getCustomerId() + "\"" +
	        "}";
		
		Map<String,Object> response = processTransaction(chargeRequest,"charges");
		
		String token = (String) response.get("token");
		payment.setTransactionId(token);
		payment.setTime(Calendar.getInstance().getTimeInMillis());
		
		paymentDAO.put(payment);
		
		return payment;
	}
	
	public Customer createCustomer(Payment payment) throws PaymentException, InvalidInputException
	{
		Customer customer = payment.getCustomer();
	
		if( customer == null ) throw new PaymentException("Customer is required");
		if( customer.getUser() == null ) throw new PaymentException("User is required");
		
		payment.validate();
		
		final String createCustomerRequest = "{" + 
		    "\"email\": \"" + customer.getUser().getEmail() + "\"," +
			    "\"card\": {" +
		            "\"number\": \"" + payment.getCreditCardNumber() + "\"," +
		            "\"expiry_month\": \"" + payment.getExpiry().substring(0,2) + "\"," +
		            "\"expiry_year\": \"" + payment.getExpiry().substring(3,7) + "\"," +
		            "\"cvc\": \"" + payment.getCcv() + "\"," +
		            "\"name\": \"" + customer.getName() + "\"," +
		            "\"address_line1\": \"" + customer.getBillingAddress().getStreetAddress() + "\"," +
		            "\"address_line2\": null," +
		            "\"address_city\": \"" + customer.getBillingAddress().getSuburb() + "\"," +
		            "\"address_postcode\": \"" + customer.getBillingAddress().getPostcode() + "\"," +
		            "\"address_state\": \"" + customer.getBillingAddress().getState() + "\"," +
		            "\"address_country\": \"" + "Australia" + "\"" +
			    "}" +
			"}";
		
		Map<String,Object> response = processTransaction(createCustomerRequest,"customers");
	    String token = (String) response.get("token");
	    Map<String,Object> card = (Map<String, Object>) response.get("card");
	    String cardToken = (String) card.get("token");
	    String displayNumber = (String) card.get("display_number");
	    String scheme = (String) card.get("scheme");
	    
	    customer.setCustomerId(token);
	    customer.setCreditCardToken(cardToken);
	    customer.setMaskedNumber(displayNumber);
	    customer.setCreditCardType(scheme);
	    
	    customerDAO.update(customer);
	    
	    return customer;
	}
	
	public Map<String,Object> processTransaction(String jsonRequest, String resource) throws PaymentException
	{
		try
		{
			HttpURLConnection connection = (HttpURLConnection) ((new URL(getEndpoint()+"/"+resource).openConnection()));
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestMethod("POST");
			
			String userpass = getSecretApiKey() + ":";
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
			connection.setRequestProperty ("Authorization", basicAuth);
			
			connection.connect();
			
			final OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
			osw.write(jsonRequest);
			osw.close();
			
			BufferedReader reader = null;
			try
			{
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
			catch( IOException e )
			{
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			}
		    StringBuilder out = new StringBuilder();
		    String line;
		    while ((line = reader.readLine()) != null) 
		    {
		        out.append(line);
		    }
		    ObjectMapper mapper = new ObjectMapper();
		    Map<String,Object> userData = mapper.readValue(out.toString(), Map.class);
		    
		    if( userData.containsKey("error") )
		    {
		        String errorCode = (String) userData.get("error");
		        String errorDescription = (String) userData.get("error_description");
		        log.error("Failed to create customer: " + errorCode + " " + errorDescription);
		        ArrayList<Object> messages = (ArrayList<Object>) userData.get("messages");
		        if( messages != null )
		        {
			        for( Object message : messages )
			        {
			        	Map<String,Object> messageMap = (Map<String,Object>) message;
			        	errorDescription += " " + messageMap.get("message") + ".";
			        }
		        }
		        throw new PaymentException(errorDescription);
		    }
		    else
		    {
		    	int code;
				code = connection.getResponseCode();
		    	if( code < 300 )
		    	{
				    Map<String,Object> response = (Map<String, Object>) userData.get("response");
				    return response;
		    	}
		    	else
		    	{
		    	    log.error("Invalid response code: " + code);
		    	    throw new PaymentException("Failed to connect to payment provider, please try again");
		    	}
		    }
		}
		catch( IOException e )
		{
			log.error(e);
			throw new PaymentException("Failed to connect to payment provider, please try again");
		}
	}
	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getSecretApiKey() {
		return secretApiKey;
	}

	public void setSecretApiKey(String secretApiKey) {
		this.secretApiKey = secretApiKey;
	}

}

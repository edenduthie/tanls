package tanls.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;

import tanls.dto.AddressDTO;
import tanls.exception.InvalidInputException;

@Entity
public class Address extends ComparableBean implements Serializable
{
	private static final long serialVersionUID = 7597103605197094167L;
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	private String streetAddress;
	private String suburb;
	private String state;
	private String postcode;
	
	public Address() {}
	
	public Address(AddressDTO billingAddress) 
	{
		BeanUtils.copyProperties(billingAddress, this);
	}

	public void update(Address other)
	{
		Integer id = getId();
		BeanUtils.copyProperties(other, this);
		setId(id);
	}
	
	public Address clone()
	{
		Address address = new Address();
		address.update(this);
		return address;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Transient
	public void validate() throws InvalidInputException
	{
		if( getName() == null || getName().trim().length() <= 0 ) throw new InvalidInputException("Name is required");
		if( getName().length() > 200 ) throw new InvalidInputException("Name cannot be more than 200 characters long");
		if( getStreetAddress() == null || getStreetAddress().trim().length() <= 0 ) throw new InvalidInputException("Street address is required");
		if( getStreetAddress().length() > 300 ) throw new InvalidInputException("Street address cannot be longer than 300 characters");
		if( getSuburb() == null || getSuburb().trim().length() <= 0 ) throw new InvalidInputException("Suburb is required");
		if( getSuburb().length() > 100 ) throw new InvalidInputException("Suburb cannot be longer than 100 characters");
		if( getState() == null || getState().trim().length() <=0 ) throw new InvalidInputException("State is required");
		if( !(getState().trim().length() == 2 || getState().trim().length() == 3) ) throw new InvalidInputException("State must be 2 or 3 characters");
		if( getPostcode() == null || getPostcode().trim().length() != 4 ) throw new InvalidInputException("Postcode must be 4 characters");
	}
}

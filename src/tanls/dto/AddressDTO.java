package tanls.dto;

import org.springframework.beans.BeanUtils;

import tanls.entity.Address;

public class AddressDTO 
{
	private Integer id;
	private String name;
	private String streetAddress;
	private String suburb;
	private String state;
	private String postcode;
	private Long time;
	
	public AddressDTO() {};
	
	public AddressDTO(Address billingAddress) 
	{
		BeanUtils.copyProperties(billingAddress, this);
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

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
}

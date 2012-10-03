package tanls.dto;

import org.springframework.beans.BeanUtils;

import tanls.entity.ProfileItem;

public class ProfileItemDTO extends ResponseDTO
{
	private Integer id;
	private String title;
	private String subtitle;
	private Long startTime;
	private Long endTime;
	private Long postedTime;
	private Long createdTime;
	private Long timelineTime;
	private String type;
	private String text;
	private LawyerDTO lawyer;
	private String status;
	private AreaOfPractiseDTO areaOfPractise;
	private CustomerDTO customer;
	private Integer rating;
	
	public ProfileItemDTO() 
	{
		super();
	}
	
	public ProfileItemDTO(String message, boolean success)
	{
		super(message,success);
	}
	
	public ProfileItemDTO(String message, boolean success, boolean redirectToLogin)
	{
		super(message,success,redirectToLogin);
	}
	
	public ProfileItemDTO(ProfileItem profileItem)
	{
		super();
		String[] ignore = {"lawyer","areaOfPractise","customer"};
		BeanUtils.copyProperties(profileItem, this, ignore);
		if( profileItem.getLawyer() != null ) setLawyer(new LawyerDTO(profileItem.getLawyer()));
		if( profileItem.getAreaOfPractise() != null ) setAreaOfPractise(new AreaOfPractiseDTO(profileItem.getAreaOfPractise()));
		if( profileItem.getCustomer() != null ) setCustomer(new CustomerDTO(profileItem.getCustomer()));
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Long getPostedTime() {
		return postedTime;
	}
	public void setPostedTime(Long postedTime) {
		this.postedTime = postedTime;
	}
	public Long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}
	public Long getTimelineTime() {
		return timelineTime;
	}
	public void setTimelineTime(Long timelineTime) {
		this.timelineTime = timelineTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public LawyerDTO getLawyer() {
		return lawyer;
	}
	public void setLawyer(LawyerDTO lawyer) {
		this.lawyer = lawyer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AreaOfPractiseDTO getAreaOfPractise() {
		return areaOfPractise;
	}

	public void setAreaOfPractise(AreaOfPractiseDTO areaOfPractise) {
		this.areaOfPractise = areaOfPractise;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
}

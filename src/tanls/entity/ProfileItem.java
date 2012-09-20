package tanls.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.beans.BeanUtils;

import tanls.dto.ProfileItemDTO;

@Entity
public class ProfileItem 
{
	public static final String JOB = "JOB";
	public static final String EDUCATION = "EDUCATION";
	public static final String STATUS_UPDATE = "STATUS_UPDATE";
	public static final String ARTICLE = "ARTICLE";
	public static final String RECOMMENDATION = "RECOMMENDATION";
	public static final String FEEDBACK = "FEEDBACK";
	
	public static final String NEW_STATUS = "NEW";
	public static final String APPROVED_STATUS = "APPROVED";
	public static final String REJECTED_STATUS = "REJECTED";
	
	@Id @GeneratedValue
	private Integer id;
	private String title;
	private String subtitle;
	private Long startTime;
	private Long endTime;
	private Long postedTime;
	private Long createdTime;
	private Long timelineTime;
	private String type;
	@Column(columnDefinition="TEXT") private String text;
	@ManyToOne Lawyer lawyer;
	private String status = NEW_STATUS;
	@ManyToOne private AreaOfPractise areaOfPractise;
	@ManyToOne private Customer customer;
	private Integer rating;
	
	public ProfileItem() {}
	
	public ProfileItem(ProfileItemDTO dto)
	{
		String[] ignore = {"lawyer","areaOfPractise","customer"};
		BeanUtils.copyProperties(dto, this, ignore);
		if( dto.getLawyer() != null ) setLawyer(new Lawyer(dto.getLawyer()));
		if( dto.getAreaOfPractise() != null ) setAreaOfPractise(new AreaOfPractise(dto.getAreaOfPractise()));
		if( dto.getCustomer() != null ) setCustomer(new Customer(dto.getCustomer()));
	}
	
	public ProfileItem(Integer id, String title, Long timelineTime)
	{
		setId(id);
		setTitle(title);
		setTimelineTime(timelineTime);
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
	public Lawyer getLawyer() {
		return lawyer;
	}
	public void setLawyer(Lawyer lawyer) {
		this.lawyer = lawyer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AreaOfPractise getAreaOfPractise() {
		return areaOfPractise;
	}

	public void setAreaOfPractise(AreaOfPractise areaOfPractise) {
		this.areaOfPractise = areaOfPractise;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
}

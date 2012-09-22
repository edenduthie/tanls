package tanls.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.beans.BeanUtils;

import tanls.dto.AreaOfPractiseDTO;
import tanls.dto.LawyerDTO;

@Entity
public class Lawyer implements Serializable
{
	private static final long serialVersionUID = 783973787053687982L;
	
	@Id @GeneratedValue
	private Integer id;
	private String username;
	private String name;
	private Integer points = 0;
	private BigDecimal percentagePositive = new BigDecimal(100);
	private String firmName;
	private String firmUrl;
	private String practitionerNumber;
	private String facebook;
	private String twitter;
	private String linkedin;
	private String state;
	private String url;
	private Long createdTime;
	private Long timeAdmitted;
	private Integer currentSignUpStage = 1;
	@Column(columnDefinition="TEXT") private String summary;
	@OneToMany(mappedBy="lawyer") private Set<ProfileItem> profileItems;
	@OneToOne private TanlsUser user;
	@ManyToMany(fetch=FetchType.EAGER) Set<AreaOfPractise> areasOfPractise;
	private Boolean questionNotifications;
	private String uuid;
	private String abn;
	private String bsb;
	private String accountNumber;
	private Boolean gst;
	
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	Photo profilePhoto;
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	Photo backgroundPhoto;
	
	public Lawyer(LawyerDTO lawyerDTO) 
	{
		String[] ignore = {"areasOfPractise","backgroundPhoto","profilePhoto","user"};
		BeanUtils.copyProperties(lawyerDTO, this, ignore);
		
		if( lawyerDTO.getAreasOfPractise() != null )
		{
			this.areasOfPractise = new HashSet<AreaOfPractise>();
			for( AreaOfPractiseDTO areaDTO : lawyerDTO.getAreasOfPractise() )
			{
				this.areasOfPractise.add(new AreaOfPractise(areaDTO));
			}
		}
		
		if( lawyerDTO.getBackgroundPhoto() != null ) setBackgroundPhoto(new Photo(lawyerDTO.getBackgroundPhoto()));
		if( lawyerDTO.getProfilePhoto() != null ) setProfilePhoto(new Photo(lawyerDTO.getProfilePhoto()));
		if( lawyerDTO.getUser() != null ) setUser(new TanlsUser(lawyerDTO.getUser()));
	}
	public Lawyer() {}
	
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
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public BigDecimal getPercentagePositive() {
		return percentagePositive;
	}
	public void setPercentagePositive(BigDecimal percentagePositive) {
		this.percentagePositive = percentagePositive;
	}
	public String getPractitionerNumber() {
		return practitionerNumber;
	}
	public void setPractitionerNumber(String practitionerNumber) {
		this.practitionerNumber = practitionerNumber;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getTwitter() {
		return twitter;
	}
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}
	public String getLinkedin() {
		return linkedin;
	}
	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getTimeAdmitted() {
		return timeAdmitted;
	}
	public void setTimeAdmitted(Long timeAdmitted) {
		this.timeAdmitted = timeAdmitted;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Set<ProfileItem> getProfileItems() {
		return profileItems;
	}
	public void setProfileItems(Set<ProfileItem> profileItems) {
		this.profileItems = profileItems;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public TanlsUser getUser() {
		return user;
	}
	public void setUser(TanlsUser user) {
		this.user = user;
	}
	public String getFirmName() {
		return firmName;
	}
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	public String getFirmUrl() {
		return firmUrl;
	}
	public void setFirmUrl(String firmUrl) {
		this.firmUrl = firmUrl;
	}
	public Long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}
	public Photo getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(Photo profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
	public Photo getBackgroundPhoto() {
		return backgroundPhoto;
	}
	public void setBackgroundPhoto(Photo backgroundPhoto) {
		this.backgroundPhoto = backgroundPhoto;
	}
	public Integer getCurrentSignUpStage() {
		return currentSignUpStage;
	}
	public void setCurrentSignUpStage(Integer currentSignUpStage) {
		this.currentSignUpStage = currentSignUpStage;
	}
	public Set<AreaOfPractise> getAreasOfPractise() {
		return areasOfPractise;
	}
	public void setAreasOfPractise(Set<AreaOfPractise> areasOfPractise) {
		this.areasOfPractise = areasOfPractise;
	}
	public Boolean getQuestionNotifications() {
		return questionNotifications;
	}
	public void setQuestionNotifications(Boolean questionNotifications) {
		this.questionNotifications = questionNotifications;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getAbn() {
		return abn;
	}
	public void setAbn(String abn) {
		this.abn = abn;
	}
	public String getBsb() {
		return bsb;
	}
	public void setBsb(String bsb) {
		this.bsb = bsb;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Boolean getGst() {
		return gst;
	}
	public void setGst(Boolean gst) {
		this.gst = gst;
	}
}

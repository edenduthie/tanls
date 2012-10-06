package tanls.dto;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import tanls.entity.AreaOfPractise;
import tanls.entity.Lawyer;
import tanls.entity.Photo;

public class LawyerDTO extends ResponseDTO
{
	private Integer id;
	private String username;
	private String name;
	private Integer points;
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
	private Integer currentSignUpStage;
	private String summary;
	private PhotoDTO backgroundPhoto;
	private PhotoDTO profilePhoto;
	private Set<AreaOfPractiseDTO> areasOfPractise;
	private Boolean questionNotifications;
	private String uuid;
	private TanlsUserDTO user;
	private String abn;
	private String bsb;
	private String accountNumber;
	private Boolean gst;
	
	public LawyerDTO(String message, boolean success)
	{
		setMessage(message);
		setSuccess(success);
	}
	
	public LawyerDTO(Lawyer lawyer) 
	{
		super();
		
		String[] ignore = {"areasOfPractise","backgroundPhoto","profilePhoto","user"};
		BeanUtils.copyProperties(lawyer, this, ignore);
		
		if( lawyer.getAreasOfPractise() != null )
		{
			this.areasOfPractise = new HashSet<AreaOfPractiseDTO>();
			for( AreaOfPractise area : lawyer.getAreasOfPractise() )
			{
				this.areasOfPractise.add(new AreaOfPractiseDTO(area));
			}
		}
		
		if( lawyer.getBackgroundPhoto() != null ) setBackgroundPhoto(new PhotoDTO(lawyer.getBackgroundPhoto()));
		if( lawyer.getProfilePhoto() != null ) setProfilePhoto(new PhotoDTO(lawyer.getProfilePhoto()));
		if( lawyer.getUser() != null ) setUser(new TanlsUserDTO(lawyer.getUser()));
	}
	
	public LawyerDTO() {}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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

	public BigDecimal getPercentagePositive() {
		return percentagePositive;
	}

	public void setPercentagePositive(BigDecimal percentagePositive) {
		this.percentagePositive = percentagePositive;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public PhotoDTO getBackgroundPhoto() {
		return backgroundPhoto;
	}

	public void setBackgroundPhoto(PhotoDTO backgroundPhoto) {
		this.backgroundPhoto = backgroundPhoto;
	}

	public PhotoDTO getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(PhotoDTO profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public Integer getCurrentSignUpStage() {
		return currentSignUpStage;
	}

	public void setCurrentSignUpStage(Integer currentSignUpStage) {
		this.currentSignUpStage = currentSignUpStage;
	}

	public Set<AreaOfPractiseDTO> getAreasOfPractise() {
		return areasOfPractise;
	}

	public void setAreasOfPractise(Set<AreaOfPractiseDTO> areasOfPractise) {
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

	public TanlsUserDTO getUser() {
		return user;
	}

	public void setUser(TanlsUserDTO user) {
		this.user = user;
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

package tanls.dto;

import java.util.List;

public class ProfileItemSearchResponse extends ResponseDTO
{
    List<ProfileItemDTO> profileItems;
    
    public ProfileItemSearchResponse() 
    {
    	super();
    }
    
    public ProfileItemSearchResponse(String message, boolean success)
    {
    	super(message,success);
    }
    
    public ProfileItemSearchResponse(String message, boolean success, boolean redirectToLogin)
    {
    	super(message,success,redirectToLogin);
    }
    
    public ProfileItemSearchResponse(List<ProfileItemDTO> profileItems) 
    {
    	super();
    	setProfileItems(profileItems);
    }

	public List<ProfileItemDTO> getProfileItems() {
		return profileItems;
	}

	public void setProfileItems(List<ProfileItemDTO> profileItems) {
		this.profileItems = profileItems;
	}
}

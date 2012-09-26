package tanls.dto;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import tanls.entity.TanlsUser;

public class TanlsUserDTO extends ResponseDTO implements Serializable
{
	private static final long serialVersionUID = -5138946675396777715L;
	
	public String email;
    private Boolean lawyer;
    private Boolean admin;
    
    public TanlsUserDTO() {}
    
    public TanlsUserDTO(TanlsUser tanlsUser)
    {
    	BeanUtils.copyProperties(tanlsUser, this);
    	setAdmin(tanlsUser.isAdmin());
    }
    
    public TanlsUserDTO(String message, boolean success)
    {
    	setMessage(message);
    	setSuccess(success);
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getLawyer() {
		return lawyer;
	}

	public void setLawyer(Boolean lawyer) {
		this.lawyer = lawyer;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
}

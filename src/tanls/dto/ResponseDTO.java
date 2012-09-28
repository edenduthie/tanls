package tanls.dto;

public class ResponseDTO 
{
	public static String SUCCESS_RESPONSE = "Success";
	
    public String message;
    public boolean success;
    public boolean redirectToLogin = false;
    
    public ResponseDTO()
    {
    	message = SUCCESS_RESPONSE;
    	success = true;
    }
    
    public ResponseDTO(String message, boolean success)
    {
    	this.message = message;
    	this.success = success;
    }
    
    public ResponseDTO(String message, boolean success, boolean redirectToLogin)
    {
    	this.message = message;
    	this.success = success;
    	this.redirectToLogin = redirectToLogin;
    }
    
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isRedirectToLogin() {
		return redirectToLogin;
	}

	public void setRedirectToLogin(boolean redirectToLogin) {
		this.redirectToLogin = redirectToLogin;
	}
}

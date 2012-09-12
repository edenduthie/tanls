package tanls.exception;

public class TanlsSecurityException extends Exception {

	private boolean redirectToLogin = false;
	
	public TanlsSecurityException() {
		// TODO Auto-generated constructor stub
	}

	public TanlsSecurityException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public TanlsSecurityException(String message, boolean redirectToLogin) {
		super(message);
		setRedirectToLogin(redirectToLogin);
	}

	public TanlsSecurityException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public TanlsSecurityException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public boolean isRedirectToLogin() {
		return redirectToLogin;
	}

	public void setRedirectToLogin(boolean redirectToLogin) {
		this.redirectToLogin = redirectToLogin;
	}

}

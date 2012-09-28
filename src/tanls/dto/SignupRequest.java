package tanls.dto;

public class SignupRequest {
	
	String email;
	String password;
	String repeatPassword;
	boolean acceptTerms;
	boolean asLawyer;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRepeatPassword() {
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	public boolean getAsLawyer() {
		return asLawyer;
	}
	public void setAsLawyer(boolean asLawyer) {
		this.asLawyer = asLawyer;
	}
	public boolean getAcceptTerms() {
		return acceptTerms;
	}
	public void setAcceptTerms(boolean acceptTerms) {
		this.acceptTerms = acceptTerms;
	}

}

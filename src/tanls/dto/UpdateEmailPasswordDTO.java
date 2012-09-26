package tanls.dto;

public class UpdateEmailPasswordDTO {

	protected String email;
	protected String password;
	protected String repeat;
	
	public UpdateEmailPasswordDTO() {}
	
	public UpdateEmailPasswordDTO(String email, String password, String repeat)
	{
		setEmail(email);
		setPassword(password);
		setRepeat(repeat);
	}
	
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
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
}

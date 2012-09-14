package tanls.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import tanls.dto.TanlsUserDTO;

@Entity
public class TanlsUser extends ComparableBean implements UserDetails
{
	private static final long serialVersionUID = -7709595936431626340L;
	
	@Id @GeneratedValue
	private Integer id;
	private String email;
	private String password;
	private Long createdTime;
	private Boolean expired = false;
	private Boolean locked = false;
	private Boolean credentialsExpired = false;
	private Boolean enabled = true;
	private String ip;
	private Long signupTime;
	private Boolean lawyer;
	@OneToOne(mappedBy="user") private Lawyer profile;
	@OneToOne(mappedBy="user") private Customer customer;
	
	@ManyToMany(fetch=FetchType.EAGER)
	List<TanlsGrantedAuthority> authorities = new ArrayList<TanlsGrantedAuthority>();
	
	public TanlsUser() {}
	
	public TanlsUser(TanlsUserDTO user) 
	{
		setEmail(user.getEmail());
		setLawyer(user.getLawyer());
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	@Transient
	public String getUsername() {
		return getEmail();
	}
	@Override
	public boolean isAccountNonExpired() {
		return !getExpired();
	}
	@Override
	public boolean isAccountNonLocked() {
		return !getLocked();
	}
	@Override
	public boolean isCredentialsNonExpired() {
        return !getCredentialsExpired();
	}
	@Override
	public boolean isEnabled() {
		return getEnabled();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}
	public Boolean getExpired() {
		return expired;
	}
	public void setExpired(Boolean expired) {
		this.expired = expired;
	}
	public Boolean getLocked() {
		return locked;
	}
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getCredentialsExpired() {
		return credentialsExpired;
	}
	public void setCredentialsExpired(Boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}
	@Override
	public List<TanlsGrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<TanlsGrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	public void add(TanlsGrantedAuthority roleUser) 
	{
		if( authorities == null ) authorities = new ArrayList<TanlsGrantedAuthority>();
		authorities.add(roleUser);
	}
	
	public boolean hasRole(String role)
	{
		if( getAuthorities() != null )
		{
			for( TanlsGrantedAuthority auth : getAuthorities() )
			{
				if(auth.getAuthority().equals(role)) return true;
			}
		}
		return false;
	}
	
	@Transient
	public Boolean getPractitioner()
	{
	    return hasRole(TanlsGrantedAuthority.ROLE_PRACTITIONER);
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Long getSignupTime() {
		return signupTime;
	}
	public void setSignupTime(Long signupTime) {
		this.signupTime = signupTime;
	}
	public Boolean getLawyer() {
		return lawyer;
	}
	public void setLawyer(Boolean lawyer) {
		this.lawyer = lawyer;
	}
	public Lawyer getProfile() {
		return profile;
	}
	public void setProfile(Lawyer profile) {
		this.profile = profile;
	}
	
	@Transient public boolean isAdmin()
	{
	    List<TanlsGrantedAuthority> auth = getAuthorities();
        if( auth == null ) return false;
        for (GrantedAuthority authority : auth ) 
        {
        	if(authority.getAuthority().equals(TanlsGrantedAuthority.ADMIN_ROLE)) return true;
        }
        return false;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}

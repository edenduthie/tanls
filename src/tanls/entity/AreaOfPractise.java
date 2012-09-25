package tanls.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.beans.BeanUtils;

import tanls.dto.AreaOfPractiseDTO;

@Entity
public class AreaOfPractise implements Serializable
{
	private static final long serialVersionUID = -5559577032679465039L;
	
	@Id @GeneratedValue
	private Integer id;
	private String name;
	@ManyToMany(mappedBy="areasOfPractise") Set<Lawyer> lawyers;
	
	public AreaOfPractise() {}
	
	public AreaOfPractise(AreaOfPractiseDTO dto)
	{
		if(dto != null ) BeanUtils.copyProperties(dto, this);
	}
	
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

	public Set<Lawyer> getLawyers() {
		return lawyers;
	}

	public void setLawyers(Set<Lawyer> lawyers) {
		this.lawyers = lawyers;
	}
}

package tanls.dto;

import org.springframework.beans.BeanUtils;

import tanls.entity.AreaOfPractise;

public class AreaOfPractiseDTO 
{
    private Integer id;
    private String name;
    
    public AreaOfPractiseDTO() {}
    
    public AreaOfPractiseDTO(AreaOfPractise area)
    {
    	if( area != null ) BeanUtils.copyProperties(area, this);
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
}

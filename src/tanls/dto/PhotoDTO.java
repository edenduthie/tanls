package tanls.dto;

import org.springframework.beans.BeanUtils;

import tanls.entity.Photo;

public class PhotoDTO 
{
    private Integer id;
    private Integer positionX;
    private Integer positionY;
    
    public PhotoDTO() {}
    
    public PhotoDTO(Photo photo) 
    {
    	if( photo != null ) 
    	{
    		String[] ignore = {"data"};
    		BeanUtils.copyProperties(photo, this, ignore);
    	}
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPositionX() {
		return positionX;
	}

	public void setPositionX(Integer positionX) {
		this.positionX = positionX;
	}

	public Integer getPositionY() {
		return positionY;
	}

	public void setPositionY(Integer positionY) {
		this.positionY = positionY;
	}
}

package tanls.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.beans.BeanUtils;

import tanls.dto.PhotoDTO;

@Entity
public class Photo
{
	public static final String JPG = "jpg";
	public static final String PNG = "png";
	
	@Id @GeneratedValue
	private Integer 		id;
	
    @OneToOne(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
    PhotoData data;
    
    private Integer positionX;
    private Integer positionY;
    
    public Photo() {}
    
    public Photo(byte[] photoDataBytes, Integer id)
    {
    	this.id = id;
    	PhotoData photoData = new PhotoData();
    	photoData.setPicture(photoDataBytes);
    	this.data = photoData;
    }
    
    public Photo(PhotoDTO photoDTO) 
    {
    	if( photoDTO != null ) 
    	{
    		String[] ignore = {"data"};
    		BeanUtils.copyProperties(photoDTO, this, ignore);
    	}
	}

	public Photo clone()
    {
    	Photo photo = new Photo();
    	photo.setName(name);
    	photo.setType(type);
    	photo.setPicture(getPicture().clone());
    	return photo;
    }
	
	public Photo copy(Photo other)
	{
		if( other != null )
		{
		    PhotoData data = getData();
		    BeanUtils.copyProperties(other, this);
		    this.data = data;
		}
		return this;
	}
	
	/**
	 * jpg or png
	 */
	private String type;
	
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getPicture() {
		if( getData() != null ) return getData().getPicture();
		else return null;
	}

	public void setPicture(byte[] picture) {
		this.data = new PhotoData();
		this.data.setPicture(picture);
	}
	
	public InputStream retrieveInputStream()
	{
		return new ByteArrayInputStream(getPicture());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PhotoData getData() {
		return data;
	}

	public void setData(PhotoData data) {
		this.data = data;
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

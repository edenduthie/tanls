package tanls.dto;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.PhotoDTO;
import tanls.entity.Photo;

public class PhotoDTOTest 
{
    @Test
    public void constructor()
    {
    	Photo photo = Generator.photo();
    	PhotoDTO photoDTO = new PhotoDTO(photo);
    	Assert.assertEquals(photoDTO.getId(),photo.getId());
    }
}

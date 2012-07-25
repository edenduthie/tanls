package tanls.dto;

import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.Generator;
import tanls.dto.AreaOfPractiseDTO;
import tanls.dto.LawyerDTO;
import tanls.entity.AreaOfPractise;
import tanls.entity.Lawyer;
import tanls.entity.Photo;

public class LawyerDTOTest 
{
    @Test
    public void constructor()
    {
    	Lawyer lawyer = Generator.lawyer();
    	Photo photo = new Photo();
    	photo.setId(666);
    	lawyer.setBackgroundPhoto(photo);
    	lawyer.setProfilePhoto(photo);
    	Set<AreaOfPractise> areas = new HashSet<AreaOfPractise>();
    	AreaOfPractise area = Generator.areaOfPractise();
    	areas.add(area);
    	lawyer.setAreasOfPractise(areas);
    	lawyer.setUser(Generator.tanlsUser());
    	
    	LawyerDTO lawyerDTO = new LawyerDTO(lawyer);
    	
    	Assert.assertEquals(lawyerDTO.getId(),lawyer.getId());
    	Assert.assertEquals(lawyerDTO.getBackgroundPhoto().getId(),photo.getId());
    	Assert.assertEquals(lawyerDTO.getProfilePhoto().getId(),photo.getId());
    	Assert.assertEquals(lawyerDTO.getAreasOfPractise().size(),1);
    	for( AreaOfPractiseDTO areaDTO : lawyerDTO.getAreasOfPractise() ) Assert.assertEquals(areaDTO.getName(),area.getName());
    	Assert.assertNotNull(lawyerDTO.getUser());
    }
}

package tanls.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import tanls.BaseTest;
import tanls.database.PhotoDAO;
import tanls.database.PhotoDataDAO;
import tanls.entity.Photo;
import tanls.entity.PhotoData;

public class PhotoServiceTest extends BaseTest
{
    @Autowired PhotoService photoService;
    @Autowired PhotoDAO photoDAO;
    @Autowired PhotoDataDAO photoDataDAO;
    
    String filename1 = "testdata/sample-blog-post.jpg";
    int width = 334;
    int height = 334;
    
    @Test
    public void testLoadPhoto() throws IOException
    {
    	System.out.println(System.getProperty("user.dir"));
    	FileInputStream fos = new FileInputStream(new File(filename1));
        Photo photo = photoService.load(fos);
        fos.close();
        Assert.assertEquals(photo.getPicture().length, 14336);
        photoDAO.deleteAll();
        photoDataDAO.deleteAll();
    }
    
    @Test
    public void testResize() throws IOException
    {
    	Photo photo = photoService.create(new FileInputStream(new File(filename1)));
    	photoService.resize(photo, width, height, Photo.JPG);
    	Assert.assertEquals(photo.getPicture().length,7974);
    }
    
    @Test
    public void testConstrainWidth() throws IOException
    {
    	Photo photo = photoService.create(new FileInputStream(new File(filename1)));
    	photoService.constrainWidth(photo, 100, Photo.JPG);
    }
    
    @Test
    public void testConstrainWidthNoConstraint() throws IOException
    {
    	Photo photo = photoService.create(new FileInputStream(new File(filename1)));
    	photoService.constrainWidth(photo, 1000, Photo.JPG);
    }
    
    @Test
    public void testConstrainHeight() throws IOException
    {
    	Photo photo = photoService.create(new FileInputStream(new File(filename1)));
    	photoService.constrainHeight(photo, 30, Photo.JPG);
    }
    
    @Test
    public void testConstrainHeightNoConstraint() throws IOException
    {
    	Photo photo = photoService.create(new FileInputStream(new File(filename1)));
    	photoService.constrainHeight(photo, 1000, Photo.JPG);
    }
}

package org.springframework.samples.travel;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/root-context.xml")
@Transactional
public class BookingServiceTest {

	@Autowired
    private BookingService bookingService;
    
       
    @Test
    public void testUser(){    	
    	
    	bookingService.createBasicData();
    	
    	User findUser = bookingService.findUser("testuser");
    	
		if(findUser != null){		
			Assert.assertEquals("testuser", findUser.getUsername());
		}
    	
    }   

}

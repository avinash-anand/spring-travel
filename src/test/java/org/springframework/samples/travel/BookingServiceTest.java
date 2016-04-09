package org.springframework.samples.travel;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
    
	@Before
	public void setUp() {
		bookingService.createBasicData();
	}
       
    @Test
    public void testUser(){    	
    	User findUser = bookingService.findUser("testuser");
		if(findUser != null){		
			Assert.assertEquals("testuser", findUser.getUsername());
		}
    }   

	@Test
	public void testFindBookings() {
		Booking booking = bookingService.createBooking(1L, "testuser");
		bookingService.save(booking);
		List<Booking> bookings = bookingService.findBookings("testuser");
		Assert.assertEquals(1, bookings.size());
	}
	

	@Test
	public void testFindHotels() {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setSearchString("Taj");
		criteria.setPage(0);
		criteria.setPageSize(10);
		List<Hotel> hotels = bookingService.findHotels(criteria);
		Assert.assertEquals(1, hotels.size());
	}

	@Test
	public void testFindHotelById() {
		Hotel hotel = bookingService.findHotelById(1L);
		Assert.assertEquals("Westin Diplomat", hotel.getName());
	}

	@Test
	public void testCreateBooking() {
		Booking booking = bookingService.createBooking(1L, "testuser");
		bookingService.save(booking);
		List<Booking> bookings = bookingService.findBookings("testuser");
		Assert.assertEquals(1, bookings.size());
	}

	@Test
	public void testSave() {
		Booking booking = bookingService.createBooking(2L, "testuser");
		Date now = new Date();
		booking.setCheckinDate(now);
		bookingService.save(booking);
		List<Booking> bookings = bookingService.findBookings("testuser");
		Date fromDb = null;
		for(Booking b: bookings) {
			if(b.getId().equals(booking.getId())) {
				fromDb = b.getCheckinDate();
			}
		}
		Assert.assertEquals(now, fromDb);
	}

	@Test
	public void testCancelBooking() {
		Booking booking = bookingService.createBooking(2L, "testuser");
		bookingService.save(booking);
		Booking booking1 = bookingService.createBooking(3L, "testuser");
		bookingService.save(booking1);
		bookingService.cancelBooking(booking.getId());
		
		Assert.assertEquals(1, bookingService.findBookings("testuser").size());
	}


	@Test
	public void testFindUser() {
		Assert.assertNotNull(bookingService.findUser("testuser"));
	}
}

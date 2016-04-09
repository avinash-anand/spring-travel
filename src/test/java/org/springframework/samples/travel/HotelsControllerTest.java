package org.springframework.samples.travel;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.easymock.EasyMock;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:/META-INF/spring/root-context.xml")
public class HotelsControllerTest {
	
	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext wac;
	
	private BookingService bookingService;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		bookingService = EasyMock.createMock(BookingService.class);
	}

	@Test
	@Ignore
	public void testHotelsController() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSearch() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testList() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testShow() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testEnterBookingDetails() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testReviewBookingDetails() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBooking() {
		fail("Not yet implemented");
	}

	@Test
//	@Ignore
	public void testBookingSuccess() throws Exception {
		Booking booking = new Booking();
		this.mockMvc.perform(get("/hotels/success")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
				.sessionAttr("booking", booking)
				)
		.andExpect(status().isOk());
//		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDeleteBooking() {
		fail("Not yet implemented");
	}

}

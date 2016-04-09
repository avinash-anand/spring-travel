package org.springframework.samples.travel;

import java.security.Principal;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"booking"})
public class HotelsController {

	private BookingService bookingService;
	
	static int status = 0;

	@Inject
	public HotelsController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@RequestMapping(value = "/hotels/search", method = RequestMethod.GET)
	public void search(SearchCriteria searchCriteria, Principal currentUser, Model model) {	
		if(status++ == 0){
			bookingService.createBasicData();
		}
		if (currentUser != null) {
			List<Booking> booking = bookingService.findBookings(currentUser.getName());
			model.addAttribute(booking);
		}
	}

	@RequestMapping(value = "/hotels", method = RequestMethod.GET)
	public String list(SearchCriteria criteria, Model model) {
		List<Hotel> hotels = bookingService.findHotels(criteria);		
		model.addAttribute(hotels);
		return "hotels/list";
	}


	@RequestMapping(value = "/hotels/{id}", method = RequestMethod.GET)
	public String show(@PathVariable Long id, Model model) {
		model.addAttribute(bookingService.findHotelById(id));
		return "hotels/show";
	}
	
	@RequestMapping(value = "/hotels/booking", method = RequestMethod.GET)
	public String enterBookingDetails(@RequestParam("hotelId") Long id, Model model, Principal currentUser) {

		//TODO dont know how, we can get back at this point after user has logged in, but added because if user isn't logged in throws 
		//a nullPointer here.
		if(currentUser == null) {
			return "users/login";
		}
		
		Booking booking = bookingService.createBooking(id, currentUser.getName());
		model.addAttribute("booking", booking);
		return "enterBookingDetails";
	}
	
	@RequestMapping(value = "/hotels/reviewBookingDetails", method = RequestMethod.POST)
	public String reviewBookingDetails(@ModelAttribute("booking") Booking booking, Model model, HttpServletRequest request, Principal currentUser) {
		booking.setHotel(bookingService.findHotelById(Long.parseLong(request.getParameter("hotel.id"))));
		System.out.println("CC1: " + booking.getCreditCard());
		model.addAttribute("booking", booking);
		return "reviewBooking";
	}
	
	@RequestMapping(value = "/hotels/save", method = RequestMethod.POST)
	public String booking(@ModelAttribute("booking") Booking booking, Model model, HttpServletRequest request, Principal currentUser) {
		booking.setHotel(bookingService.findHotelById(Long.parseLong(request.getParameter("hotel.id"))));
		model.addAttribute("booking", booking);
		System.out.println("CC2: " + booking.getCreditCard());
		bookingService.createBooking(booking.getHotel().getId(), currentUser.getName());
		return "redirect:../hotels/success";
	}
	
	@RequestMapping(value = "/hotels/success", method = RequestMethod.GET)
	public String bookingSuccess(@ModelAttribute("booking") Booking booking, Model model) {
		System.out.println("CC3: " + booking.getCreditCard());
		bookingService.save(booking);
		return "success";
	}

	@RequestMapping(value = "/bookings/{id}", method = RequestMethod.DELETE)
	public String deleteBooking(@PathVariable Long id) {
		bookingService.cancelBooking(id);
		return "redirect:../hotels/search";
	}
}
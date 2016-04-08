package org.springframework.samples.travel;

import java.security.Principal;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
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
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ booking enterBookingDetails = " + booking);
		model.addAttribute("booking", booking);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ model enterBookingDetails = " + model);
		return "enterBookingDetails";
	}
	
	@RequestMapping(value = "/hotels/reviewBookingDetails", method = RequestMethod.POST)
	public String reviewBookingDetails(@ModelAttribute("booking") Booking booking, Model model, HttpServletRequest request, Principal currentUser) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ booking reviewBookingDetails 1 = " + booking);
		booking.setHotel(bookingService.findHotelById(Long.parseLong(request.getParameter("hotel.id"))));
		//Booking booking = bookingService.createBooking(booking.getHotel().getId(), currentUser.getName());
		model.addAttribute("booking", booking);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ booking reviewBookingDetails 1 = " + booking);
		return "reviewBooking";
	}
	
	@RequestMapping(value = "/hotels/save", method = RequestMethod.POST)
	public String booking(@ModelAttribute("booking") Booking booking, Model model, HttpServletRequest request, Principal currentUser) {
		System.out.println("id =  " + request.getParameter("id"));
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ booking 2 = " + booking);
		booking.setHotel(bookingService.findHotelById(Long.parseLong(request.getParameter("hotel.id"))));
		model.addAttribute("booking", booking);
		bookingService.createBooking(booking.getHotel().getId(), currentUser.getName());
		return "redirect:../hotels/success";
	}
	
	@RequestMapping(value = "/hotels/success", method = RequestMethod.GET)
	public String bookingSuccess(Model model) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@ booking 3 = ");
		return "success";
	}

	@RequestMapping(value = "/bookings/{id}", method = RequestMethod.DELETE)
	public String deleteBooking(@PathVariable Long id) {
		bookingService.cancelBooking(id);
		return "redirect:../hotels/search";
	}

	@RequestMapping(value = "hotels/myBookings", method = RequestMethod.GET)
	public String myBookings(Principal currentUser, Model model) {
		
		if(currentUser == null) {
			return "users/login";
		}
		
		List<Booking> bookings = bookingService.findBookings(currentUser.getName());
		model.addAttribute("bookings", bookings);
		return "hotels/bookingsTable";
	}
}
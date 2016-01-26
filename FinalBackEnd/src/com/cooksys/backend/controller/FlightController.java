package com.cooksys.backend.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.backend.beans.dao.FlightDao;
import com.cooksys.backend.beans.dao.MessageDao;
import com.cooksys.backend.beans.wrapper.GetRoutesWrapper;
import com.cooksys.backend.beans.wrapper.PlaneFlightWrapper;
import com.cooksys.backend.beans.wrapper.RouteWrapper;
import com.cooksys.backend.beans.wrapper.RoutesWrapper;
import com.cooksys.backend.beans.wrapper.TripFlightWrapper;
import com.cooksys.backend.model.PlaneFlight;
import com.cooksys.backend.model.Ticket;
import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;

/**
 * Accept HTTP requests pertaining to Flights and PlaneFlights
 * 
 * @author Russell Good
 *
 */
@Controller
@RequestMapping("/flight")
public class FlightController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FlightDao flightDao;
	@Autowired
	private MessageDao messageDao;

	@RequestMapping(value = "/getAllFlights")
	public @ResponseBody FlightModel getAllFlights() {
		return flightDao.getAllFlights();
	}

	@RequestMapping(value = "/getFlightsByTrip/{tripId}")
	public @ResponseBody PlaneFlightWrapper getFlightsByTrip(
			@PathVariable Integer tripId) {
		System.out.println("trip Id = " + tripId);

		PlaneFlightWrapper pfw = new PlaneFlightWrapper();
		pfw.setItems(flightDao.getFlightsByTrip(tripId));

		System.out.println("getFlightsController List length: "
				+ pfw.getItems().size());
		return pfw;
	}

	@RequestMapping(value = "/getDelays/{tripId}")
	public @ResponseBody PlaneFlightWrapper getDelays(
			@PathVariable Integer tripId) {
		PlaneFlightWrapper pfw = new PlaneFlightWrapper();
		pfw.setItems(messageDao.getDelaysByTripId(tripId));
		return pfw;
	}

	/**
	 * save flight, create ticket for flight, and save ticket with trip id
	 * planeflight id if their are no more than five tickts already booked for
	 * the flight
	 * 
	 * @param flight
	 * @return plane flight id
	 */
	@RequestMapping(value = "/saveFlight", method = RequestMethod.POST)
	public @ResponseBody Boolean saveFlight(@RequestBody PlaneFlight flight) {
		log.debug("Flight controller saveing flight to "
				+ flight.getDestinationState() + ", "
				+ flight.getDestinationCity());

		Ticket ticket = new Ticket();
		ticket.setFlightId(flight.getFlightId());
		ticket.setTripId(flight.getTripId());

		List<Ticket> tickets = flightDao.getTicketsByFlightId(flight
				.getFlightId());

		if (tickets == null) {
			tickets = new ArrayList<Ticket>();
		}

		if (tickets.size() < 5) {
			// add flight to database
			flightDao.saveFlight(flight);
			flightDao.saveTicket(ticket);
			return true;
		} else {
			// signal to frontend that trip cannot be booked
			return false;
		}
	}

	@RequestMapping(value = "/cancelFlight", method = RequestMethod.POST)
	public @ResponseBody Boolean cancelFlight(@RequestBody TripFlightWrapper tfw) {
		flightDao.cancelFlight(tfw.getPlaneFlight());
		return true;
	}

	/**
	 * Call getAllRoutes as a List<List<Flight>>. Sort routes by total time of
	 * each route, shortest to longest. Wrap each Route in Routes in
	 * RouteWrapper. Wrap Routes in RoutesWrapper.
	 * 
	 * @param grw
	 * @return RoutesWrapper
	 */
	@RequestMapping(value = "/getRoutes", method = RequestMethod.POST)
	public @ResponseBody RoutesWrapper getRoutes(
			@RequestBody GetRoutesWrapper grw) {

		List<List<Flight>> routes = flightDao.getAllRoutes(grw.getFlights(),
				grw.getOrigin(), grw.getDest());

		routes.sort(new Comparator<List<Flight>>() {
			@Override
			public int compare(List<Flight> f1, List<Flight> f2) {
				Integer len1 = f1.get(f1.size() - 1).getDeparture()
						+ f1.get(f1.size() - 1).getEta();
				Integer len2 = f2.get(f2.size() - 1).getDeparture()
						+ f2.get(f2.size() - 1).getEta();
				return len1.compareTo(len2);
			}
		});

		RoutesWrapper rwp = new RoutesWrapper();
		List<RouteWrapper> list = new ArrayList<RouteWrapper>();

		for (List<Flight> route : routes) {
			RouteWrapper rw = new RouteWrapper();
			rw.setRoute(route);
			list.add(rw);
		}
		rwp.setRoutes(list);
		return rwp;
	}
}

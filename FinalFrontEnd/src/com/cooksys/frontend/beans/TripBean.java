package com.cooksys.frontend.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;
import com.cooksys.core.models.Location;
import com.cooksys.frontend.beans.dao.FlightDao;
import com.cooksys.frontend.beans.dao.TripDao;
import com.cooksys.frontend.model.PlaneFlight;
import com.cooksys.frontend.model.Trip;
import com.cooksys.frontend.model.User;

/**
 * Get all information pertaining to trips including:
 * flights and potential trips as well as the FlightModel
 * that the user sees and searches
 * 
 * @author Russell Good
 *
 */
@Component
@Scope("session")
public class TripBean {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FlightDao flightDao;
	@Autowired
	private AuthenticationBean auth;
	@Autowired
	private TripDao tripDao;

	private boolean fullFlight;
	private Location origin;
	private Location destination;
	private Trip trip;

	private List<Trip> trips;
	private List<Flight> allFlights;
	private List<PlaneFlight> tripFlights;
	private List<PlaneFlight> delays;
	private List<List<Flight>> routes;

	//booleans for showing/hiding panels on pages
	private boolean showRoutes = false;
	private boolean showHomeRoute = false;
	private boolean match = false;
	private boolean justDrive = false;

	@PostConstruct
	public void init() {
		FlightModel fm = flightDao.getAllFlights();
		allFlights = fm.getFlights();
		origin = new Location();
		destination = new Location();
	}

	/**
	 * Store the speficied user as this user so that
	 * when the page accesses this user, it is the right one.
	 * Get the most current FlightModel
	 * @param user
	 */
	public void selectUser(User user) {
		trips = tripDao.getTripsByUser(user.getId());
		List<PlaneFlight> temp = new ArrayList<>();
		delays = new ArrayList<>();

		for (Trip trip : trips) {
			temp = flightDao.getDelaysByTripId(trip.getId());
			delays.addAll(temp);
		}
	}

	public String getTripById(Integer tripId) {
		Trip t = tripDao.getTripById(tripId);
		selectTrip(t);
		log.debug("Retrieved trip with id " + t.getId());
		return "trip";
	}

	/**
	 * Initialze trip variables with trip information
	 * 
	 * @param trip
	 * @return String to redirect page
	 */
	public String selectTrip(Trip trip) {
		this.trip = trip;
		tripFlights = flightDao.getFlightsByTrip(trip);
		showHomeRoute = false;
		fullFlight = false;
		log.debug("Retrieved flights for trip id " + trip.getId());
		return "trip";
	}

	/**
	 * Get a new FlightModel and pass it, the origin, and the destination
	 * to the getRoutes method. It will return a list of all possible routes
	 * 
	 * @return String to reload the page
	 */
	public String search() {
		FlightModel fm = flightDao.getAllFlights();
		allFlights = fm.getFlights();

		routes = flightDao.getRoutes(allFlights, origin.getCity(),
				destination.getCity());

		for (List<Flight> list : routes) {
			System.out.println("A Route:");
			for (Flight flight : list) {
				log.debug(flight.getOrigin().getCity() + " to "
						+ flight.getDestination().getCity());
			}
		}
		log.debug("getRoutes() returned " + routes.size() + " routes.");

		//set booleans to show/hide appropriate panels on page
		match = (routes.size() == 0) ? true : false;
		showRoutes = true;
		showHomeRoute = false;
		fullFlight = false;
		return "search-results";
	}

	/**
	 * Create a new trip and try to save all the flights in that trip.
	 * If they can be saved, the trip is booked. If not, show/hide appropriate 
	 * panels to let the user see the message that one or more flights on the 
	 * trip was full.
	 * 
	 * @param route
	 * @return String to reload the page
	 */
	public String buyTickets(List<Flight> route) {
		Trip newTrip = new Trip();
		newTrip.setCancelled(false);
		newTrip.setUser(auth.getUser());
		newTrip.setOrigin(origin.getCity());
		newTrip.setDestination(destination.getCity());

		Integer newTripId = tripDao.saveTrip(newTrip);
		newTrip.setId(newTripId);

		log.debug("New Trip created with Id: " + newTrip.getId());	
		boolean flag = true;
		
		//convert Flights to PlaneFlight so they can be stored in the database
		for (Flight f : route) {
			PlaneFlight pf = new PlaneFlight();
			pf.setDeparture(f.getDeparture());
			pf.setDestinationCity(f.getDestination().getCity());
			pf.setDestinationState(f.getDestination().getState());
			pf.setEta(f.getEta());
			pf.setFlightId(f.getFlightId());
			pf.setOriginCity(f.getOrigin().getCity());
			pf.setOriginState(f.getOrigin().getState());
			pf.setTripId(newTripId);
			pf.setCancelled(false);
			pf.setLate(false);

			log.debug("Save flight from " + pf.getOriginCity() + " to "
					+ pf.getDestinationCity() + "with TripId: "
					+ pf.getTripId());

			if (flightDao.saveFlight(pf)) {
				log.debug("Save flight from " + pf.getOriginCity() + " to "
						+ pf.getDestinationCity() + "with TripId: "
						+ pf.getTripId());
			} else {
				tripDao.deleteTrip(newTrip);
				fullFlight = true;
				flag = false;
				break;
			}
		}		
		if (flag) {
			selectUser(auth.getUser());
			selectTrip(newTrip);
			return "trip";
		} else {
			fullFlight = true;
			return "home";
		}
	}

	/**
	 * set this.allFlights to the most current verson of the flight model
	 * @return String to reloade the page
	 */
	public String showAllFlights() {
		FlightModel fm = flightDao.getAllFlights();
		allFlights = fm.getFlights();
		showRoutes = false;
		return "search-results";
	}

	/**
	 * Cancel flight. Automatically cancel all flights 
	 * after this flight in the trip.
	 * 
	 * @param flight
	 * @return String to reload the page
	 */
	public String cancel(PlaneFlight flight) {
		log.debug("Flight with flight id " + flight.getFlightId()
				+ " and all subsequent flights are being cancelled");

		boolean flag = false;
		for (PlaneFlight pf : tripFlights) {

			if (flag || pf.getFlightId() == flight.getFlightId())
				flag = true;

			if (flag) {
				flightDao.cancelFlight(pf);
			}
		}
		selectTrip(this.trip);
		return "trip";
	}

	/**
	 * Look for a new route from the selected flight to the trip destination. If
	 * none exists, try to find a route back to the user's home city. If none
	 * exists, tell them to drive.
	 * 
	 * @param flight
	 * @return String to redirect page
	 */
	public String findNewRoute(PlaneFlight flight) {
		FlightModel fm = flightDao.getAllFlights();
		allFlights = fm.getFlights();
		showRoutes = true;
		justDrive = false;
		Trip rerouteTrip = new Trip();
		routes = flightDao.getRoutes(allFlights, flight.getOriginCity(),
				trip.getDestination());

		if (routes.size() == 0) {
			routes = flightDao.getRoutes(allFlights, flight.getOriginCity(),
					auth.getUser().getCity());
			showHomeRoute = true;

			if (routes.size() == 0) {
				justDrive = true;
			} else {

				Integer tripId = tripDao.saveTrip(rerouteTrip);
				for (Flight f : routes.get(0)) {
					PlaneFlight pf = new PlaneFlight();
					pf.setDeparture(f.getDeparture());
					pf.setDestinationCity(f.getDestination().getCity());
					pf.setDestinationState(f.getDestination().getState());
					pf.setEta(f.getEta());
					pf.setFlightId(f.getFlightId());
					pf.setOriginCity(f.getOrigin().getCity());
					pf.setOriginState(f.getOrigin().getState());
					pf.setTripId(tripId);
					pf.setCancelled(false);
					pf.setLate(false);
				}
			}
		}
		return "search-results";
	}

	public boolean isDelayed(int dep, int eta, int nextDep) {
		return nextDep <= (dep + eta);
	}

	public boolean showNoMatch() {
		return match;
	}

	public void lougout() {
		trips = new ArrayList<Trip>();
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public boolean isJustDrive() {
		return justDrive;
	}

	public void setJustDrive(boolean justDrive) {
		this.justDrive = justDrive;
	}

	public List<PlaneFlight> getDelays() {
		return delays;
	}

	public void setDelays(List<PlaneFlight> delays) {
		this.delays = delays;
	}

	public List<PlaneFlight> getTripFlights() {
		return tripFlights;
	}

	public void setTripFlights(List<PlaneFlight> tripFlights) {
		this.tripFlights = tripFlights;
	}

	public boolean isShowRoutes() {
		return showRoutes;
	}

	public void setShowRoutes(boolean showRoutes) {
		this.showRoutes = showRoutes;
	}

	public List<Trip> getTrips() {
		return trips;
	}

	public void setTrips(List<Trip> trips) {
		this.trips = trips;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		System.out.println("set origin " + origin.getCity());
		this.origin = origin;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public List<List<Flight>> getRoutes() {
		return routes;
	}

	public void setRoutes(List<List<Flight>> routes) {
		this.routes = routes;
	}

	public List<Flight> getAllFlights() {
		return allFlights;
	}

	public void setAllFlights(List<Flight> allFlights) {
		this.allFlights = allFlights;
	}

	public boolean isFullFlight() {
		return fullFlight;
	}

	public void setFullFlight(boolean fullFlight) {
		this.fullFlight = fullFlight;
	}

	public boolean isShowHomeRoute() {
		return showHomeRoute;
	}

	public void setShowHomeRoute(boolean showHomeRoute) {
		this.showHomeRoute = showHomeRoute;
	}

}

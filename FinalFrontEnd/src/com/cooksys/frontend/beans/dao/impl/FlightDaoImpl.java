package com.cooksys.frontend.beans.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;
import com.cooksys.frontend.beans.dao.FlightDao;
import com.cooksys.frontend.beans.wrapper.GetRoutesWrapper;
import com.cooksys.frontend.beans.wrapper.PlaneFlightWrapper;
import com.cooksys.frontend.beans.wrapper.RouteWrapper;
import com.cooksys.frontend.beans.wrapper.RoutesWrapper;
import com.cooksys.frontend.beans.wrapper.TripFlightWrapper;
import com.cooksys.frontend.model.PlaneFlight;
import com.cooksys.frontend.model.Trip;

/**
 * Send requests server side to save and retrieve Flight and Plane Flight information.
 * 
 * @author Russell Good
 */
@Component
@Transactional
public class FlightDaoImpl implements FlightDao {

	Logger log = LoggerFactory.getLogger(this.getClass());
	private RestTemplate rt = new RestTemplate();

	/**
	 * Get all the delayed flights in the specified trip.
	 * 
	 * @return List<PlaneFlight> of delayed flights
	 */
	@Override
	public List<PlaneFlight> getDelaysByTripId(Integer tripId) {

		PlaneFlightWrapper pfw = rt
				.getForObject(
						"http://localhost:8080/FinalBackEnd/flight/getDelays/"
								+ tripId, PlaneFlightWrapper.class);
		if (pfw.getItems() != null) {
			return pfw.getItems();
		} else {
			log.debug("GetDelayedFlightsByTripId() returned null");
			return new ArrayList<PlaneFlight>();
		}
	}

	@Override
	public Boolean cancelFlight(PlaneFlight pf) {
		TripFlightWrapper tfw = new TripFlightWrapper();
		tfw.setPlaneFlight(pf);
		return rt.postForObject(
				"http://localhost:8080/FinalBackEnd/flight/cancelFlight/", tfw,
				Boolean.class);

	}

	/**
	 * Get all flights in the specified trip.
	 * 
	 * @return List<PlaneFlight>
	 */
	@Override
	public List<PlaneFlight> getFlightsByTrip(Trip trip) {
		PlaneFlightWrapper pfw = rt.getForObject(
				"http://localhost:8080/FinalBackEnd/flight/getFlightsByTrip/"
						+ trip.getId(), PlaneFlightWrapper.class);

		if (pfw.getItems() != null) {

		} else {
			pfw.setItems(new ArrayList<PlaneFlight>());
		}

		System.out.println("pfw.items.size()=" + pfw.getItems().size());
		return pfw.getItems();
	}

	/**
	 * Get the most up to date FlightModel from FinalInstructorWebService
	 * 
	 * @return FlightModel containing a List of all Flights
	 */
	@Override
	public FlightModel getAllFlights() {
		log.debug("Getting FlightModel...");
		return rt.getForObject(
				"http://localhost:8080/FinalBackEnd/flight/getAllFlights",
				FlightModel.class);
	}

	@Override
	public List<List<Flight>> getRoutes(List<Flight> allFlights, String origin,
			String dest) {

		GetRoutesWrapper grw = new GetRoutesWrapper(origin, dest, allFlights);
		grw.setOrigin(origin);
		grw.setDest(dest);
		grw.setFlights(allFlights);

		RoutesWrapper rwp = rt.postForObject(
				"http://localhost:8080/FinalBackEnd/flight/getRoutes", grw,
				RoutesWrapper.class);

		if (rwp == null)
			log.debug("RoutesWrapper was null");
		if (rwp.getRoutes() == null) {
			log.debug("RoutesWrapper list was null");
			return new ArrayList<List<Flight>>();
		}

		List<List<Flight>> routes = new ArrayList<List<Flight>>();
		for (RouteWrapper rw : rwp.getRoutes()) {
			List<Flight> route = rw.getRoute();
			routes.add(route);
		}
		return routes;
	}

	/**
	 * Make a request to the database to save the specified flight
	 */
	@Override
	public Boolean saveFlight(PlaneFlight flight) {
		return rt.postForObject(
				"http://localhost:8080/FinalBackEnd/flight/saveFlight", flight,
				Boolean.class);
	}
}

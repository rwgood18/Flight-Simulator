package com.cooksys.backend.beans.dao.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cooksys.backend.beans.dao.FlightDao;
import com.cooksys.backend.model.PlaneFlight;
import com.cooksys.backend.model.Ticket;
import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;

/**
 * Data access layer for all transactions related to 
 * the PlaneFlight table in the database.
 * 
 * @author RussellGood
 *
 */
@Component
@Transactional
public class FlightDaoImpl implements FlightDao {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionFactory factory;

	@Override
	public void cancelFlight(PlaneFlight pf) {
		pf.setCancelled(true);
		factory.getCurrentSession().update(pf);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlaneFlight> getFlightsByTrip(Integer tripId) {
		return factory
				.getCurrentSession()
				.createQuery(
						"SELECT pf FROM PlaneFlight pf WHERE pf.tripId = :tripId")
				.setInteger("tripId", tripId).list();
	}

	/**
	 * Get all the most current FlightModel from FinalInstructorWebService
	 */
	@Override
	public FlightModel getAllFlights() {
		RestTemplate rt = new RestTemplate();
		return rt
				.getForObject(
						"http://localhost:8080/FinalInstructorWebService/getFlightModel",
						FlightModel.class);
	}

	/**
	 * Take all flights, origin, and destination and find all possible routes.
	 * getRoutes() finds flights with matching origin and calls recursive on
	 * them.
	 */
	@Override
	public List<List<Flight>> getAllRoutes(List<Flight> allFlights,
			String origin, String dest) {
		List<List<Flight>> routes = new ArrayList<List<Flight>>();

		for (Flight flight : allFlights) {
			if (flight.getOrigin().getCity().equalsIgnoreCase(origin)) {

				getRoutes(allFlights, flight, dest, new ArrayList<Flight>(),
						routes);
			}
		}
		log.debug("================================================ALL ROUTES=========================================================");
		for (int i = 0; i < routes.size(); i++) {
			log.debug("Route #" + i + ":");
			for (Flight f : routes.get(i)) {
				log.debug(f.getOrigin().getCity() + " to "
						+ f.getDestination().getCity());
			}
		}
		return routes;
	}

	private void getRoutes(List<Flight> allFlights, Flight currentFlight,
			String dest, ArrayList<Flight> route, List<List<Flight>> routes) {

		route.add(currentFlight);

		if (currentFlight.getDestination().getCity().equalsIgnoreCase(dest)) {
			boolean flag = true;
			for (List<Flight> list : routes) {
				if (list.equals(route)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				routes.add(new ArrayList<Flight>(route));
				route.remove(currentFlight);
			}
			return;
		}

		for (Flight flight : getNextFlights(allFlights, route, currentFlight)) {
			if (!route.contains(flight)) {
				getRoutes(allFlights, flight, dest, route, routes);
			}
		}
		route.remove(currentFlight);
	}

	/**
	 * Get all flights whose origin is the same as currentFlight's destination
	 * and whose departure time is greater than or equal to currentFlight's ETA
	 * plus its departure time.
	 * 
	 * @param allFlights
	 * @param route
	 * @param currentFlight
	 * @return List<Flight>
	 */
	private List<Flight> getNextFlights(List<Flight> allFlights,
			List<Flight> route, Flight currentFlight) {

		List<Flight> nextFlights = new ArrayList<Flight>();
		for (Flight flight : allFlights) {
			if (flight.getOrigin().getCity()
					.equalsIgnoreCase(currentFlight.getDestination().getCity())
					&& (route.get(route.size() - 1).getDeparture() + route.get(
							route.size() - 1).getEta()) <= flight
							.getDeparture()) {

				nextFlights.add(flight);
			}
		}
		return nextFlights;
	}

	@Override
	public Integer saveFlight(PlaneFlight flight) {
		return (Integer) factory.getCurrentSession().save(flight);
	}

	@Override
	public PlaneFlight getFlightById(Integer id) {
		return (PlaneFlight) factory.getCurrentSession()
				.createQuery("SELECT pf FROM PlaneFlight pf WHERE pf.id = :id")
				.setInteger("id", id).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ticket> getTicketsByFlightId(Integer flightId) {
		return factory.getCurrentSession()
				.createQuery("Select t FROM Ticket t WHERE t.flightId = :id")
				.setInteger("id", flightId).list();
	}

	@Override
	public void saveTicket(Ticket ticket) {
		factory.getCurrentSession().save(ticket);
	}

	@Override
	public PlaneFlight getFlightByNotId(PlaneFlight flight) {

		Integer tripId = flight.getTripId();
		Integer flightId = flight.getFlightId();
		return (PlaneFlight) factory
				.getCurrentSession()
				.createQuery(
						"SELECT pf FROM PlaneFlight pf WHERE pf.flightId = :flightId AND pf.tripId = :tripId")
				.setInteger("flightId", flightId).setInteger("tripId", tripId)
				.uniqueResult();
	}

}

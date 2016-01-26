package com.cooksys.backend.beans.dao;

import java.util.List;

import com.cooksys.backend.model.PlaneFlight;
import com.cooksys.backend.model.Ticket;
import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;

public interface FlightDao {

	public List<PlaneFlight> getFlightsByTrip(Integer tripId);

	public FlightModel getAllFlights();

	public List<List<Flight>> getAllRoutes(List<Flight> allFlights, String origin,
			String dest);

	public Integer saveFlight(PlaneFlight flight);

	public void cancelFlight(PlaneFlight pf);

	public PlaneFlight getFlightById(Integer id);

	public PlaneFlight getFlightByNotId(PlaneFlight flight);

	public List<Ticket> getTicketsByFlightId(Integer planeFlightId);

	public void saveTicket(Ticket ticket);

}

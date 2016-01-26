package com.cooksys.frontend.beans.dao;

import java.util.List;

import com.cooksys.core.models.Flight;
import com.cooksys.core.models.FlightModel;
import com.cooksys.frontend.model.PlaneFlight;
import com.cooksys.frontend.model.Trip;

public interface FlightDao {

	public List<PlaneFlight> getFlightsByTrip(Trip trip);

	public FlightModel getAllFlights();

	public List<List<Flight>> getRoutes(List<Flight> allFlights, String origin,
			String dest);

	public Boolean saveFlight(PlaneFlight flight);

	public Boolean cancelFlight(PlaneFlight flight);

	public List<PlaneFlight> getDelaysByTripId(Integer tripId);
}

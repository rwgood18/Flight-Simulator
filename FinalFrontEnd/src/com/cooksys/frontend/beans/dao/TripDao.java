package com.cooksys.frontend.beans.dao;

import java.util.List;

import com.cooksys.frontend.model.Trip;

public interface TripDao {

	public Integer saveTrip(Trip trip);

	public List<Trip> getTripsByUser(Integer userId);

	public Trip getTripById(Integer tripId);
	
	public Integer deleteTrip(Trip trip);

}

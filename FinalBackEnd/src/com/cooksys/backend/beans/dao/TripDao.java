package com.cooksys.backend.beans.dao;

import java.util.List;

import com.cooksys.backend.model.Trip;

public interface TripDao {

	public Integer saveTrip(Trip trip);

	public List<Trip> getTripsByUser(Integer userId);

	public Trip getTripById(Integer tripId);

	public void deleteTrip(Trip tripId);
}

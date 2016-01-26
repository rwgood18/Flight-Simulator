package com.cooksys.backend.beans.dao;

import java.util.List;

import com.cooksys.backend.model.PlaneFlight;

public interface MessageDao {

	public void setArrived(Integer flightId);

	public void setDelay(Integer flightId);

	public List<PlaneFlight> getDelaysByTripId(Integer tripId);
}

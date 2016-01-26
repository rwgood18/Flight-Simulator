package com.cooksys.backend.beans.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cooksys.backend.model.PlaneFlight;
import com.cooksys.backend.model.Trip;

/**
 * Wrapper for transporting a Flight and a Trip
 * 
 * @author Russell Good
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TripFlightWrapper {

	@XmlElement
	private Trip trip;
	private PlaneFlight planeFlight;

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public PlaneFlight getPlaneFlight() {
		return planeFlight;
	}

	public void setPlaneFlight(PlaneFlight planeFlight) {
		this.planeFlight = planeFlight;
	}

}

package com.cooksys.frontend.beans.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cooksys.frontend.beans.dao.TripDao;
import com.cooksys.frontend.beans.wrapper.TripWrapper;
import com.cooksys.frontend.model.Trip;

/**
 * Send requests server side to save and retrieve Trip information.
 * 
 * @author Russell Good
 */
@Repository
@Transactional
public class TripDaoImpl implements TripDao {

	Logger log = LoggerFactory.getLogger(this.getClass());
	RestTemplate rt = new RestTemplate();

	@Override
	public Integer saveTrip(Trip trip) {
		return rt.postForObject(
				"http://localhost:8080/FinalBackEnd/trip/saveTrip", trip,
				Integer.class);
	}

	/**
	 * Get all the trips a user has booked
	 * 
	 * @return List of user's Trips
	 */
	@Override
	public List<Trip> getTripsByUser(Integer userId) {
		TripWrapper tw;
		List<Trip> trips = null;

		try {
			tw = rt.getForObject(
					"http://localhost:8080/FinalBackEnd/trip/getTripsByUser/"
							+ userId, TripWrapper.class);

			log.debug("Recieved user's trips");
			trips = new ArrayList<Trip>();
			if (tw.getTrips() != null) {

				for (Iterator iterator = tw.getTrips().iterator(); iterator
						.hasNext();) {
					Trip t = (Trip) iterator.next();
					trips.add(t);
				}
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return trips;
	}

	@Override
	public Trip getTripById(Integer tripId) {
		return rt
				.getForObject(
						"http://localhost:8080/FinalBackEnd/trip/getTripById/"
								+ tripId, Trip.class);
	}

	@Override
	public Integer deleteTrip(Trip trip) {
		return rt.postForObject("http://localhost:8080/FinalBackEnd/trip/deleteTrip", trip, Integer.class);
	}

}

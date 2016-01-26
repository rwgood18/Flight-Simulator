package com.cooksys.backend.controller;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.backend.beans.dao.TripDao;
import com.cooksys.backend.beans.dao.UserDao;
import com.cooksys.backend.beans.wrapper.TripWrapper;
import com.cooksys.backend.model.Trip;

/**
 * Accept HTTP requests pertaining to Trips
 * 
 * @author Russell Good
 *
 */
@Controller
@RequestMapping("/trip")
public class TripController {
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDao userDao;
	@Autowired
	private TripDao tripDao;

	/**
	 * Get all the trips with the corresponding user id
	 * 
	 * @param id
	 * @return TripWrapper containing a list of trips
	 * @throws JAXBException
	 */
	@RequestMapping(value = "/getTripsByUser/{id}")
	public @ResponseBody TripWrapper getTripsByUser(@PathVariable String id)
			throws JAXBException {

		List<Trip> trips = tripDao.getTripsByUser(Integer.parseInt(id));
		TripWrapper tw = new TripWrapper();
		tw.setTrips(trips);

		return tw;
	}

	@RequestMapping(value = "/getTripById/{tripId}")
	public @ResponseBody Trip getTripsByUser(@PathVariable Integer tripId)
			throws JAXBException {
		System.out.println("Controller getting trip for id " + tripId);
		return tripDao.getTripById(tripId);
	}

	@RequestMapping(value = "/saveTrip", method = RequestMethod.POST)
	public @ResponseBody Integer saveTrip(@RequestBody Trip trip) {
		return tripDao.saveTrip(trip);
	}
	
	@RequestMapping(value = "/deleteTrip", method = RequestMethod.POST)
	public @ResponseBody Integer deleteTrip(@RequestBody Trip trip) {
		tripDao.deleteTrip(trip);
		return 0;
	}

}

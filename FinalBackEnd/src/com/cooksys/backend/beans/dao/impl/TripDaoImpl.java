package com.cooksys.backend.beans.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cooksys.backend.beans.dao.TripDao;
import com.cooksys.backend.model.Trip;

/**
 * Data access layer for all transactions related to the 
 * Trip table in the database.
 * 
 * @author Russell Good
 *
 */
@Repository
@Transactional
public class TripDaoImpl implements TripDao {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SessionFactory factory;

	@Override
	public Trip getTripById(Integer tripId) {
		Trip t = (Trip) factory.getCurrentSession()
				.createQuery("SELECT t FROM Trip t WHERE t.id = :tripId")
				.setInteger("tripId", tripId).uniqueResult();
		
		log.debug("Getting trip from " + t.getOrigin() + " to " + t.getDestination());
		return t;
	}

	@Override
	public Integer saveTrip(Trip trip) {
		return (Integer) factory.getCurrentSession().save(trip);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Trip> getTripsByUser(Integer userId) {
		return factory.getCurrentSession()
				.createQuery("SELECT t FROM Trip t where t.user = :userId")
				.setInteger("userId", userId).list();
	}

	@Override
	public void deleteTrip(Trip trip) {
		factory.getCurrentSession().delete(trip);
	}

}

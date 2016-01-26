package com.cooksys.backend.beans.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cooksys.backend.beans.dao.MessageDao;
import com.cooksys.backend.model.PlaneFlight;

/**
 * Data access layer for all transactions related to the 
 * PlaneFlight table in the database that have to do with 
 * delay/arrival notifications from JMS Subscriber.
 * 
 * @author Russell Good
 *
 */
@Repository
@Transactional
public class MessageDaoImpl implements MessageDao {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SessionFactory factory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PlaneFlight> getDelaysByTripId(Integer tripId) {
		return factory
				.getCurrentSession()
				.createQuery(
						"SELECT pf FROM PlaneFlight pf WHERE pf.late = 1 AND pf.tripId = :tripId")
				.setInteger("tripId", tripId).list();
	}


	/**
	 * get flights from database by flight ID and increment ETA by one
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setDelay(Integer flightId) {
		List<PlaneFlight> pfs = factory
				.getCurrentSession()
				.createQuery(
						"SELECT pf FROM PlaneFlight pf WHERE pf.flightId = :flightId")
				.setInteger("flightId", flightId).list();

		for (PlaneFlight pf : pfs) {

			pf.setEta(pf.getEta() + 1);
			pf.setLate(true);
			factory.getCurrentSession().update(pf);
		}
	}

	/**
	 * get flights from database by flight ID and set arrived to true
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setArrived(Integer flightId) {
		List<PlaneFlight> pfs = factory
				.getCurrentSession()
				.createQuery(
						"SELECT pf FROM PlaneFlight pf WHERE pf.flightId = :flightId")
				.setInteger("flightId", flightId).list();

		for (PlaneFlight pf : pfs) {
			pf.setArrived(true);
			factory.getCurrentSession().update(pf);
		}
	}

}

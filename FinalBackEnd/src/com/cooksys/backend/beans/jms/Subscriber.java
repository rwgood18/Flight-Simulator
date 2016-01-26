package com.cooksys.backend.beans.jms;

import java.util.List;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cooksys.backend.beans.dao.MessageDao;
import com.cooksys.core.models.Flight;

/**
 *Listen for messages from the specified topic
 *and update the relevant flights in the database
 *via MessageDao.
 * 
 * @author Russell Good
 *
 */
public class Subscriber implements Runnable, ExceptionListener {

	Logger log = LoggerFactory.getLogger(this.getClass());

	private List<Flight> delays;
	private MessageDao messageDao;

	private static final String JMS_URL = "tcp://localhost:61616";
	private static final String TOPIC_NAME = "FlightUpdate";

	public Subscriber() {
	}

	public Subscriber(MessageDao messageDao) {
		super();
		this.messageDao = messageDao;
	}

	/**
	 * Create a connection and session and listen to specified Topic on
	 * specified host
	 */
	@Override
	public void run() {
		try {
			System.out.println("Subscriber run method called");

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					JMS_URL);
			Connection connection = connectionFactory.createConnection();
			connection.start();
			connection.setExceptionListener(this);

			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);

			Topic destination = session.createTopic(TOPIC_NAME);
			MessageConsumer consumer = session.createConsumer(destination);
			boolean flag = true;

			/**
			 * Wait for message. If the message is an object message, then if
			 * its String Property is "Flight Arrived", mark the corresponding
			 * flight as "arrived" in the database. If the message String
			 * Property is Flights Delayed, iterate over the list and mark each
			 * one as "late" in the database and increment its ETA by one;
			 */

			while (flag) {
				log.debug("Waiting for messages...");
				Message message = consumer.receive();
				if (message != null) {

					if (message instanceof ObjectMessage) {
						ObjectMessage objMessage = (ObjectMessage) message;

						if (objMessage.getStringProperty("FlightStatus")
								.equals("Flight Arrived")) {

							Flight flight = (Flight) objMessage.getObject();
							messageDao.setArrived(flight.getFlightId());
							log.debug("JMS: Flight from "
									+ flight.getOrigin().getCity() + " to "
									+ flight.getDestination().getCity()
									+ " has arrived");

						} else if (objMessage.getStringProperty("FlightStatus")
								.equals("Flights Delayed")) {

							@SuppressWarnings("unchecked")
							List<Flight> flights = (List<Flight>) objMessage
									.getObject();

							for (Flight flight : flights) {
								log.debug("JMS: Flight from "
										+ flight.getOrigin().getCity() + " to "
										+ flight.getDestination().getCity()
										+ " is delayed.");
								messageDao.setDelay(flight.getFlightId());
							}
						}
					}
				}
			}
			consumer.close();
			session.close();
			connection.close();
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	@Override
	public void onException(JMSException ex) {
		System.out.println("JMS Exception occured.  Shutting down client.");
	}

	public List<Flight> getDelays() {
		return delays;
	}

	public void setDelays(List<Flight> delays) {
		this.delays = delays;
	}

}

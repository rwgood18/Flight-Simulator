package com.cooksys.core.beans;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.core.models.Flight;


@Component
@Scope("singleton")
public class NotificationHandler implements ExceptionListener {
	
	private static final String JMS_URL = "tcp://localhost:61616";
	private static final String TOPIC_NAME = "FlightUpdate";
	
	private TopicConnectionFactory connFactory;
	private TopicConnection connection;
	
	private Topic topic;
	
	private Boolean connected = false;
	
	@PostConstruct
	public void init(){
		
		this.connFactory = new ActiveMQConnectionFactory(JMS_URL);
		try {
			this.connection = connFactory.createTopicConnection();
				this.connection.setExceptionListener(this);
				this.connection.start();
			
			try(TopicSession session = this.connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE)){
				this.topic = session.createTopic(TOPIC_NAME);
			}
			
			connected = true;
			
		} catch (JMSException e) {
			this.connFactory = null;
			this.connection = null;
			this.topic = null;
			
			e.printStackTrace();
		}
		
		
		
	}
	
	@PreDestroy
	public void shutdown(){
		this.connected = false;
		try{
			this.connection.stop();
			this.connection.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void handleFlightDelay(Collection<Flight> delayedFlights){
		ArrayList<Flight> flights = new ArrayList<>(delayedFlights);
		
		if(!connected){
			Exception e = new RuntimeException("JMS Not Connected");
			e.printStackTrace();
			
			return;
		}
		
		try(TopicSession session = this.connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE)){
			try(TopicPublisher publisher = session.createPublisher(this.topic)){
				ObjectMessage objM = session.createObjectMessage(flights);
				
				objM.setStringProperty("FlightStatus", "Flights Delayed");
				
				publisher.publish(objM);
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void handleFlightArrival(Flight completedFlight){
		if(!connected){
			Exception e = new RuntimeException("JMS Not Connected");
			e.printStackTrace();
			
			return;
		}
		
		try(TopicSession session = this.connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE)){
			try(TopicPublisher publisher = session.createPublisher(this.topic)){
				ObjectMessage objM = session.createObjectMessage(completedFlight);
				
				objM.setStringProperty("FlightStatus", "Flight Arrived");
				
				publisher.publish(objM);
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onException(JMSException arg0) {
		
		this.connected = false;
		
		try {
			this.connection.setExceptionListener(null);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		new Thread(this::handleReconnect).start();
	}
	
	public void handleReconnect(){
		
		while(!this.connected){
			this.init();
			if(!this.connected) {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
		
	}
	
}

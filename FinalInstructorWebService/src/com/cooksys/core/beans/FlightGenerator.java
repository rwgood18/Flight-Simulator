package com.cooksys.core.beans;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.core.models.Flight;
import com.cooksys.core.models.Location;
import com.cooksys.core.models.Plane;

@Component
@Scope("singleton")
public class FlightGenerator {
	
	@Autowired
	private LocationGenerator locationGenerator;
	
	@Autowired
	private NotificationHandler notificationHandler;
	
	List<Location> major;
	List<Location> minor;
	
	private List<Plane> allPlanes = new ArrayList<>();
	
	private SecureRandom random = new SecureRandom();
	
	private Thread tickThread;
	
	private Long startTime = System.currentTimeMillis();
	private Integer currentDay = 0;

	@PostConstruct
	public void init(){
		System.out.println("Begin init");
		
		major = locationGenerator.getMajorLocations();
		minor = locationGenerator.getMinorLocations();
		
		int i = 0;
		while(i++ < 9){
			Location origin = major.get(random.nextInt(major.size()));
			
			Flight flight = generateFlight(origin, 0);
			
			Location destination = flight.getDestination();

			Plane plane = new Plane(origin, destination);
				plane.getFlightDeque().add(flight);
			
			this.allPlanes.add(plane);
			
		}
		
		for(Plane plane : this.allPlanes){
			Flight flight = plane.getFlightDeque().getFirst();
			
			int a = 0;
			while(a++ < 5){
				flight = generateFlight(flight.getDestination(), flight.getDeparture() + flight.getEta());
				
				plane.getFlightDeque().add(flight);
			}
		}
		
		this.tickThread = new Thread(this::tick);
		
		this.tickThread.start();
		
		System.out.println("init Complete");
	}
	
	@PreDestroy
	public void shutdown(){
		this.tickThread.interrupt();
	}
	
	public void tick(){
		System.out.println("Begin Tick");
		while(!Thread.currentThread().isInterrupted()){
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				return;
			}
			
			for(Plane plane : this.allPlanes){
				Flight f = plane.getFlightDeque().getFirst();
				
				boolean delayed = f.getDeparture() == 0 ? random.nextInt(100) < 10 : false;
				
				if(!delayed){
					f.setEta(f.getEta() - 1);
					
					for(Flight flight : plane.getFlightDeque()){
						flight.setDeparture(flight.getDeparture() - 1);
					}
					
					if(f.getEta() == 0){
						notificationHandler.handleFlightArrival(f);
						plane.getFlightDeque().remove(f);
						
						Flight lastQueuedFlight = plane.getFlightDeque().getLast();
						Flight nextFlight = generateFlight(lastQueuedFlight.getDestination(), lastQueuedFlight.getDeparture() + lastQueuedFlight.getEta());
						
						plane.getFlightDeque().add(nextFlight);
						
						plane.setCurrentLocation(plane.getNextLocation());
						plane.setNextLocation(nextFlight.getDestination());
					}
					
				}else{
					notificationHandler.handleFlightDelay(plane.getFlightDeque());
				}
			}
			
			this.currentDay++;
			
			System.out.println("Day " + this.currentDay);
		}
	}
	
	public Flight generateFlight(Location origin, Integer departure){
		Flight flight = new Flight();
		
		List<Location> possibleDestinations = getNextPossibleDestinations(origin);
		Location destination = possibleDestinations.get(random.nextInt(possibleDestinations.size()));
		
		flight.setFlightId(Flight.getNextFlightID());
		
		flight.setOrigin(origin);
		flight.setDestination(destination);
		flight.setDeparture(departure);
		flight.setEta(random.nextInt(2) + 1);
		
		return flight;
	}
	
	
	public List<Location> getNextPossibleDestinations(Location l){
		List<Location> locations = new ArrayList<>();
		
		boolean isMajor = major.contains(l);
		
		List<Location> minorLocations = minorLocationsInState(l);
		
		locations.addAll(minorLocations);
		
		if(!isMajor){
			locations.addAll(minorLocations);
			locations.addAll(major);
		}
		
		return locations;
	}
	
	public List<Location> minorLocationsInState(Location l){
		List<Location> locations = new ArrayList<>();
		
		for(Location location : minor){
			if(location.getState().equals(l.getState())) locations.add(location);
		}
		
		return locations;
	}
	
	
	public List<Plane> getAllPlanes() {
		return allPlanes;
	}

	public void setAllPlanes(List<Plane> allPlanes) {
		this.allPlanes = allPlanes;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Integer getCurrentDay() {
		return currentDay;
	}

	public void setCurrentDay(Integer currentDay) {
		this.currentDay = currentDay;
	}
	
}

package com.cooksys.frontend.beans.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cooksys.core.models.Flight;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetRoutesWrapper {

	@XmlElement
	private String origin;
	@XmlElement
	private String dest;
	@XmlElement
	private List<Flight> flights;

	public GetRoutesWrapper() {
	}

	public GetRoutesWrapper(String origin, String dest, List<Flight> flights) {
		super();
		this.origin = origin;
		this.dest = dest;
		this.flights = flights;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

}

package com.cooksys.backend.beans.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cooksys.core.models.Flight;

/**
 * Wrapper for transporting a list of Flights
 * 
 * @author Russell Good
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RouteWrapper {

	@XmlElement
	private List<Flight> route;

	public List<Flight> getRoute() {
		return route;
	}

	public void setRoute(List<Flight> route) {
		this.route = route;
	}

}
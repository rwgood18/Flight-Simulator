package com.cooksys.frontend.beans.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cooksys.core.models.Flight;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FlightWrapper {

	@XmlElement
	Flight flight;

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

}

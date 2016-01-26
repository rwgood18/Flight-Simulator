package com.cooksys.backend.beans.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cooksys.backend.model.PlaneFlight;

/**
 * Wrapper for transporting a List of PlaneFlights
 * 
 * @author Russell Good
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PlaneFlightWrapper {

	@XmlElement
	private List<PlaneFlight> items;

	public List<PlaneFlight> getItems() {
		return items;
	}

	public void setItems(List<PlaneFlight> items) {
		this.items = items;
	}
}

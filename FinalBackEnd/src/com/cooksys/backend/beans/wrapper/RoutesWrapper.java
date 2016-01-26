package com.cooksys.backend.beans.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper for transport a List of routes which are themselves lists
 * 
 * @author Russell Good
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RoutesWrapper {

	@XmlElement
	private List<RouteWrapper> routes;

	public List<RouteWrapper> getRoutes() {
		return routes;
	}

	public void setRoutes(List<RouteWrapper> routes) {
		this.routes = routes;
	}

}

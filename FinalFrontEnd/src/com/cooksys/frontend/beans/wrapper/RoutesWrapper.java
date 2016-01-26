package com.cooksys.frontend.beans.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
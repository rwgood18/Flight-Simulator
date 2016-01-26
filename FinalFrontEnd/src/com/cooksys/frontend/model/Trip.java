package com.cooksys.frontend.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// Generated Jun 5, 2015 9:53:22 AM by Hibernate Tools 4.3.1

/**
 * Trip generated by hbm2java
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Trip implements java.io.Serializable {

	@XmlElement
	private Integer id;
	@XmlElement
	private User user;
	@XmlElement
	private boolean cancelled;
	@XmlElement
	private String origin;
	@XmlElement
	private String destination;

	public Trip() {
	}

	public Trip(boolean cancelled, String origin, String destination) {
		this.cancelled = cancelled;
		this.origin = origin;
		this.destination = destination;
	}

	public Trip(User user, boolean cancelled, String origin, String destination) {
		this.user = user;
		this.cancelled = cancelled;
		this.origin = origin;
		this.destination = destination;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}

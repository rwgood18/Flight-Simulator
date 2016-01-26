package com.cooksys.frontend.beans;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.frontend.beans.dao.FlightDao;
import com.cooksys.frontend.beans.dao.UserDao;
import com.cooksys.frontend.model.User;

/**
 * Get, save, and hold user profile information
 * 
 * @author Russell Good
 *
 */
@Component
@Scope("view")
public class ProfileBean {

	@Autowired
	private AuthenticationBean auth;
	@Autowired
	private UserDao userDao;
	@Autowired
	private FlightDao flightDao;

	private User user;

	@PostConstruct
	public void init() {
		user = auth.getUser();
	}

	// initialize this user to the desired user and redirect
	// to the home page
	public String selectUser(User user) {
		this.user = user;
		return "home";
	}

	// update user information with the information on the page
	public String update() {
		this.user = userDao.updateUser(this.user);
		return "profile";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

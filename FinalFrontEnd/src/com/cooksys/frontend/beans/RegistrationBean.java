package com.cooksys.frontend.beans;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.frontend.beans.dao.UserDao;
import com.cooksys.frontend.model.User;

@Component
@Scope("request")
public class RegistrationBean {

	@Autowired
	private UserDao userDao;

	private User user;

	@PostConstruct
	public void init() {
		user = new User();
	}

	public String register() {
		boolean registered = userDao.registerUser(user);
		return (registered) ? "registration-success" : "registration-failure";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

package com.cooksys.frontend.beans;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.frontend.beans.dao.UserDao;
import com.cooksys.frontend.model.User;

/**
 * Authenticate user
 * 
 * @author Russell Good
 *
 */
@Component
@Scope("session")
public class AuthenticationBean {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDao userDao;

	private User user;

	public boolean isLoggedIn() {
		return user != null;
	}

	@PreDestroy
	private void destroy() {
		user = new User();
	}

	public String getUserName() {
		String userName = null;
		if (user != null) {
			userName = user.getUsername();
		}
		return userName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
package com.cooksys.frontend.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.frontend.beans.dao.UserDao;
import com.cooksys.frontend.model.User;

/**
 * Login user and call methods in other beans
 * that initialize their fields for this user.
 * 
 * @author Russell Good
 *
 */
@Component
@Scope("request")
public class LoginBean {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthenticationBean auth;
	@Autowired
	private ProfileBean profileBean;
	@Autowired
	private TripBean tripBean;
	@Autowired
	private UserDao userDao;

	private User user;
	private Boolean loginFailed;
	private String password;

	@PostConstruct
	private void init() {
		user = new User();
	}
	
	@PreDestroy
	private void destroy() {
		user = new User();
	}
	
	//login user
	public String login() {
		log.debug("Logging in...");
		if ((user = userDao.login(user)) != null) {
			auth.setUser(user);

			loginFailed = false;
			profileBean.setUser(user);
			tripBean.selectUser(user);
			

			log.debug("Login success!");
			return "login-success";
		} else {
			log.debug("Login failure!");
			loginFailed = true;
			return "login-failure";
		}
	}

	//redirect to registration page
	public String register() {
		return "register";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getLoginFailed() {
		return loginFailed;
	}

	public void setLoginFailed(Boolean loginFailed) {
		this.loginFailed = loginFailed;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
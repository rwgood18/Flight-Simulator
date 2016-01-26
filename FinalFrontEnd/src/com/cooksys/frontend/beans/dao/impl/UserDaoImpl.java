package com.cooksys.frontend.beans.dao.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.cooksys.frontend.beans.dao.UserDao;
import com.cooksys.frontend.model.User;

/**
 * Send requests server side to save and retrieve User information.
 * 
 * @author Russell Good
 */
@Repository
@Transactional
public class UserDaoImpl implements UserDao {

	Logger log = LoggerFactory.getLogger(this.getClass());
	RestTemplate rt = new RestTemplate();

	/**
	 * Send new information to server to update user
	 */
	@Override
	public User updateUser(User user) {
		return rt.postForObject(
				"http://localhost:8080/FinalBackEnd/updateUser", user,
				User.class);
	}

	public User login(User user) {
		User u = rt.postForObject("http://localhost:8080/FinalBackEnd/login",
				user, User.class);
		return u;
	}

	@Override
	public boolean registerUser(User user) {
		rt.postForObject("http://localhost:8080/FinalBackEnd/register", user,
				User.class);
		return true;
	}

	@Override
	public User getUserByUsername(String username) {

		return rt
				.getForObject(
						"http://localhost:8080/FinalBackEnd/getUserByUsername/" + username,
						User.class);
	}

}

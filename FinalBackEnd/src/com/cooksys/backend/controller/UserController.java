package com.cooksys.backend.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.backend.beans.dao.UserDao;
import com.cooksys.backend.model.User;

/**
 * Accept HTTP requests pertaining to User
 * 
 * @author Russell Good
 *
 */
@Controller
public class UserController {
	
	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDao userDao;
	
	/**
	 * Take in User and hash their password before passing the
	 * user on the be saved to the database via userDao.
	 * 
	 * @param User
	 * @return User
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody User register(@RequestBody User user) {
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		userDao.registerUser(user);
		return userDao.getUserByUsername(user.getUsername());
	}

	/**
	 * Take in User and update their information in the database
	 * via userDao.
	 * 
	 * @param User
	 * @return User
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public @ResponseBody User updateUser(@RequestBody User user) {
		return userDao.updateUser(user);
	}

	@RequestMapping(value = "/getUserByUsername/{username}")
	public @ResponseBody User getUserByUsername(@PathVariable String username) {
		System.out.println("Get user by username called");
		return userDao.getUserByUsername(username);

	}

}

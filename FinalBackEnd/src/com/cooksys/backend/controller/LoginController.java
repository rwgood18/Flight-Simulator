package com.cooksys.backend.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.backend.beans.dao.MessageDao;
import com.cooksys.backend.beans.dao.UserDao;
import com.cooksys.backend.beans.jms.Subscriber;
import com.cooksys.backend.model.User;

/**
 * Handle all request that pertain to login process.
 * 
 * @author Russell Good
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDao userDao;
	@Autowired
	private MessageDao messageDao;

	/**
	 * Get user from database and compare that password to the entered
	 * password. If they match, the login is successful and the from the 
	 * database is returned.
	 * 
	 * @param user
	 * @return User if login was successful; null if it was not
	 */
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody User login(@RequestBody User user) {
		User u = null;
		String password = null;
		log.debug("Attempting login with: Username=" + user.getUsername()
				+ " Password=" + user.getPassword());

		System.out.println("Password = " + user.getPassword() + "\n" + "Username = " + user.getUsername());
		if (user != null && user.getUsername() != null
				&& user.getPassword() != null) {

			String userName = user.getUsername().trim();
			password = user.getPassword().trim();

			if (!userName.isEmpty() && !password.isEmpty()) {
				User dbUser = userDao.getUserByUsername(userName);

				if (dbUser != null && BCrypt.checkpw(password, dbUser.getPassword())) {
					u = dbUser;
					startSubscriber();
				}
			}
		}
		return u;
	}

	// start jms subscriber
	private void startSubscriber() {
		Thread subscriberThread = new Thread(new Subscriber(messageDao));
		subscriberThread.start();
		log.debug("SubscriberThead started");

	}
}

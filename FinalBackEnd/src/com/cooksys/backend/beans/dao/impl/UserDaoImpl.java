package com.cooksys.backend.beans.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cooksys.backend.beans.dao.UserDao;
import com.cooksys.backend.model.User;

/**
 * Data access layer for all transactions related to the 
 * User table in the database.
 * 
 * @author Russell Good
 *
 */
@Repository
@Transactional
public class UserDaoImpl implements UserDao {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SessionFactory factory;

	@Override
	public User updateUser(User user) {
		factory.getCurrentSession().update(user);
		return user;
	}

	@Override
	public boolean registerUser(User user) {
		factory.getCurrentSession().save(user);
		log.debug("Successfully registered " + user.getUsername());
		return true;
	}

	@Override
	public User getUserByUsername(String userName) {
		return (User) factory
				.getCurrentSession()
				.createQuery(
						"SELECT u FROM User u WHERE u.username = :userName")
				.setString("userName", userName).uniqueResult();
	}

}

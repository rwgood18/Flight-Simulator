package com.cooksys.frontend.beans.dao;

import com.cooksys.frontend.model.User;

public interface UserDao {

	public User updateUser(User user);

	public boolean registerUser(User user);

	public User getUserByUsername(String userName);
	
	public User login(User user);
}

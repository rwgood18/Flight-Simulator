package com.cooksys.backend.beans.dao;

import com.cooksys.backend.model.User;

public interface UserDao {

	public User updateUser(User user);

	public boolean registerUser(User user);

	public User getUserByUsername(String userName);
}

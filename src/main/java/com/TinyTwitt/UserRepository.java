package com.TinyTwitt;

import static com.TinyTwitt.OfyService.ofy;

import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public class UserRepository {
	private static UserRepository userRepository = null;

	static {
		ObjectifyService.register(User.class);
	}

	private UserRepository() {
	}

	public static synchronized UserRepository getInstance() {
		if (null == userRepository) {
			userRepository = new UserRepository();
		}
		return userRepository;
	}

	public Collection<User> findUsers() {
		List<User> users = ofy().load().type(User.class).list();
		return users;
	}

	public User createUser(User user) {
		ofy().save().entity(user).now();
		return user;
	}

	public User updateUser(User updatedUser) {
		if (updatedUser.getId() == null) {
			return null;
		}
		User user = ofy().load().type(User.class).id(updatedUser.getId()).now();
		if (updatedUser.getUsername() != null) {
			user.setUsername(updatedUser.getUsername());
		}
		ofy().save().entity(user).now();
		return user;
	}
	
	public void removeUser(Long id) {
		if (id == null) {
			return;
		}
		ofy().delete().type(User.class).id(id).now();
	}
	
	public User findUser(Long id) {
		User user = ofy().load().type(User.class).id(id).now();
		return user;
	}
}

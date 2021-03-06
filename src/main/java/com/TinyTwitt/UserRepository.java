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

	public Collection<User> findUsers(int limit) {
		List<User> users = ofy().load().type(User.class).limit(limit).list();
		return users;
	}
	
	public Collection<User> findUsers(String username, int limit) {
		List<User> users = ofy().load().type(User.class).filter("username >=", username).filter("username <", username + "\uFFFD").limit(limit).list();
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
		if (updatedUser.getFollowers() != null) {
			user.setFollowers(updatedUser.getFollowers());
		}
		if (updatedUser.getFollowing() != null) {
			user.setFollowing(updatedUser.getFollowing());
		}
		
		if (updatedUser.getProfilePic() != null) {
			user.setProfilePic(updatedUser.getProfilePic());
		}
		ofy().save().entity(user).now();
		return user;
	}
	
	public User findUser(String id) {
		User user = ofy().load().type(User.class).id(id).now();
		if (user == null) {
			return null;
		} else {
			return user;
		}
	}
	
	public void removeUser(String id) {
		if (id == null) {
			return;
		}
		ofy().delete().type(User.class).id(id).now();
	}
	
	public void deleteAllUsers() {
		Iterable<Key<User>> users = ofy().load().type(User.class).keys();
		ofy().delete().keys(users);
	}
}

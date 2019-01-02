package com.TinyTwitt;

import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.Key;

import com.TinyTwitt.PMF;

@Api(name = "UserEndpoint", namespace = @ApiNamespace(ownerDomain = "TinyTweet.com", ownerName = "TinyTweet.com", packagePath = "users"))
public class UserEndpoint {
	
	@SuppressWarnings({ "null" })
	@ApiMethod(name = "addUser")
	public UserEntity addUser(@Named("userId") Key userId, @Named("username") String username) throws EntityExistsException {
		PersistenceManager pmr = getPersistenceManager();
		UserEntity userEntity = null;
		try {
			userEntity.setId(userId);
			userEntity.setUsername(username);
			if (containsUserEntity(userEntity)) {
				throw new EntityExistsException("User already exists");
			}
			pmr.makePersistent(userEntity);
		} finally {
			pmr.close();
		}
		return userEntity;
	}
	
	@ApiMethod(name = "getUser")
	public UserEntity getUser(@Named("userId") Key userId) throws EntityNotFoundException {
		PersistenceManager pmr = getPersistenceManager();
		UserEntity userEntity = null;
		try {
			if (containsUserEntity(pmr.getObjectById(UserEntity.class, userId))) {
				userEntity = pmr.getObjectById(UserEntity.class, userId);
			} else {
				throw new EntityNotFoundException("User does not exist !");
			}
		} finally {
			pmr.close();
		}
		return userEntity;
	}
	
	@ApiMethod(name = "followUser")
	public void followUser (@Named("userId") Key userId, Key userToFollow) throws EntityNotFoundException {
		PersistenceManager pmr = getPersistenceManager();
		try {
			if (!containsUserEntity(pmr.getObjectById(UserEntity.class, userToFollow))){
				throw new EntityNotFoundException("Object does not exist");
			}
			UserEntity userFollowing = pmr.getObjectById(UserEntity.class, userId);
			UserEntity userFollowed = pmr.getObjectById(UserEntity.class, userToFollow);
			if (!userFollowing.following.contains(userToFollow) && !userFollowed.followers.contains(userId)) {
				userFollowing.following.add(userToFollow);
				userFollowed.followers.add(userId);
			} else {
				userFollowing.following.remove(userToFollow);
				userFollowed.followers.remove(userId);
			}
		} finally {
			pmr.close();
		}
	}
	
	private boolean containsUserEntity(UserEntity userEntity) {
		PersistenceManager pmr = getPersistenceManager();
		boolean contains = true;
		try {
			pmr.getObjectById(UserEntity.class, userEntity.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			pmr.close();
		}
		return contains;
	}
	
	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}

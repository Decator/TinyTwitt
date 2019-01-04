package com.TinyTwitt;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.*;

@Entity
public class User {
	@Id
	String id;

	String username;
	
	Set<String> followers = new HashSet<String>();

	Set<String> following = new HashSet<String>();

	public User() {
	}

	public User(String username, Set<String> followers, Set<String> following) {
		this.username = username;
		this.followers = followers;
		this.following = following;
	}

	public User(String id, String username, Set<String> followers, Set<String> following) {
		this.id = id;
		this.username = username;
		this.followers = followers;
		this.following = following;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<String> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<String> followers) {
		this.followers = followers;
	}

	public Set<String> getFollowing() {
		return following;
	}

	public void setFollowing(Set<String> following) {
		this.following = following;
	}
	
	public void addFollower(String idFollower) {
		this.followers.add(idFollower);
	}
	
	public void removeFollower(String idFollower) {
		this.followers.remove(idFollower);
	}
	
	public void addFollowing(String idFollowing) {
		this.following.add(idFollowing);
	}
	
	public void removeFollowing(String idFollowing) {
		this.following.remove(idFollowing);
	}
	
}

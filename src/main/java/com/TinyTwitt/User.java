package com.TinyTwitt;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.*;

@Entity
public class User {
	@Id
	Long id;

	String username;
	
	Set<Long> followers = new HashSet<Long>();

	Set<Long> following = new HashSet<Long>();
	
	Set<Message> timeline = new HashSet<Message>();

	public User() {
	}

	public User(String username, Set<Long> followers, Set<Long> following, Set<Message> timeline) {
		this.username = username;
		this.followers = followers;
		this.following = following;
		this.timeline = timeline;
	}

	public User(Long id, String username, Set<Long> followers, Set<Long> following, Set<Message> timeline) {
		this.id = id;
		this.username = username;
		this.followers = followers;
		this.following = following;
		this.timeline = timeline;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Long> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<Long> followers) {
		this.followers = followers;
	}

	public Set<Long> getFollowing() {
		return following;
	}

	public void setFollowing(Set<Long> following) {
		this.following = following;
	}

	public Set<Message> getTimeline() {
		return timeline;
	}

	public void setTimeline(Set<Message> timeline) {
		this.timeline = timeline;
	}
	
	public void addFollower(Long idFollower) {
		this.followers.add(idFollower);
	}
	
	public void removeFollower(Long idFollower) {
		this.followers.remove(idFollower);
	}
	
	public void addFollowing(Long idFollowing) {
		this.following.add(idFollowing);
	}
	
	public void removeFollowing(Long idFollowing) {
		this.following.remove(idFollowing);
	}
	
}

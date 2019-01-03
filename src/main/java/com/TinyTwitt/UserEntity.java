package com.TinyTwitt;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;

import com.TinyTwitt.PMF;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class UserEntity {
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	String id;

	@Persistent
	String username;
	
	@Persistent 
	Set<String> followers = new HashSet<String>();
	
	@Persistent
	@ApiResourceProperty(ignored=AnnotationBoolean.TRUE)
	Set<String> following = new HashSet<String>();
	
	@Persistent
	@ApiResourceProperty(ignored=AnnotationBoolean.TRUE)
	Set<String> timeline = new HashSet<String>();
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<String> getFollowers() {
		return this.followers;
	}

	public void setFollowers(Set<String> followers) {
		this.followers = followers;
	}

	public Set<String> getFollowing() {
		return this.following;
	}

	public void setFollowing(Set<String> following) {
		this.following = following;
	}
	
	public void addFollower(String followerId) {
		this.followers.add(followerId);
	}

	public void addFollowing(String followingId) {
		this.following.add(followingId);
	}
	
	public Set<String> getTimeline() {
		return this.timeline;
	}

	public void setTimeline(Set<String> timeline) {
		this.timeline = timeline;
	}
	
	public void addingTimeline(String messageId) {
		this.timeline.add(messageId);
	}
}

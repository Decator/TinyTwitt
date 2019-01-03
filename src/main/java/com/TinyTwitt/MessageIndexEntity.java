package com.TinyTwitt;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import com.TinyTwitt.PMF;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class MessageIndexEntity {
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private String id;
	
	@Persistent
	Set<String> receivers = new HashSet<String>();
	
	@Persistent
	Set<String> hashtags = new HashSet<String>();
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setReceivers(Set<String> receivers) {
		this.receivers = receivers;
	}
	
	public Set<String> getReceivers(){
		return this.receivers;
	}
	
	public void addReceiver(String idReceiver) {
		this.receivers.add(idReceiver);
	}
	
	public void removeReceiver(String idReceiver) {
		this.receivers.remove(idReceiver);
	}
	
	public void setHashtags(Set<String> hashtags) {
		this.hashtags = hashtags;
	}
	
	public Set<String> getHashtags(){
		return this.hashtags;
	}
	
	public void addHashtag(String hashtag) {
		this.hashtags.add(hashtag);
	}
	
	public void removeHashtag(String hashtag) {
		this.hashtags.remove(hashtag);
	}
}

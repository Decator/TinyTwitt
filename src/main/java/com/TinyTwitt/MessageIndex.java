package com.TinyTwitt;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;


@Entity
public class MessageIndex {
	@Id
	private String id;
	@Parent
	Key<Message> messageEntity;
	@Index
	String owner;
	@Index
	Set<String> receivers = new HashSet<String>();
	@Index
	Set<String> hashtags = new HashSet<String>();
	@Index
	String date;
	
	public MessageIndex() {
	}
	

	public MessageIndex(Key<Message> messageEntity, Set<String> receivers, String date, String owner) {
		this.messageEntity = messageEntity;
		this.receivers = receivers;
		this.date = date;
		this.owner = owner;
	}

	public MessageIndex(Key<Message> messageEntity, Set<String> receivers, Set<String> hashtags, String date,String owner) {
		this.messageEntity = messageEntity;
		this.receivers = receivers;
		this.hashtags = hashtags;
		this.date = date;
		this.owner = owner;
	}
	
	public MessageIndex(String id, Key<Message> messageEntity, Set<String> receivers, String date, String owner) {
		this.id = id;
		this.messageEntity = messageEntity;
		this.receivers = receivers;
		this.date = date;
		this.owner = owner;
	}

	public MessageIndex(String id, Key<Message> messageEntity, Set<String> receivers, Set<String> hashtags, String date, String owner) {
		this.id = id;
		this.messageEntity = messageEntity;
		this.receivers = receivers;
		this.hashtags = hashtags;
		this.date = date;
		this.owner = owner;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Key<Message> getMessageEntity() {
		return messageEntity;
	}

	public void setMessageEntity(Key<Message> messageEntity) {
		this.messageEntity = messageEntity;
	}

	public Set<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(Set<String> receivers) {
		this.receivers = receivers;
	}
	
	public void addReceiver(String receiver) {
		this.receivers.add(receiver);
	}
	
	public void removeReceiver(String receiver) {
		this.receivers.remove(receiver);
	}

	public Set<String> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<String> hashtags) {
		this.hashtags = hashtags;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}

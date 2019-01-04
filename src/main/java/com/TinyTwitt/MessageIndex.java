package com.TinyTwitt;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;


@Entity
public class MessageIndex {
	@Id
	private Long id;
	@Parent
	Key<Message> messageEntity;
	@Index
	Long owner;
	@Index
	Set<Long> receivers = new HashSet<Long>();
	@Index
	Set<String> hashtags = new HashSet<String>();
	@Index
	String date;
	
	public MessageIndex() {
	}
	

	public MessageIndex(Key<Message> messageEntity, Set<Long> receivers, String date, Long owner) {
		this.messageEntity = messageEntity;
		this.receivers = receivers;
		this.date = date;
		this.owner = owner;
	}

	public MessageIndex(Key<Message> messageEntity, Set<Long> receivers, Set<String> hashtags, String date,Long owner) {
		this.messageEntity = messageEntity;
		this.receivers = receivers;
		this.hashtags = hashtags;
		this.date = date;
		this.owner = owner;
	}

	public MessageIndex(Long id, Key<Message> messageEntity, Set<Long> receivers, Set<String> hashtags, String date, Long owner) {
		this.id = id;
		this.messageEntity = messageEntity;
		this.receivers = receivers;
		this.hashtags = hashtags;
		this.date = date;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key<Message> getMessageEntity() {
		return messageEntity;
	}

	public void setMessageEntity(Key<Message> messageEntity) {
		this.messageEntity = messageEntity;
	}

	public Set<Long> getReceivers() {
		return receivers;
	}

	public void setReceivers(Set<Long> receivers) {
		this.receivers = receivers;
	}
	
	public void addReceiver(Long receiver) {
		this.receivers.add(receiver);
	}
	
	public void removeReceiver(Long receiver) {
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


	public Long getOwner() {
		return owner;
	}


	public void setOwner(Long owner) {
		this.owner = owner;
	}
	
	
	
	
}

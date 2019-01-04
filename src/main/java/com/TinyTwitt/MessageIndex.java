package com.TinyTwitt;

import java.util.Date;
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
	Set<Long> receivers = new HashSet<Long>();
	@Index
	Set<String> hashtags = new HashSet<String>();
	@Index
	Date date = new Date();
	
	public MessageIndex() {
	}
	

	public MessageIndex(Key<Message> messageEntity, Set<Long> receivers) {
		this.messageEntity = messageEntity;
		this.receivers = receivers;
	}

	public MessageIndex(Key<Message> messageEntity, Set<Long> receivers, Set<String> hashtags) {
		this.messageEntity = messageEntity;
		this.receivers = receivers;
		this.hashtags = hashtags;
	}

	public MessageIndex(Long id, Key<Message> messageEntity, Set<Long> receivers, Set<String> hashtags) {
		this.id = id;
		this.messageEntity = messageEntity;
		this.receivers = receivers;
		this.hashtags = hashtags;
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

	public Set<String> getHashtags() {
		return hashtags;
	}

	public void setHashtags(Set<String> hashtags) {
		this.hashtags = hashtags;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
	
}

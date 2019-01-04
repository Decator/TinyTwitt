package com.TinyTwitt;

import java.util.Date;

import com.googlecode.objectify.annotation.*;

@Entity
public class Message {
	
	@Id
	private Long id;
	
	private String sender;
	@Index
	private Long owner;

	private String body;

	private Date date;

	public Message() {
	}

	public Message(String sender, Long owner, String body, Date date) {
		this.sender = sender;
		this.owner = owner;
		this.body = body;
		this.date = date;
	}

	public Message(Long id, String sender, Long owner, String body, Date date) {
		this.id = id;
		this.sender = sender;
		this.owner = owner;
		this.body = body;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}

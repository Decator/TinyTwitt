package com.TinyTwitt;


import com.googlecode.objectify.annotation.*;

@Entity
public class Message {
	
	@Id
	private String id;
	
	private String sender;
	@Index
	private String owner;

	private String body;
	@Index
	private String date;

	public Message() {
	}

	public Message(String sender, String owner, String body, String date) {
		this.sender = sender;
		this.owner = owner;
		this.body = body;
		this.date = date;
	}

	public Message(String id, String sender, String owner, String body, String date) {
		this.id = id;
		this.sender = sender;
		this.owner = owner;
		this.body = body;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}

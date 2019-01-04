package com.TinyTwitt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

import static com.TinyTwitt.OfyService.ofy;

import com.TinyTwitt.Message;

public class MessageRepository {
	
	private static MessageRepository messageRepository = null;

	static {
		ObjectifyService.register(Message.class);
	}

	private MessageRepository() {
	}

	public static synchronized MessageRepository getInstance() {
		if (null == messageRepository) {
			messageRepository = new MessageRepository();
		}
		return messageRepository;
	}

	public Collection<Message> findMessages(int limit) {
		List<Message> messages = ofy().load().type(Message.class).limit(limit).list();
		return messages;
	}

	public Message createMessage(Message message) {
		ofy().save().entity(message).now();
		return message;
	}

	public Message updateMessage(Message updatedMessage) {
		if (updatedMessage.getId() == null) {
			return null;
		}
		Message message = ofy().load().type(Message.class).id(updatedMessage.getId()).now();
		if (updatedMessage.getDate() != null) {
			message.setDate(updatedMessage.getDate());
		}
		if (updatedMessage.getBody() != null) {
			message.setBody(updatedMessage.getBody());
		}
		if (updatedMessage.getOwner() != null) {
		message.setOwner(updatedMessage.getOwner());
		}
		if (updatedMessage.getSender() != null) {
		message.setSender(updatedMessage.getSender());
		}
		ofy().save().entity(message).now();
		return message;
	}
	
	public void removeMessage(String id) {
		if (id == null) {
			return;
		}
		ofy().delete().type(Message.class).id(id).now();
	}
	
	public Message findMessage(String id) {
		Message message = ofy().load().type(Message.class).id(id).now();
		if (message == null) {
			return null;
		} else {
			return message;
		}
	}
	
	public void removeMessageUser(String userId) {
		Iterable<Key<Message>> messages = ofy().load().type(Message.class).filter("owner", userId).keys();
		ofy().delete().keys(messages);
	}
	
	public void deleteAllMessages() {
		Iterable<Key<Message>> messages = ofy().load().type(Message.class).keys();
		ofy().delete().keys(messages);
	}
	
	public List<Message> getMessagesFromMessageIndexes(List<MessageIndex> messageIndexes){
		List<Key<Message>> messageKeys = new ArrayList<Key<Message>>();
		messageIndexes.forEach (messageIndex -> messageKeys.add(messageIndex.getMessageEntity()));
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(ofy().load().keys(messageKeys).values());
		return messages;
	}
	
	public List<Message> myMessages(String userId, int limit){
		return ofy().load().type(Message.class).filter("owner", userId).order("-date").limit(limit).list();
	}
	
	public List<Message> myTimeline(List<MessageIndex> messageIndexes){
		List<Key<Message>> messageKeys = new ArrayList<Key<Message>>();
		messageIndexes.forEach(messageIndex -> messageKeys.add(messageIndex.getMessageEntity()));
		List<Message> messages = new ArrayList<Message>();
		messages.addAll(ofy().load().keys(messageKeys).values());
		return messages;
	}
}

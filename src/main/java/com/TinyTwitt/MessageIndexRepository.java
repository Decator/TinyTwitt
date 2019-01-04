package com.TinyTwitt;

import static com.TinyTwitt.OfyService.ofy;

import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public class MessageIndexRepository {
	private static MessageIndexRepository messageIndexRepository = null;

	static {
		ObjectifyService.register(Message.class);
	}

	private MessageIndexRepository() {
	}

	public static synchronized MessageIndexRepository getInstance() {
		if (null == messageIndexRepository) {
			messageIndexRepository = new MessageIndexRepository();
		}
		return messageIndexRepository;
	}

	public MessageIndex createMessageIndex(MessageIndex messageIndex) {
		ofy().save().entity(messageIndex).now();
		return messageIndex;
	}

	public MessageIndex updateMessageindex(MessageIndex updatedMessageIndex) {
		if (updatedMessageIndex.getId() == null) {
			return null;
		}
		MessageIndex messageIndex = ofy().load().key(Key.create(MessageIndex.class, updatedMessageIndex.getId())).now();
		messageIndex.setId(updatedMessageIndex.getId());
		messageIndex.setMessageEntity(updatedMessageIndex.getMessageEntity());
		messageIndex.setReceivers(updatedMessageIndex.getReceivers());
		messageIndex.setHashtags(updatedMessageIndex.getHashtags());
		ofy().save().entity(messageIndex).now();
		return messageIndex;
	}
	
	public void removeMessageIndex(Long id) {
		if (id == null) {
			return;
		}
		ofy().delete().type(MessageIndex.class).id(id).now();
	}
	
	public MessageIndex findMessageIndex(Long id) {
		MessageIndex messageIndex = ofy().load().type(MessageIndex.class).id(id).now();
		return messageIndex;
	}
}

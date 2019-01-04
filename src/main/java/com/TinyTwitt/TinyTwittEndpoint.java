package com.TinyTwitt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.DefaultValue;


import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityNotFoundException;

import com.googlecode.objectify.cmd.Query;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;

import com.TinyTwitt.MessageRepository;

import static com.TinyTwitt.OfyService.ofy;

@Api(name = "tinytwittendpoint", namespace = @ApiNamespace(ownerDomain = "TinyTwitt.com", ownerName = "TinyTwitt.com", packagePath = "services"))
public class TinyTwittEndpoint {
	
	public TinyTwittEndpoint() {}
	
	@ApiMethod(name = "addMessage", httpMethod = HttpMethod.POST, path = "users/{userId}/messages")
	public Message addMessage(@Named("userId") String userId, Message message, @Nullable @Named("hashtags") HashSet<String> hashtags ) {
		User user = UserRepository.getInstance().findUser(Long.valueOf(userId));
		message.setOwner(Long.valueOf(userId));
		Message newMessage = MessageRepository.getInstance().createMessage(message);
		MessageIndex messageIndex = new MessageIndex();
		if (hashtags != null) {
			messageIndex = new MessageIndex(Key.create(Message.class, message.getId()), user.getFollowers(), hashtags);
		} else {
			messageIndex = new MessageIndex(Key.create(Message.class, message.getId()), user.getFollowers());
		}
		MessageIndexRepository.getInstance().createMessageIndex(messageIndex);
		return newMessage;
	}
	
	@ApiMethod(name = "updateMessage", httpMethod = HttpMethod.PUT, path = "users/{userId}/messages")
	public Message updateMessage(@Named("userId") String userId, Message message) {
		if (Long.toString(message.getOwner()) == userId) {
			return MessageRepository.getInstance().updateMessage(message);
		} else 
			return null;
	}
	
	@ApiMethod(name = "getMessage", httpMethod = HttpMethod.GET, path = "users/{userId}/messages/{id}")
	public Message getMessage(@Named("userId") String userId, @Named("id") Long id) {
		Message message = MessageRepository.getInstance().findMessage(id);
		if (Long.toString(message.getOwner()) == userId) {
			return message;
		} else {
			return null;
		}
	}
	
	@ApiMethod(name = "removeMessage", httpMethod = HttpMethod.DELETE, path = "users/{userId}/messages/{id}")
	public void removeMessage(@Named("userId") String userId, @Named("id") Long id) {
		Message message = MessageRepository.getInstance().findMessage(id);
		if (Long.toString(message.getOwner()) == userId) {
			List<MessageIndex> messageIndexes = ofy().load().type(MessageIndex.class).ancestor(message.getId()).list();
			for (MessageIndex messageIndex : messageIndexes) {
				MessageIndexRepository.getInstance().removeMessageIndex(messageIndex.getId());
			} 
			MessageRepository.getInstance().removeMessage(id);
		}
	}
	
	@ApiMethod(name = "getMyMessages", httpMethod = HttpMethod.GET, path = "users/self/messages")
	public List<Message> getMyMessages(@Named("userId") String userId, @Named("limit") @DefaultValue("10") int limit){
		List<Message> messages = ofy().load().type(Message.class).filter("owner",Long.valueOf(userId)).limit(limit).list();
		return messages;
	}
	
	@SuppressWarnings("rawtypes")
	@ApiMethod(name = "getMessageHashtags", httpMethod = HttpMethod.GET, path = "messages/hashtag")
	public Collection<Message> getMessageHashtags(@Named ("hashtag") String hashtag, @Named("limit") @DefaultValue("10") int limit){
			List<MessageIndex> messageIndexes = ofy().load().type(MessageIndex.class).filter("hashtags IN", hashtag).order("hashtag").list();
			@SuppressWarnings("unchecked")
			List<Key<Message>> messageKeys = new ArrayList();
			for (MessageIndex messageIndex : messageIndexes) {
				messageKeys.add(messageIndex.getMessageEntity());
			}
			Map<Key<Message>, Message> messages = ofy().load().keys(messageKeys);
			return messages.values();
	}
	
	@ApiMethod(name = "addUser", httpMethod = HttpMethod.POST, path = "users")
	public User addUser(@Named("userId") String userId, User user) {
		user.setId(Long.valueOf(userId));
		return UserRepository.getInstance().createUser(user);
	}
	
	@ApiMethod(name = "updateUser", httpMethod = HttpMethod.PUT, path = "users")
	public User updateUser(User user) {
			return UserRepository.getInstance().updateUser(user);
	}
	
	@ApiMethod(name = "getUser", httpMethod = HttpMethod.GET, path = "users/{id}")
	public User getUser(@Named("id") Long id) {
		return UserRepository.getInstance().findUser(id);
	}
	
	@ApiMethod(name = "removeUser", httpMethod = HttpMethod.DELETE, path = "users/{id}")
	public void removeUser(@Named("id") Long id) {
		List<Message> messages = ofy().load().type(Message.class).filter("owner", id).list();
		UserRepository.getInstance().removeUser(id);
		for (Message message : messages) {
			List<MessageIndex> messageIndexes = ofy().load().type(MessageIndex.class).ancestor(message.getId()).list();
			for (MessageIndex messageIndex : messageIndexes) {
				MessageIndexRepository.getInstance().removeMessageIndex(messageIndex.getId());
			}
			MessageRepository.getInstance().removeMessage(message.getId());
		}
	}
	
	@ApiMethod(name = "followUser", httpMethod = HttpMethod.PUT, path = "users/{userId}/following/{userToFollowId}")
	public void followUser (@Named("userId") String userId, @Named("userToFollowId") String userToFollowId) throws EntityNotFoundException {
		User user = UserRepository.getInstance().findUser(Long.valueOf(userId));
		User userToFollow = UserRepository.getInstance().findUser(Long.valueOf(userToFollowId));
		if (!user.getFollowing().contains(Long.valueOf(userToFollowId)) && !userToFollow.getFollowers().contains(Long.valueOf(userId))) {
			user.getFollowing().add(Long.valueOf(userToFollowId));
			userToFollow.getFollowers().add(Long.valueOf(userId));
		} else {
			user.getFollowing().remove(Long.valueOf(userToFollowId));
			userToFollow.getFollowers().remove(Long.valueOf(userId));
		}
	}
	
	
	
	/*@SuppressWarnings({"unchecked", "unused"})
	@ApiMethod(name = "getTimeline", httpMethod = HttpMethod.GET, path = "users/self/timeline")
	public CollectionResponse<Message> getTimeline(
			@Named("userId") String userId,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named ("limit") Integer limit) {
		
		Query<Message> query = ofy().load().type(MessageEntity.class);
		if (limit != null) query.limit(limit);
		if (cursorString != null && cursorString != "") {
			query = query.startAt(Cursor.fromWebSafeString(cursorString));
		}
		List<MessageEntity> records = new ArrayList<MessageEntity>();
		QueryResultIterator<MessageEntity> iterator = query.iterator();
		int num = 0;
		while (iterator.hasNext()) {
			records.add(iterator.next());
			if (limit != null) {
				num++;
				if (num == limit) break;
				}
		}
		//Find the next cursor
		if (cursorString != null && cursorString != "") {
			Cursor cursor = iterator.getCursor();
			if (cursor != null) {
				cursorString = cursor.toWebSafeString();
			}
		}
		return CollectionResponse.<MessageEntity>builder().setItems(records).setNextPageToken(cursorString).build();
		}*/
	
	@ApiMethod(name = "sayHello", httpMethod = HttpMethod.GET, path = "sayHello")
	public HelloClass sayHello() throws EntityNotFoundException {
		return new HelloClass();
	}
	
	@ApiMethod(name = "sayHelloByName", httpMethod = HttpMethod.GET, path = "sayHelloByName")
	public HelloClass sayHelloByName(@Named("name") String name) {
		return new HelloClass(name);
	}
	


	
	

	
	
	
	
	
}

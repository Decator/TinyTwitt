package com.TinyTwitt;

import java.time.LocalDateTime;
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
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;

import com.TinyTwitt.MessageRepository;

import static com.TinyTwitt.OfyService.ofy;

@Api(name = "tinytwittendpoint", namespace = @ApiNamespace(ownerDomain = "TinyTwitt.com", ownerName = "TinyTwitt.com", packagePath = "services"))
public class TinyTwittEndpoint {
	
	public TinyTwittEndpoint() {}
	
	//good
	@ApiMethod(name = "addMessage", httpMethod = HttpMethod.POST, path = "users/{userId}/messages")
	public Message addMessage(@Named("userId") Long userId, @Named("body") String body, @Nullable @Named("hashtags") HashSet<String> hashtags ) {
		User user = UserRepository.getInstance().findUser(userId);
		Message message = new Message();
		message.setOwner(userId);
		message.setBody(body);
		message.setSender(user.getUsername());
		String creationDate = DateFormatting.getInstance().formatting(LocalDateTime.now());
		message.setDate(creationDate);
		Message newMessage = MessageRepository.getInstance().createMessage(message);
		MessageIndex messageIndex = new MessageIndex();
		if (hashtags != null) {
			messageIndex = new MessageIndex(Key.create(Message.class, message.getId()), user.getFollowers(), hashtags, creationDate, message.getOwner());
			messageIndex.addReceiver(userId);
		} else {
			messageIndex = new MessageIndex(Key.create(Message.class, message.getId()), user.getFollowers(), creationDate, message.getOwner());
			messageIndex.addReceiver(userId);
		}
		MessageIndexRepository.getInstance().createMessageIndex(messageIndex);
		return newMessage;
	}
	//good
	@ApiMethod(name = "updateMessage", httpMethod = HttpMethod.PUT, path = "users/{userId}/messages")
	public Message updateMessage(@Named("userId") Long userId, @Named("messageId") Long messageId, @Named("body") String body) {
		Message message = MessageRepository.getInstance().findMessage(messageId);
		if (message.getOwner() == userId) {
			message.setBody(body);
			return MessageRepository.getInstance().updateMessage(message);
		} else {
			return null;
		}
	}
	//good
	@ApiMethod(name = "getMessage", httpMethod = HttpMethod.GET, path = "users/{userId}/messages/{id}")
	public Message getMessage(@Named("userId") Long userId, @Named("id") Long messageId) {
		Message message = MessageRepository.getInstance().findMessage(messageId);
		return message;
	}
	//error java.lang.Long
	@ApiMethod(name = "removeMessage", httpMethod = HttpMethod.DELETE, path = "users/{userId}/messages/{id}")
	public void removeMessage(@Named("userId") Long userId, @Named("id") Long messageId) {
		Message message = MessageRepository.getInstance().findMessage(messageId);
		if (message.getOwner() == userId) {
			MessageIndexRepository.getInstance().removeMessageIndexMessage(messageId);
			MessageRepository.getInstance().removeMessage(messageId);
		}
	}
	//no result
	@ApiMethod(name = "getMyMessages", httpMethod = HttpMethod.GET, path = "users/self/messages")
	public List<Message> getMyMessages(@Named("userId") Long userId, @Named("limit") @DefaultValue("10") int limit){
		List<Message> messages = ofy().load().type(Message.class).filter("owner",userId).limit(limit).list();
		return messages;
	}
	//java.lang.IllegalArgumentException: A collection of values is required.
	@ApiMethod(name = "getMessageHashtags", httpMethod = HttpMethod.GET, path = "messages/hashtag")
	public Collection<Message> getMessageHashtags(@Named ("hashtag") String hashtag, @Named("limit") @DefaultValue("10") int limit){
			List<MessageIndex> messageIndexes = MessageIndexRepository.getInstance().findMessageIndexByHashtag(hashtag, limit);
			return MessageRepository.getInstance().getMessagesFromMessageIndexes(messageIndexes);
	}
	//good
	@ApiMethod(name = "addUser", httpMethod = HttpMethod.POST, path = "users")
	public User addUser(@Named("userId") Long userId, @Named("pseudo") String pseudo) {
		User user = new User();
		user.setId(userId);
		user.setUsername(pseudo);
		return UserRepository.getInstance().createUser(user);
	}
	//good
	@ApiMethod(name = "updateUser", httpMethod = HttpMethod.PUT, path = "users")
	public User updateUser(@Named("userId") Long userId, @Named("pseudo") String pseudo) {
			User user = UserRepository.getInstance().findUser(userId);
			user.setUsername(pseudo);
			return UserRepository.getInstance().updateUser(user);
	}
	//good
	@ApiMethod(name = "getUser", httpMethod = HttpMethod.GET, path = "users/{id}")
	public User getUser(@Named("id") Long id) {
		return UserRepository.getInstance().findUser(id);
	}
	//good
	@ApiMethod(name = "removeUser", httpMethod = HttpMethod.DELETE, path = "users/{id}")
	public void removeUser(@Named("id") Long userId) {
		MessageIndexRepository.getInstance().removeMessageIndexUser(userId);
		MessageRepository.getInstance().removeMessageUser(userId);
		UserRepository.getInstance().removeUser(userId);
	}
	//good
	@ApiMethod(name = "findUsers", httpMethod = HttpMethod.GET, path = "users")
	public Collection<User> findUsers(@Nullable @Named("limit") @DefaultValue("10") int limit){
		return UserRepository.getInstance().findUsers(limit);
	}
	//good
	@ApiMethod(name = "followUser", httpMethod = HttpMethod.PUT, path = "users/{userId}/follow/{userToFollowId}")
	public void followUser (@Named("userId") Long userId, @Named("userToFollowId") Long userToFollowId) throws EntityNotFoundException {
		User user = UserRepository.getInstance().findUser(userId);
		User userToFollow = UserRepository.getInstance().findUser(userToFollowId);
		if (!user.getFollowing().contains(userToFollowId) && !user.getFollowers().contains(userId)) {
			user.addFollowing(userToFollowId);
			userToFollow.addFollower(userId);
			UserRepository.getInstance().updateUser(user);
			UserRepository.getInstance().updateUser(userToFollow);
		} else {
			user.removeFollowing(userToFollowId);
			userToFollow.removeFollower(userId);
			UserRepository.getInstance().updateUser(user);
			UserRepository.getInstance().updateUser(userToFollow);
		}	
	}
	//good
	@ApiMethod(name = "deleteAllUsers", httpMethod = HttpMethod.DELETE, path = "users")
	public void deleteAllUsers() {
		MessageIndexRepository.getInstance().deleteAllMessageIndexes();
		MessageRepository.getInstance().deleteAllMessages();
		UserRepository.getInstance().deleteAllUsers();
	}
	//good
	@ApiMethod(name = "deleteAllMessages", httpMethod = HttpMethod.DELETE, path = "users/all/messages")
	public void deleteAllMessages() {
		MessageIndexRepository.getInstance().deleteAllMessageIndexes();
		MessageRepository.getInstance().deleteAllMessages();
	}
	
	/*@SuppressWarnings({"unchecked", "unused"})
	@ApiMethod(name = "getTimeline", httpMethod = HttpMethod.GET, path = "users/self/timeline")
	public CollectionResponse<Message> getTimeline(
			@Named("userId") Long userId,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named ("limit") Integer limit) {
		
		Query<MessageIndex> query = ofy().load().type(MessageIndex.class).filter("receivers IN", userId);
		if (limit != null) query.limit(limit);
		if (cursorString != null && cursorString != "") {
			query = query.startAt(Cursor.fromWebSafeString(cursorString));
		}
		List<Message> records = new ArrayList<Message>();
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
	
	/* Test methods
	@ApiMethod(name = "sayHello", httpMethod = HttpMethod.GET, path = "sayHello")
	public HelloClass sayHello() throws EntityNotFoundException {
		return new HelloClass();
	}
	
	@ApiMethod(name = "sayHelloByName", httpMethod = HttpMethod.GET, path = "sayHelloByName")
	public HelloClass sayHelloByName(@Named("name") String name) {
		return new HelloClass(name);
	}*/
	
}

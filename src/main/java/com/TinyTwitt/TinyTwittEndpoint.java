package com.TinyTwitt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.DefaultValue;
import com.google.api.server.spi.response.CollectionResponse;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.googlecode.objectify.cmd.Query;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;

import com.TinyTwitt.MessageRepository;

import static com.TinyTwitt.OfyService.ofy;

@Api(name = "tinytwittendpoint", namespace = @ApiNamespace(ownerDomain = "TinyTwitt.com", ownerName = "TinyTwitt.com", packagePath = "services"))
public class TinyTwittEndpoint {
	
	public TinyTwittEndpoint() {}
	
	/**
	 * Method used to insert a message in the datastore
	 * @param userId the Id of the user writing the message
	 * @param body the content of the message
	 * @param hashtags the hashtags tied to the message
	 * @return the message entity
	 */
	@ApiMethod(name = "addMessage", httpMethod = HttpMethod.POST, path = "users/{userId}/messages")
	public Message addMessage(@Named("userId") String userId, @Named("body") String body, @Nullable @Named("hashtags") HashSet<String> hashtags ) {
		User user = UserRepository.getInstance().findUser(userId);
		String idMessage = UUID.randomUUID().toString().replaceAll("-","");
		String creationDate = DateFormatting.getInstance().formatting(LocalDateTime.now());
		Message message = new Message(idMessage, user.getUsername(), userId, body, creationDate);
		String idMessageIndex = UUID.randomUUID().toString().replaceAll("-","");
		MessageIndex messageIndex = new MessageIndex(idMessageIndex, Key.create(Message.class, message.getId()), user.getFollowers(), creationDate, message.getOwner());
		messageIndex.addReceiver(userId);
		if (hashtags != null) {
			String textHashtag =" ";
			messageIndex.setHashtags(hashtags);
			for (String hashtag : messageIndex.getHashtags()) {
				textHashtag += "#"+hashtag+" ";
			}
			message.setBody(message.getBody()+textHashtag);
		}
		MessageRepository.getInstance().createMessage(message);
		MessageIndexRepository.getInstance().createMessageIndex(messageIndex);
		return message;
	}

	/**
	 * Method used to update a message with a new content and new hashtags
	 * @param userId ID of the user retrieving the message (must match ID of the message's owner)
	 * @param messageId ID of the message to be updated
	 * @param body new content of the message
	 * @param hashtags new hashtags of the message
	 * @return updated message
	 */
	@ApiMethod(name = "updateMessage", httpMethod = HttpMethod.PUT, path = "users/{userId}/messages/{messageId}")
	public Message updateMessage(@Named("userId") String userId, @Named("messageId") String messageId, @Named("body") String body,@Nullable @Named("hashtags") Set<String> hashtags) {
		Message message = MessageRepository.getInstance().findMessage(messageId);
		User user = UserRepository.getInstance().findUser(userId);
		if (message.getOwner() == userId) {
			message.setId(messageId);
			message.setBody(body);
			Iterable<Key<MessageIndex>> OldMessageIndexKey = ofy().load().type(MessageIndex.class).ancestor(messageId).keys();
			ofy().delete().keys(OldMessageIndexKey);
			String idMessageIndex = UUID.randomUUID().toString().replaceAll("-","");
			MessageIndex messageIndex = new MessageIndex(idMessageIndex, Key.create(Message.class, message.getId()), user.getFollowers(), message.getDate(), message.getOwner());
			messageIndex.addReceiver(userId);
			if (hashtags != null) {
				String textHashtag =" ";
				messageIndex.setHashtags(hashtags);
				for (String hashtag : messageIndex.getHashtags()) {
					textHashtag += "#"+hashtag+" ";
				}
				message.setBody(message.getBody()+textHashtag);
			}
			return MessageRepository.getInstance().updateMessage(message);
		} else {
			return null;
		}
	}

	/** 
	 * Method used to retrieve a message from the datastore
	 * @param userId ID of the user retrieving the message (must match ID of the message's owner)
	 * @param messageId iD of the message to be retrieved
	 * @return the message entity retrieved from the datastore
	 */
	@ApiMethod(name = "getMessage", httpMethod = HttpMethod.GET, path = "users/{userId}/messages/{messageId}")
	public Message getMessage(@Named("userId") String userId, @Named("messageId") String messageId) {
		Message message = MessageRepository.getInstance().findMessage(messageId);
		if (message.getOwner() == userId) {
			return message;
		} else {
			return null;
		}
	}

	/** 
	 * Method used to delete a message from the datastore
	 * @param userId ID of the user deleting the message (must match ID if the message's owner)
	 * @param messageId ID of the message entity to be deleted
	 */
	@ApiMethod(name = "removeMessage", httpMethod = HttpMethod.DELETE, path = "users/{userId}/messages/{messageId}")
	public void removeMessage(@Named("userId") String userId, @Named("messageId") String messageId) {
		Message message = MessageRepository.getInstance().findMessage(messageId);
		if (message.getOwner() == userId) {
			MessageIndexRepository.getInstance().removeMessageIndexMessage(messageId);
			MessageRepository.getInstance().removeMessage(messageId);
		}
	}

	/** 
	 * Method used to get all the messages owned by the given userId
	 * @param userId ID of the user
	 * @param limit number of messages to be retrieved (default 10)
	 * @return list of messages
	 */
	@ApiMethod(name = "getMyMessages", httpMethod = HttpMethod.GET, path = "users/self/messages")
	public List<Message> getMyMessages(@Named("userId") String userId, @Named("limit") @DefaultValue("10") int limit){
		return MessageRepository.getInstance().myMessages(userId, limit);
	}
	
	/**
	 * Method used to get all the messages containing a given hashtag
	 * @param hashtag hashtag used to search for messages
	 * @param limit number of messages to be retrieved (default 10)
	 * @return list of messages
	 */
	@ApiMethod(name = "getMessageHashtags", httpMethod = HttpMethod.GET, path = "messages/hashtag")
	public List<Message> getMessageHashtags(@Named ("hashtag") String hashtag, @Named("limit") @DefaultValue("10") int limit){
			List<MessageIndex> messageIndexes = MessageIndexRepository.getInstance().findMessageIndexByHashtag(hashtag, limit);
			return MessageRepository.getInstance().getMessagesFromMessageIndexes(messageIndexes);
	}

	/**
	 * Method used to insert an User into the datastore
	 * @param userId ID of the user to be created
	 * @param pseudo Username of the user to be created
	 * @param profilePic picture of the user to be created
	 * @return created User
	 */
	@ApiMethod(name = "addUser", httpMethod = HttpMethod.POST, path = "users")
	public User addUser(@Named("userId") String userId, @Named("pseudo") String pseudo, @Nullable @Named("profilePic") String profilePic) {
		User user = new User();
		user.setId(userId);
		user.setUsername(pseudo);
		if (profilePic != null) {
			user.setProfilePic(profilePic);
		}
		if (UserRepository.getInstance().findUser(userId) == null) {
			return UserRepository.getInstance().createUser(user);
		}
		else {
			return null;
		}
	}

	/**
	 * Method used to update an User
	 * @param userId ID of the user to be updated
	 * @param pseudo Username
	 * @return updated User
	 */
	@ApiMethod(name = "updateUser", httpMethod = HttpMethod.PUT, path = "users/{userId}")
	public User updateUser(@Named("userId") String userId, @Nullable @Named("pseudo") String pseudo, @Nullable @Named("profilePic") String profilePic) {
			User user = UserRepository.getInstance().findUser(userId);
			if (pseudo != null) {
				user.setUsername(pseudo);
			}
			if (profilePic != null) {
				user.setProfilePic(profilePic);
			}
			return UserRepository.getInstance().updateUser(user);
	}

	/**
	 * Method used to retrieve an User from the datastore
	 * @param userId ID of the user to be retrieved
	 * @return retrieved User
	 */
	@ApiMethod(name = "getUser", httpMethod = HttpMethod.GET, path = "users/{userId}")
	public User getUser(@Named("userId") String userId) {
		return UserRepository.getInstance().findUser(userId);
	}

	/**
	 * Method used to delete an User from the datastore
	 * @param userId ID of the user to be deleted
	 */
	@ApiMethod(name = "removeUser", httpMethod = HttpMethod.DELETE, path = "users/{userId}")
	public void removeUser(@Named("userId") String userId) {
		MessageIndexRepository.getInstance().removeMessageIndexUser(userId);
		MessageRepository.getInstance().removeMessageUser(userId);
		UserRepository.getInstance().removeUser(userId);
	}
	
	/**
	 * Method used to find all Users
	 * @param limit number of users to be retrieved (default 10)
	 * @return collection of Users
	 */
	@ApiMethod(name = "findUsers", httpMethod = HttpMethod.GET, path = "users/all")
	public Collection<User> findUsers(@Nullable @Named("limit") @DefaultValue("10") int limit){
		return UserRepository.getInstance().findUsers(limit);
	}
	
	/**
	 * Method used to find all Users with a given username
	 * @param username username used to find users
	 * @param limit limit number of users to be retrieved (default 10)
	 * @return collection of Users
	 */
	@ApiMethod(name = "findUsersByUsername", httpMethod = HttpMethod.GET, path = "users")
	public Collection<User> findUsersByUsername(@Named("username") String username, @Nullable @Named("limit") @DefaultValue("10") int limit){
		return UserRepository.getInstance().findUsers(username, limit);
	}

	/**
	 * Method used to add userToFollowId to (user)'s following list, while also adding (userId) to (userToFollow)'s followers list
	 * @param userId ID of the user following userToFollow
	 * @param userToFollowId ID of the user being followed by user
	 */
	@ApiMethod(name = "followUser", httpMethod = HttpMethod.PUT, path = "users/{userId}/follow/{userToFollowId}")
	public void followUser (@Named("userId") String userId, @Named("userToFollowId") String userToFollowId) {
		User user = UserRepository.getInstance().findUser(userId);
		User userToFollow = UserRepository.getInstance().findUser(userToFollowId);
		if (userId != userToFollowId) {
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
	}
	
	/** 
	 * Method used to get an user's timeline
	 * @param userId ID of the user's timeline to be retrieved
	 * @param cursorString cursor for the query
	 * @param limit number of messages to be retrieved
	 * @return A CollectionResponse of Messages
	 */
	@ApiMethod(name = "getTimeline", httpMethod = HttpMethod.GET, path = "users/self/timeline")
	public CollectionResponse<Message> getTimeline(
			@Named("userId") String userId,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named ("limit") Integer limit) {
		
		Query<MessageIndex> query = ofy().load().type(MessageIndex.class).filter("receivers", userId).order("-date");
		
		if (limit != null) query.limit(limit);
		if (cursorString != null && cursorString != "") {
			query = query.startAt(Cursor.fromWebSafeString(cursorString));
		}
		List<MessageIndex> records = new ArrayList<MessageIndex>();
		QueryResultIterator<MessageIndex> iterator = query.iterator();
		int num = 0;
		while (iterator.hasNext()) {
			records.add(iterator.next());
			if (limit != null) {
				num++;
				if (num == limit) break;
				}
		}
		List<Message> messages = MessageRepository.getInstance().myTimeline(records);
		
		//Find the next cursor
		if (cursorString != null && cursorString != "") {
			Cursor cursor = iterator.getCursor();
			if (cursor != null) {
				cursorString = cursor.toWebSafeString();
			}
		}
		return CollectionResponse.<Message>builder().setItems(messages).setNextPageToken(cursorString).build();
		}

	/**
	 * Method used to delete all Users and their messages in the datastore
	 */
	@ApiMethod(name = "deleteAllUsers", httpMethod = HttpMethod.DELETE, path = "users/all")
	public void deleteAllUsers() {
		MessageIndexRepository.getInstance().deleteAllMessageIndexes();
		MessageRepository.getInstance().deleteAllMessages();
		UserRepository.getInstance().deleteAllUsers();
	}

	/**
	 * Method used to delete all messages in the datastore
	 */
	@ApiMethod(name = "deleteAllMessages", httpMethod = HttpMethod.DELETE, path = "users/all/messages")
	public void deleteAllMessages() {
		MessageIndexRepository.getInstance().deleteAllMessageIndexes();
		MessageRepository.getInstance().deleteAllMessages();
	}
	
	/*@ApiMethod(name = "creatingFollowers", httpMethod = HttpMethod.POST, path = "test/1")
	public void creatingFollowers(@Named("userId") String userId, 
			@Named("nbFollowers") @Nullable @DefaultValue("100") int nbFollowers,
			@Nullable @Named("nbCall") @DefaultValue("1") int nbCall,
			@Nullable @Named("totalCalls") @DefaultValue("5") int totalCalls) {
		User user = UserRepository.getInstance().findUser(userId);
		for (int i = (nbCall-1)*nbFollowers/totalCalls; i < nbCall*nbFollowers/totalCalls; i++) {
			User mockUser = new User();
			mockUser.setId(""+i);
			mockUser.setUsername(""+i);
			UserRepository.getInstance().createUser(mockUser);
			followUser(mockUser.getId(),user.getId());
		}
	}
	
	@ApiMethod(name = "creatingBatchMessages", httpMethod = HttpMethod.POST, path = "test/2")
	public void creatingBatchMessages(@Named("userId") String userId,  
			@Nullable @Named("nbMessages") @DefaultValue("100") Long nbMessages, 
			@Nullable @Named("hashtags") HashSet<String> hashtags) {
		User user = UserRepository.getInstance().findUser(userId);
		for (int i = 0; i < nbMessages; i++) {
			String idMessage = UUID.randomUUID().toString().replaceAll("-","");
			String creationDate = DateFormatting.getInstance().formatting(LocalDateTime.now());
			Message message = new Message(idMessage, user.getUsername(), userId, "Coucou", creationDate);
			String idMessageIndex = UUID.randomUUID().toString().replaceAll("-","");
			MessageIndex messageIndex = new MessageIndex(idMessageIndex, Key.create(Message.class, message.getId()), user.getFollowers(), creationDate, message.getOwner());
			messageIndex.addReceiver(userId);
			if (hashtags != null) {
				String textHashtag =" ";
				messageIndex.setHashtags(hashtags);
				for (String hashtag : messageIndex.getHashtags()) {
					textHashtag += "#"+hashtag+" ";
				}
				message.setBody(message.getBody()+textHashtag);
			}
			MessageRepository.getInstance().createMessage(message);
			MessageIndexRepository.getInstance().createMessageIndex(messageIndex);
		}
	}*/
}

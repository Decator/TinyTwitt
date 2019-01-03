package com.TinyTwitt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.TinyTwitt.PMF;

@Api(name = "tinytwittendpoint", namespace = @ApiNamespace(ownerDomain = "TinyTwitt.com", ownerName = "TinyTwitt.com", packagePath = "services"))
public class TinyTwittEndpoint {
	
	/*@SuppressWarnings({"unchecked", "unused"})
	@ApiMethod(name = "getTimeline", httpMethod = HttpMethod.GET, path = "users/self/timeline")
	public CollectionResponse<MessageEntity> getTimeline(
			@Named("userId") String userId,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named ("limit") Integer limit) {
		PersistenceManager pmr = null;
		Cursor cursor  = null;
		List<MessageEntity> execute = null;
		
		try {
			pmr = getPersistenceManager();
			Query query = pmr.newQuery(MessageEntity.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}
			
			if (limit != null) {
				query.setRange(0, limit);
			}
			
			execute = (List<MessageEntity>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null) {
				cursorString = cursor.toWebSafeString();
			}
			for (MessageEntity obj : execute);
		} finally {
			pmr.close();
		}
		
		return CollectionResponse.<MessageEntity> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}*/
	
	@ApiMethod(name = "sayHello", httpMethod = HttpMethod.GET, path = "sayHello")
	public HelloClass sayHello() throws EntityNotFoundException {
		return new HelloClass();
	}
	
	@ApiMethod(name = "sayHelloByName", httpMethod = HttpMethod.GET, path = "sayHelloByName")
	public HelloClass sayHelloByName(@Named("name") String name) {
		return new HelloClass(name);
	}
	

	@ApiMethod(name = "addMessage", httpMethod = HttpMethod.POST, path = "messages")
	public MessageEntity addMessage(@Named("userId") String userId, @Named("body") String body, @Nullable @Named("hashtags") Set<String> hashtags) throws EntityExistsException {
		PersistenceManager pmr = getPersistenceManager();
		MessageEntity messageEntity = new MessageEntity();
		try {
			UserEntity sender = pmr.getObjectById(UserEntity.class, userId);
			messageEntity.setSender(sender.getUsername());
			messageEntity.setOwner(userId);
			messageEntity.setBody(body);
			messageEntity.setDate(new Date());
			if (containsMessageEntity(messageEntity)) {
				throw new EntityExistsException("Message already exists");
			}
			pmr.makePersistent(messageEntity);
			String keyIndex = KeyFactory.createKeyString(messageEntity.getId(),"MessageIndex");
			MessageIndexEntity messageIndex = new MessageIndexEntity();
			messageIndex.setId(keyIndex);
			messageIndex.setReceivers(sender.followers);
			if (hashtags != null) {
				messageIndex.setHashtags(hashtags);
			}
			pmr.makePersistent(messageIndex);
		} finally {
			pmr.close();
		}
		return messageEntity;
	}

	@ApiMethod(name = "getMessageEntity", httpMethod = HttpMethod.GET, path = "messages/$id")
	public MessageEntity getMessageEntity(@Named("id") String id) throws EntityNotFoundException {
		PersistenceManager pmr = getPersistenceManager();
		MessageEntity messageEntity = null;
		try {
			messageEntity = pmr.getObjectById(MessageEntity.class, id);
		} finally {
			pmr.close();
		}
		return messageEntity;
	}
	
	@ApiMethod(name = "updateMessageEntity", httpMethod = HttpMethod.PUT, path = "messages")
	public MessageEntity updateMessageEntity(MessageEntity messageEntity) throws EntityNotFoundException {
		PersistenceManager pmr = getPersistenceManager();
		try {
			if (!containsMessageEntity(messageEntity)) {
				throw new EntityNotFoundException("Message does not exist");
			} 
			pmr.makePersistent(messageEntity);
		} finally {
			pmr.close();
		}
		return messageEntity;
	}
	
	@ApiMethod(name = "removeMessageEntity", httpMethod = HttpMethod.DELETE, path = "messages/id")
	public void removeMessageEntity(@Named("id") String id) {
		PersistenceManager pmr = getPersistenceManager();
		try {
			MessageEntity message = pmr.getObjectById(MessageEntity.class, id);
			String indexMessage = KeyFactory.createKeyString(message.getId(), "MessageIndex");
			MessageIndexEntity messageIndex = pmr.getObjectById(MessageIndexEntity.class, indexMessage);
			pmr.deletePersistent(message);
			pmr.deletePersistent(messageIndex);
		} finally {
			pmr.close();
		}
	}
	
	@ApiMethod(name = "getMessageHashtags", httpMethod = HttpMethod.GET, path = "messages/hashtag")
	public Collection<Entity> getMessageHashtags(@Named ("hashtag") String hashtag){
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Filter p = new FilterPredicate("hashtags", FilterOperator.EQUAL, hashtag);
			com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query ("MessageIndexEntity").setFilter(p);
			q.setKeysOnly();
			PreparedQuery pq = ds.prepare(q);
			List<Entity> results = pq.asList(FetchOptions.Builder.withLimit(10));
			List<Key> pk = new ArrayList<Key>();
			for (Entity r : results) {
				pk.add(r.getParent());
			}
			Map<Key, Entity> map = ds.get(pk);
			return map.values();
	}
	
	@ApiMethod(name = "getMyMessages", httpMethod = HttpMethod.GET, path = "users/self/messages")
	public List<Entity> getMyMessages(@Named("userId") String userID){
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Filter f = new FilterPredicate("owner", FilterOperator.EQUAL, userID);
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query("MessageEntity").setFilter(f);
		PreparedQuery pq= ds.prepare(q);
		List<Entity> results=pq.asList(FetchOptions.Builder.withLimit(10));
		return results;
	}
	
	private boolean containsMessageEntity(MessageEntity messageEntity) {
		PersistenceManager pmr = getPersistenceManager();
		boolean contains = true;
		try {
			pmr.getObjectById(MessageEntity.class, messageEntity.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			pmr.close();
		}
		return contains;
	}
	
	@SuppressWarnings({ "null" })
	@ApiMethod(name = "addUser", httpMethod = HttpMethod.POST, path = "users")
	public UserEntity addUser(@Named("userId") String userId, @Named("username") String username) throws EntityExistsException {
		PersistenceManager pmr = getPersistenceManager();
		UserEntity userEntity = null;
		try {
			userEntity.setId(userId);
			userEntity.setUsername(username);
			if (containsUserEntity(userEntity)) {
				throw new EntityExistsException("User already exists");
			}
			pmr.makePersistent(userEntity);
		} finally {
			pmr.close();
		}
		return userEntity;
	}
	
	@ApiMethod(name = "getUser", httpMethod = HttpMethod.GET, path = "users/${id}")
	public UserEntity getUser(@Named("userId") String userId) throws EntityNotFoundException {
		PersistenceManager pmr = getPersistenceManager();
		UserEntity userEntity = null;
		try {
			userEntity = pmr.getObjectById(UserEntity.class, userId);
		} finally {
			pmr.close();
		}
		return userEntity;
	} 
	
	@ApiMethod(name = "followUser", httpMethod = HttpMethod.PUT, path = "users/${id}")
	public void followUser (@Named("userId") String userId,@Named("userToFollowId") String userToFollowId) throws EntityNotFoundException {
		PersistenceManager pmr = getPersistenceManager();
		try {
			if (!containsUserEntity(pmr.getObjectById(UserEntity.class, userToFollowId))){
				throw new EntityNotFoundException("Object does not exist");
			}
			UserEntity userFollowing = pmr.getObjectById(UserEntity.class, userId);
			UserEntity userFollowed = pmr.getObjectById(UserEntity.class, userToFollowId);
			if (!userFollowing.following.contains(userToFollowId) && !userFollowed.followers.contains(userId)) {
				userFollowing.following.add(userToFollowId);
				userFollowed.followers.add(userId);
			} else {
				userFollowing.following.remove(userToFollowId);
				userFollowed.followers.remove(userId);
			}
		} finally {
			pmr.close();
		}
	}
	
	private boolean containsUserEntity(UserEntity userEntity) {
		PersistenceManager pmr = getPersistenceManager();
		boolean contains = true;
		try {
			pmr.getObjectById(UserEntity.class, userEntity.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			pmr.close();
		}
		return contains;
	}
	
	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}
}

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

@Api(name = "MessageEndpoint", namespace = @ApiNamespace(ownerDomain = "TinyTweet.com", ownerName = "TinyTweet.com", packagePath = "messages"))
public class MessageEndpoint {
	
	@SuppressWarnings({"unchecked", "unused"})
	@ApiMethod(name = "getTimeline")
	public CollectionResponse<MessageEntity> getTimeline(
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
	}
	
	@ApiMethod(name = "getTest")
	public String getTest() throws EntityNotFoundException {
//		PersistenceManager pmr = getPersistenceManager();
//		MessageEntity  messageEntity= null;
//		try {
//			if (containsMessageEntity(pmr.getObjectById(MessageEntity.class, id))) {
//				messageEntity = pmr.getObjectById(MessageEntity.class, id);
//			} else {
//				throw new EntityNotFoundException("Message does not exist !");
//			}
//		} finally {
//			pmr.close();
//		}
		return "test ok";
	}
	
	
	@ApiMethod(name = "getMessageEntity")
	public MessageEntity getMessageEntity(@Named("id") Key id) throws EntityNotFoundException {
		PersistenceManager pmr = getPersistenceManager();
		MessageEntity  messageEntity= null;
		try {
			if (containsMessageEntity(pmr.getObjectById(MessageEntity.class, id))) {
				messageEntity = pmr.getObjectById(MessageEntity.class, id);
			} else {
				throw new EntityNotFoundException("Message does not exist !");
			}
		} finally {
			pmr.close();
		}
		return messageEntity;
	}

	@ApiMethod(name = "addMessage")
	public MessageEntity addMessage(@Named("userId") Key userId, @Named("body") String body, @Nullable @Named("hashtags") Set<String> hashtags) throws EntityExistsException {
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
			Key keyIndex = KeyFactory.createKey(messageEntity.getId(), "MessageIndex", "index");
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
	
	@ApiMethod(name = "updateMessageEntity")
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
	
	@ApiMethod(name = "removeMessageEntity")
	public void removeMessageEntity(@Named("id") Key id) {
		PersistenceManager pmr = getPersistenceManager();
		try {
			MessageEntity message = pmr.getObjectById(MessageEntity.class, id);
			Key indexMessage = KeyFactory.createKey(message.getId(), "MessageIndex", "index");
			MessageIndexEntity messageIndex = pmr.getObjectById(MessageIndexEntity.class, indexMessage);
			pmr.deletePersistent(message);
			pmr.deletePersistent(messageIndex);
		} finally {
			pmr.close();
		}
	}
	
	@ApiMethod(name = "getMessageHashtags")
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
	
	@ApiMethod(name = "getMyMessages")
	public List<Entity> getMyMessages(@Named("userId") Key userID){
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
	
	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}
}

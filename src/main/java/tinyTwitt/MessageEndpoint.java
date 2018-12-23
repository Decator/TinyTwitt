package tinyTwitt;

import java.util.HashMap;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "MessageEndpoint", namespace = @ApiNamespace(ownerDomain = "TinyTweet.com", ownerName = "TinyTweet.com"))

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
	
	
	@ApiMethod(name = "getMessageEntity")
	public MessageEntity getMessageEntity(@Named("id") Long id) {
		PersistenceManager pmr = getPersistenceManager();
		MessageEntity  messageEntity= null;
		try {
			messageEntity = pmr.getObjectById(MessageEntity.class, id);
		} finally {
			pmr.close();
		}
		return messageEntity;
	}

	@ApiMethod(name = "addMessage")
	public MessageEntity AddMessage(MessageEntity messageEntity) {
		PersistenceManager pmr = getPersistenceManager();
		try {
			if (containsMessageEntity(messageEntity)) {
				throw new EntityExistsException("Object already exists");
			}
			pmr.makePersistent(messageEntity);
		} finally {
			pmr.close();
		}
		return messageEntity;
	}
	
	@ApiMethod(name = "updateMessageEntity")
	public MessageEntity updateMessageEntity(MessageEntity messageEntity) {
		PersistenceManager pmr = getPersistenceManager();
		try {
			if (!containsMessageEntity(messageEntity)) {
				throw new EntityNotFoundException("Object does not exist");
			} 
			pmr.makePersistent(messageEntity);
		} finally {
			pmr.close();
		}
		return messageEntity;
	}
	
	@ApiMethod(name = "removeMessageEntity")
	public void removeMessageEntity(@Named("id") Long id) {
		PersistenceManager pmr = getPersistenceManager();
		try {
			MessageEntity messageEntity = pmr.getObjectById(MessageEntity.class, id);
			pmr.deletePersistent(messageEntity);
		} finally {
			pmr.close();
		}
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

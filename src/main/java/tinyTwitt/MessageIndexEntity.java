package tinyTwitt;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class MessageIndexEntity {
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	@Persistent
	Set<Key> receivers = new HashSet<Key>();
	
	@Persistent
	Set<String> hashtags = new HashSet<String>();
	
	public void setId(Key id) {
		this.id = id;
	}
	
	public Key getId() {
		return this.id;
	}
	
	public void setReceivers(Set<Key> receivers) {
		this.receivers = receivers;
	}
	
	public Set<Key> getReceivers(){
		return this.receivers;
	}
	
	public void addReceiver(Key idReceiver) {
		this.receivers.add(idReceiver);
	}
	
	public void removeReceiver(Key idReceiver) {
		this.receivers.remove(idReceiver);
	}
	
	public void setHashtags(Set<String> hashtags) {
		this.hashtags = hashtags;
	}
	
	public Set<String> getHashtags(){
		return this.hashtags;
	}
	
	public void addHashtag(String hashtag) {
		this.hashtags.add(hashtag);
	}
	
	public void removeHashtag(String hashtag) {
		this.hashtags.remove(hashtag);
	}
}

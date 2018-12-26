package tinyTwitt;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class UserEntity {
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	Key id;

	@Persistent
	String username;
	
	@Persistent 
	Set<Key> followers = new HashSet<Key>();
	
	@Persistent
	@ApiResourceProperty(ignored=AnnotationBoolean.TRUE)
	Set<Key> following = new HashSet<Key>();
	
	@Persistent
	@ApiResourceProperty(ignored=AnnotationBoolean.TRUE)
	List<Key> timeline = new ArrayList<Key>();
	
	public Key getId() {
		return this.id;
	}

	public void setId(Key id) {
		this.id = id;
	}
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Key> getFollowers() {
		return this.followers;
	}

	public void setFollowers(Set<Key> followers) {
		this.followers = followers;
	}

	public Set<Key> getFollowing() {
		return this.following;
	}

	public void setFollowing(Set<Key> following) {
		this.following = following;
	}
	
	public void addFollower(Key followerId) {
		this.followers.add(followerId);
	}

	public void addFollowing(Key followingId) {
		this.following.add(followingId);
	}
	
	public List<Key> getTimeline() {
		return this.timeline;
	}

	public void setTimeline(List<Key> timeline) {
		this.timeline = timeline;
	}
	
	public void addingTimeline(Key messageId) {
		this.timeline.add(messageId);
	}
}

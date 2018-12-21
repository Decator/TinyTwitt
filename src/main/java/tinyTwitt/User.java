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

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class User {
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	Long key;

	@Persistent
	String username;
	
	@Persistent 
	Set<Long> followers = new HashSet<Long>();
	
	@Persistent
	@ApiResourceProperty(ignored=AnnotationBoolean.TRUE)
	Set<Long> following = new HashSet<Long>();
	
	@Persistent
	@ApiResourceProperty(ignored=AnnotationBoolean.TRUE)
	List<Message> timeline = new ArrayList<Message>();
	
	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Long> getFollowers() {
		return this.followers;
	}

	public void setFollowers(Set<Long> followers) {
		this.followers = followers;
	}

	public Set<Long> getFollowing() {
		return this.following;
	}

	public void setFollowing(Set<Long> following) {
		this.following = following;
	}
	
	public void addFollower(Long key) {
		this.followers.add(key);
	}

	public void addFollowing(Long key) {
		this.following.add(key);
	}
	
	public List<Message> getTimeline() {
		return this.timeline;
	}

	public void setTimeline(List<Message> timeline) {
		this.timeline = timeline;
	}
	
	public void ajoutTimeline(Message message) {
		this.timeline.add(message);
	}
}

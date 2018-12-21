package tinyTwitt;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class MessageEntity {
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	Long key;
	
	@Persistent
	UserEntity sender;
	
	@Persistent
	String body;
	
	@Persistent
	String date;
	
	public void setKey(Long key) {
		this.key = key;
	}
	
	public long getKey() {
		return this.key;
	}
	
	

}

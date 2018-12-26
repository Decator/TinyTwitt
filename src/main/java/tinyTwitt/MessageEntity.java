package tinyTwitt;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class MessageEntity {
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String sender;
	
	@Persistent
	private String body;
	
	@Persistent
	private Date date;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getBody() {
		return this.body;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return this.date;
	}

}

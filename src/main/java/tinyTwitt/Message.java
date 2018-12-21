package tinyTwitt;

@Persistance.Capable(identityType=IdentityType.APPLICATION)
public class Message {
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	Long id;
	
	@Persistent
	String owner;
	
	@Persistent
	String body;
	
	@Persistent
	String picture;
	
	@Persistent
	String date;

}

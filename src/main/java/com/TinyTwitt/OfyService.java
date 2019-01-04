package com.TinyTwitt;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {
	
	static {
		ObjectifyService.register(Message.class);
		ObjectifyService.register(MessageIndex.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(Long.class);
		}

	public static Objectify ofy() {
	return ObjectifyService.ofy();
	}

	public static ObjectifyFactory factory() {
	return ObjectifyService.factory();
	}
}

package com.TinyTwitt;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

public class ObjectifyBootstrap implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ObjectifyService.register(Message.class);
		ObjectifyService.register(MessageIndex.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(Long.class);
		ObjectifyService.begin();
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}

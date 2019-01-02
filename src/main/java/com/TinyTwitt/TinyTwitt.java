package com.TinyTwitt;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.TinyTwitt.PMF;

@SuppressWarnings("serial")
@WebServlet(
    name = "TinyTwitt",
    urlPatterns = {"/twitt"}
)
public class TinyTwitt extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {

    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().print("TinyTweet\r\n");
    UserService userService = UserServiceFactory.getUserService();
    response.getWriter().println(userService.getCurrentUser().getUserId());
    response.getWriter().println("pouet");
    Key userId = KeyFactory.createKey("User", userService.getCurrentUser().getUserId());
    UserEndpoint userEndpoint = new UserEndpoint();
    UserEntity user = new UserEntity();
    
    
  }
}
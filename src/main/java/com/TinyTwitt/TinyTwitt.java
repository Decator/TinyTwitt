package com.TinyTwitt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
@WebServlet(
    name = "TinyTwitt",
    urlPatterns = {"/twitt"}
)
public class TinyTwitt extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    PrintWriter out = response.getWriter();
	response.setContentType("text/html; charset=UTF-8");
	
    out.println("<!DOCTYPE html>");
    out.println("<html><head>");
    out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
    out.println("<script src=\"script.js\"></script>");
    out.println("<title>TinyTwitt</title></head><body>");
    out.println("<a href=\"#\" onclick=\"signOut();\">Sign out</a>");
    out.println(userService.getCurrentUser().getUserId());
    out.println("</body></html>");
    
    
  }
}
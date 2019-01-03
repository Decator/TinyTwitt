package com.TinyTwitt;

public class HelloClass {
	public String message = "Hello World";
	
	public HelloClass() {}
	
	public HelloClass(String name){
		this.message= "Hello" + name + "!";
	}
	
	public String getMessage(){
		return message;
	}

}

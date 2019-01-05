var services = angular.module('services', []);

services.factory('GoogleAuth', function() {
	var googleAuth = {};
	
	googleAuth.getAllGoogleAuth = function() {
		return googleAuth.all;
	}
	
	googleAuth.getIdGoogleAuth = function() {
		return googleAuth.id;
	}
	
	googleAuth.getNameGoogleAuth = function() {
		return googleAuth.name;
	}
	
	googleAuth.getImageGoogleAuth = function() {
		return googleAuth.image;
	}
	
	googleAuth.getEmailGoogleAuth = function() {
		return googleAuth.email;
	}
	
	googleAuth.setGoogleAuth = function(element) {
		googleAuth.all = element;
		googleAuth.id = element.getId();
		googleAuth.name = element.getName();
		googleAuth.image = element.getImageUrl();
		googleAuth.email = element.getEmail();
	}
	
	return googleAuth;
});
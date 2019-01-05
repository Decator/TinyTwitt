var services = angular.module('services', []);

services.factory('GoogleAuth', function() {
	var googleAuth = {};
	
	googleAuth.getIdGoogleAuth = function() {
		return googleAuth.id;
	}
	
	googleAuth.getNameGoogleAuth = function() {
		return googleAuth.name;
	}
	
	googleAuth.getImageUrlGoogleAuth = function() {
		return googleAuth.imageUrl;
	}
	
	googleAuth.setIdGoogleAuth = function(id) {
		googleAuth.id = id;
	}
	
	googleAuth.setNameGoogleAuth = function(name) {
		googleAuth.name = name;
	}
	
	googleAuth.setImageUrlGoogleAuth = function(imageUrl) {
		googleAuth.imageUrl = imageUrl;
	}
	
	return googleAuth;
});
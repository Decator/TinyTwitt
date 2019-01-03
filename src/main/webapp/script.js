var app = angular.module('tinyTwitt', ['ngRoute']);
app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "main.html"
    })
    .when("/twitt", {
        templateUrl : "twitt.html"
    })
    .otherwise({
        redirectTo: '/'
    });
}]);

function onSignIn(googleUser) {
	var profile = googleUser.getBasicProfile();
	console.log('ID: ' + profile.getId());
	console.log('Name: ' + profile.getName());
	console.log('Image URL: ' + profile.getImageUrl());
	console.log('Email: ' + profile.getEmail());
	window.location.href = "#!twitt";
}

function onSignInFailure() {
	console.log("Sign in failed");
}

function signOut() {
  var auth2 = gapi.auth2.getAuthInstance();
  auth2.signOut().then(function () {
    console.log('User signed out.');
    document.location.href="#";
  });
}

var app = angular.module('tinyTwitt', ['ngRoute']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "partials/main.html",
        controller : "mainCtrl"
    })
    .when("/twitt", {
        templateUrl : "partials/twitt.html",
        controller : "twittCtrl"
    })
    .otherwise({
        redirectTo: '/'
    });
}]);

app.controller('mainCtrl', ['$scope', '$window', function($scope, $window) {
	$scope.count = 0;
	
	$scope.myFunc = function() {
		$scope.count++;
	}
	
	$scope.listMyMessages = function() {
		console.log("list messages");
		console.log(gapi);
        gapi.client.MessageEndpoint.getTest().execute(
          function(resp) {
            console.log(resp);
          }
        );
    };
    
    // little hack to be sure that apis.google.com/js/client.js is loaded
    // before calling
    // onload -> init() -> window.init() -> then here
    $window.init = function() {
	    console.log("windowinit called");
	    var rootApi = 'http://localhost:8080/_ah/api/';
	    gapi.client.load('MessageEndpoint', 'v1', function() {
	    	console.log("message api loaded");
	    	$scope.listMyMessages();
	    }, rootApi);

        //gapi.load('auth2', initSigninV2);
    }
}]);

app.controller('twittCtrl', ['$scope', '$window', function($scope, $window) {
    
}]);


/*function onSuccess(googleUser) {
	var profile = googleUser.getBasicProfile();
	console.log('ID: ' + profile.getId());
	console.log('Name: ' + profile.getName());
	console.log('Image URL: ' + profile.getImageUrl());
	console.log('Email: ' + profile.getEmail());
	window.location.href = "#!twitt";
}

function onFailure() {
	console.log("Sign in failed");
}*/

function signOut() {
  var auth2 = gapi.auth2.getAuthInstance();
  auth2.signOut().then(function () {
    console.log('User signed out.');
    document.location.href="#";
  });
}

var init = function() {
	console.log("init called");
	window.init();
};
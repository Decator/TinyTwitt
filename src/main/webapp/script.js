var app = angular.module('tinyTwitt', ['ngRoute']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "partials/main.html",
        controller : "mainCtrl"
    })
    .when("/twitt/:id", {
        templateUrl : "partials/twitt.html",
        controller : "twittCtrl"
    })
    .otherwise({
        redirectTo: '/'
    });
}]);

app.controller('twittCtrl', ['$scope', '$window', '$routeParams', function($scope, $window, $routeParams) {
	$scope.text = null;
	$scope.hastags = null;
	$scope.id = $routeParams.id;
	
	$scope.addMessage = function() {
		gapi.client.tinytwittendpoint.addMessage({userId: +($scope.id), body:$scope.text}).execute(
				function(resp) {
					console.log(resp);
				}
		);
	}
}]);

app.controller('mainCtrl', ['$scope', '$window', function($scope, $window) {
	
	$scope.user = null;
    
    $window.onSuccess = function(googleUser) {
    	console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
    	$scope.profile = googleUser.getBasicProfile();

    	gapi.client.tinytwittendpoint.getUser({userId: +($scope.profile.getId())}).execute(
          function(resp) {
            if(resp.id == null){
            	gapi.client.tinytwittendpoint.addUser({userId: +($scope.profile.getId()), pseudo:$scope.profile.getName()}).execute(
            		function(resp) {
            			$scope.user = resp;
            	    	window.location.href = "#!twitt/"+$scope.profile.getId()+"";
            		}
            	);
            } else {
            	$scope.user = resp;
            	window.location.href = "#!twitt/"+$scope.profile.getId()+"";
            }
          }
        );
    };
    
    $window.init = function() {
	    console.log("windowinit called");
	    var rootApi = 'https://tinytwitt-227514.appspot.com/_ah/api/';
	    gapi.client.load('tinytwittendpoint', 'v1', function() {
	    	console.log("message api loaded");
	    }, rootApi);
    }
}]);

function signOut() {
  var auth2 = gapi.auth2.getAuthInstance();
  auth2.signOut().then(function () {
    console.log('User signed out.');
    document.location.href="#";
    renderButton();
  });
}
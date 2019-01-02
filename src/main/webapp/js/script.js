var app = angular.module('routeApp', [
	'ngRoute',
	'routeAppControllers'
]);

app.config(['$routeProvider',
    function($routeProvider) { 
        $routeProvider
        .when('/home', {
            templateUrl: 'partials/home.html',
            controller: 'homeCtrl'
        })
        .when('/twitt', {
            templateUrl: 'partials/twitt.html',
            controller: 'twittCtrl'
        })
        .otherwise({
            redirectTo: '/home'
        });
    }
]);

var appControllers = angular.module('routeAppControllers', []);

appControllers.controller('homeCtrl', ['$scope',
    function($scope){
        $scope.message = "Bienvenue sur la page d'accueil";
    }
]);

appControllers.controller('contactCtrl', ['$scope',
    function($scope){
        $scope.message = "Laissez-nous un message sur la page de contact !";
        $scope.msg = "Bonne chance pour cette nouvelle appli !";
    }
]);

/*var app = angular.module('tinyTwitt', []).controller('Controller', ['$scope', '$window', function($scope, $window) {

	$scope.messages = [{
		id : 1,
		sender : "sender",
		owner : "owner",
		body : "body",
		date : new Date()
	}, {
		id : 2,
		sender : "sender",
		owner : "owner",
		body : "body",
		date : new Date()
	}, {
		id : 3,
		sender : "sender",
		owner : "owner",
		body : "body",
		date : new Date()
	}];
	
	$scope.count = 4;
	
    $scope.myFunc = function() {
        $scope.count++;
    };
    
    $scope.addMessage = function(body) {
    	console.log("add message");
    	console.log(body);
    	$scope.messages.push({
			id : $scope.count,
			sender : "sender",
			owner : "owner",
			body : body,
			date : new Date()
		});
    	$scope.count++;
    	console.log("ah");
    }
    
    $scope.getTimeline = function() {
    	
    }
    
    $scope.followUser = function() {
    	
    }
}]);*/

function onSignIn(googleUser) {
	var profile = googleUser.getBasicProfile();
	console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
	console.log('Name: ' + profile.getName());
	console.log('Image URL: ' + profile.getImageUrl());
	console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
}

function onSignInFailure() {
	console.log("Sign in failed");
}

function signOut() {
  var auth2 = gapi.auth2.getAuthInstance();
  auth2.signOut().then(function () {
    console.log('User signed out.');
  });
}

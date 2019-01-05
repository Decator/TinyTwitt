var app = angular.module('tinyTwitt', ['ngRoute', 'services']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
//    .when("/", {
//        templateUrl : "partials/main.html",
//        controller : "mainCtrl"
//    })
    .when("/", {
        templateUrl : "partials/twitt.html",
        controller : "twittCtrl"
    })
    .when("/users", {
    	templateUrl : "partials/users.html",
    	controller : "twittCtrl"
    })
    .when("/user/:id", {
    	templateUrl : "partials/user.html",
    	controller : "userCtrl"
    })
    .otherwise({
        redirectTo: '/twitt/'
    });
}]);

function signOut() {
  var auth2 = gapi.auth2.getAuthInstance();
  auth2.signOut().then(function () {
    console.log('User signed out.');
    document.location.href="#";
    renderButton();
  });
}
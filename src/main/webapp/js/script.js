var app = angular.module('tinyTwitt', ['ngRoute', 'services']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "partials/logger.html",
        controller : "loggerCtrl"
    })
    .when("/main", {
        templateUrl : "partials/main.html",
        controller : "mainCtrl"
    })
    .when("/users/:search", {
    	templateUrl : "partials/users.html",
    	controller : "usersCtrl"
    })
    .when("/user/:id", {
    	templateUrl : "partials/user.html",
    	controller : "userCtrl"
    })
    .when("/hashtag/:hashtag", {
    	templateUrl : "partials/hashtag.html",
    	controller : "hashtagCtrl"
    })
    .otherwise({
        redirectTo: '/'
    });
}]);
var app = angular.module('tinyTwitt', []).controller('Controller', ['$scope', '$window', function($scope, $window) {
	
	
}]);

function signOut() {
	console.log("pourt");
	var auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut();
}
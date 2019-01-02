var app = angular.module('tinyTwitt', []).controller('Controller', ['$scope', '$window', function($scope, $window) {

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
}]);

function onSignIn(){
	console.log("signed in");
}

function onSignInFailure() {
	console.log("sign in failed");
}

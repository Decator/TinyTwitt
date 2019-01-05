app.controller('twittCtrl', ['$scope', '$window', '$routeParams', 'GoogleAuth', function($scope, $window, $routeParams, GoogleAuth) {
	
	$scope.text = "";
	$scope.hastags = "";
	$scope.search = "";
	
	$scope.messages = [{
		id: "12345678901234567890",
		sender: "tibodu47",
		owner: "owner",
		body: "bonjour, voici mon tweet",
		date: "date"
	}, {
		id: "12345678901234567890",
		sender: "maudjevd",
		owner: "owner",
		body: "coucou je m'appelle maudjecoucou je m'appelle maudjecoucou je m'appelle maudjecoucou je m'appelle maudjecoucou je m'appelle maudjecoucou je m'appelle maudjecoucou je m'appelle maudjecoucou je m'appelle maudje",
		date: "date"
	}, {
		id: "12345678901234567890",
		sender: "martijntje",
		owner: "owner",
		body: "comment on écrit un tweet ?",
		date: "date"
	}];
	
	$scope.users = [{
		id: "12345678901234567890",
		username: "tibodu47",
		followers: [{username: "martijntje"}],
		following: [{username: "maudjevd"}]
	}, {
		id: "12345678901234567890",
		username: "maudjevd",
		followers: [{username: "martijntje"}, {username: "tibodu47"}],
		following: [{username: "martijntje"}]
	}, {
		id: "12345678901234567890",
		username: "martijntje",
		followers: [{username: "maudjevd"}],
		following: [{username: "maudjevd"}, {username: "tibodu47"}]
	}, {
		id: "12345678901234567890",
		username: "martijntje",
		followers: [{username: "maudjevd"}],
		following: [{username: "maudjevd"}, {username: "tibodu47"}]
	}];
	
	$scope.id = $routeParams.id;
	
	$scope.addMessage = function() {
		if($scope.text != ""){
			gapi.client.tinytwittendpoint.addMessage({userId: +($scope.id), body:$scope.text}).execute(
					function(resp) {
						$scope.text = "";
						$scope.hashtags = "";
						console.log(resp);
					}
			);
		} else {
			console.log("error : cannot send empty tweet");
		}
	}
	
	$scope.searchUsers = function() {
		console.log($scope.search);
//		if($scope.search != ""){
//			gapi.client.tinytwittendpoint.findUsersByUsername({username: $scope.search}).execute(
//					function(resp) {
//						console.log(resp);
//						$scope.users = resp;
//						window.location.href = "#!users/";
//					}
//			);
//		}
		
		//remove this !!!
		window.location.href = "#!users/";
	}
	
	$scope.goToUser = function(user) {
		console.log("goToUser");
		window.location.href = "#!user/";
	}
	
	$scope.followUser = function(user) {
		console.log("followUser");
		gapi.client.tinytwittendpoint.followUser({userId: $scope.id, userToFollowId: user.id}).execute(
			function(resp) {
				console.log(resp);
			}
		);
	}

	$scope.loadMessages = function () {
//		gapi.client.tinytwittendpoint.getTimeline({userId: $scope.id}).execute(
//			function(resp) {
//				console.log(resp);
//				$scope.messages = resp;
//			}
//		);
	};

	$scope.loadMessages();
}]);
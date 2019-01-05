app.controller('userCtrl', ['$scope', '$window', '$routeParams', 'GoogleAuth', function($scope, $window, $routeParams, GoogleAuth) {
	
	$scope.id = $routeParams.id;
	
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
app.controller('hashtagCtrl', ['$scope', '$window', '$routeParams', 'GoogleAuth', function($scope, $window, $routeParams, GoogleAuth) {
	
	$scope.hashtag = $routeParams.hashtag;
	$scope.messages = [];
	$scope.idCurrentUser = GoogleAuth.getIdGoogleAuth();
	$scope.nameCurrentUser = GoogleAuth.getNameGoogleAuth();
	$scope.imageUrlCurrentUser = GoogleAuth.getImageUrlGoogleAuth();
	
	$scope.checkLogin = function(){
		if(GoogleAuth.getIdGoogleAuth() == null) {
			window.location.href = "#!";
		} else {
			$scope.loadMessages();
		}
	};
	
	$scope.signOut = function() {
		var auth2 = gapi.auth2.getAuthInstance();
		auth2.signOut().then(function () {
			GoogleAuth.setIdGoogleAuth(null);
			GoogleAuth.setNameGoogleAuth(null);
			GoogleAuth.setImageUrlGoogleAuth(null);
			console.log('User signed out.');
			document.location.href="#!";
			renderButton();
		});
	}
	
	$scope.loadMessages = function() {
		var timeBefore = new Date().getTime();
		gapi.client.tinytwittendpoint.getMessageHashtags({hashtag: $scope.hashtag}).execute(
			function(resp) {
				var timeAfter = new Date().getTime();
				M.toast({html: "Get tweets by hashtag : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
				$scope.messages.length = 0;
				if(resp.items != null){
					for(var i=0; i<resp.items.length; i++){
						var splitBody = resp.items[i].body.split(' ');
						var splitMessage = [];
						var finalTags = [];
						
						splitBody.forEach(function(element) {
							if(element.charAt(0) == '#'){
								finalTags.push(element);
							} else {
								splitMessage.push(element);
							}
						});
						
						var finalMessage = splitMessage.join(' ');
						
						resp.items[i].body = finalMessage;
						resp.items[i].hashtags = finalTags;
						
						$scope.messages.push(resp.items[i]);
					}
				}
				$scope.$apply();
			}
		);
	};
	
	$scope.checkLogin();
}]);
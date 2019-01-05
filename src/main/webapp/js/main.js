app.controller('mainCtrl', ['$scope', '$window', '$routeParams', 'GoogleAuth', function($scope, $window, $routeParams, GoogleAuth) {
	
	$scope.text = "";
	$scope.hashtags = "";
	$scope.search = "";
	$scope.messages = [];
	
	$scope.addMessage = function() {
		if($scope.text != ""){
			if($scope.hashtags != ""){
				console.log($scope.hashtags);
				var splitTags = $scope.hashtags.split(' ');
				var finalTags = [];
				splitTags.forEach(function(element) {
					element = element.replace(/[!?'@#$%^&*éèùàüëö,;.:=+-<>]/g, "");
					element = element.toLowerCase();
					finalTags.push(element);
				});
				console.log(finalTags);
				var timeBefore = new Date().getTime();
				gapi.client.tinytwittendpoint.addMessage({userId: +(GoogleAuth.getIdGoogleAuth()), body:$scope.text, hashtags: finalTags}).execute(
					function(resp) {
						var timeAfter = new Date().getTime();
						M.toast({html: "Adding tweet : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
						$scope.text = "";
						$scope.hashtags = "";
						$scope.loadMessages();
						console.log(resp);
					}
				);
			} else {
				var timeBefore = new Date().getTime();
				gapi.client.tinytwittendpoint.addMessage({userId: +(GoogleAuth.getIdGoogleAuth()), body:$scope.text}).execute(
					function(resp) {
						var timeAfter = new Date().getTime();
						M.toast({html: "Adding tweet : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
						$scope.text = "";
						$scope.hashtags = "";
						$scope.loadMessages();
						console.log(resp);
					}
				);
			}
		} else {
			console.log("error : cannot send empty tweet");
		}
	};
	
	$scope.searchUsers = function() {
		window.location.href = "#!users/"+$scope.search;
	};
	
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
		gapi.client.tinytwittendpoint.getTimeline({userId: +(GoogleAuth.getIdGoogleAuth())}).execute(
			function(resp) {
				var timeAfter = new Date().getTime();
				M.toast({html: "Loading timeline : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
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
						
						console.log(resp.items[i]);
						$scope.messages.push(resp.items[i]);
					}
				}
				console.log($scope.messages);
				$scope.$apply();
			}
		);
	};

	$scope.checkLogin();
}]);
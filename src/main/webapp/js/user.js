app.controller('userCtrl', ['$scope', '$window', '$routeParams', 'GoogleAuth', function($scope, $window, $routeParams, GoogleAuth) {
	
	$scope.id = $routeParams.id;
	$scope.messages = [];
	$scope.image = "";
	
	$scope.followUser = function() {
		console.log("followUser");
		var timeBefore = new Date().getTime();
		gapi.client.tinytwittendpoint.followUser({userId: +(GoogleAuth.getIdGoogleAuth()), userToFollowId: +($scope.id)}).execute(
			function(resp) {
				var timeAfter = new Date().getTime();
				M.toast({html: "Following user : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
				if($scope.user.followers == null){
					$scope.user.followers = [];
				}
				$scope.user.followers.push((+GoogleAuth.getIdGoogleAuth()).toString());
				$scope.$apply();
			}
		);
	};
	
	$scope.unfollowUser = function() {
		console.log("unfollowUser");
		var timeBefore = new Date().getTime();
		gapi.client.tinytwittendpoint.followUser({userId: +(GoogleAuth.getIdGoogleAuth()), userToFollowId: +($scope.id)}).execute(
			function(resp) {
				var timeAfter = new Date().getTime();
				M.toast({html: "Unfollowing user : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
				$scope.user.followers.splice($scope.user.followers.indexOf((+GoogleAuth.getIdGoogleAuth()).toString()), 1);
				$scope.$apply();
			}
		);
	};
	
	$scope.showButton = function(){
		if($scope.user != null){
			return $scope.user.id != (+GoogleAuth.getIdGoogleAuth()).toString();
		} else {
			return false;
		}
	}
	
	$scope.followButton = function(){
		if($scope.user != null){
			if($scope.user.followers) {
				return $scope.user.followers.includes((+GoogleAuth.getIdGoogleAuth()).toString());
			} else {
				console.log(false);
				return false;
			}
		} else {
			return false;
		}
	};
	
	$scope.checkLogin = function(){
		if(GoogleAuth.getIdGoogleAuth() == null) {
			window.location.href = "#!";
		} else {
			$scope.loadUser();
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
	
	$scope.loadUser = function() {
		var timeBefore = new Date().getTime();
		gapi.client.tinytwittendpoint.getUser({userId: +($scope.id)}).execute(
			function(resp) {
				var timeAfter = new Date().getTime();
				console.log(resp);
				M.toast({html: "Loading user : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
				$scope.user = resp;
				$scope.image = resp.profilePic;
				$scope.$apply();
			}
		);
	};
	
	$scope.loadMessages = function() {
		var timeBefore = new Date().getTime();
		gapi.client.tinytwittendpoint.getMyMessages({userId: +($scope.id)}).execute(
			function(resp) {
				var timeAfter = new Date().getTime();
				M.toast({html: "Loading tweets : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
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
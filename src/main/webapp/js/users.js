app.controller('usersCtrl', ['$scope', '$window', '$routeParams', 'GoogleAuth', function($scope, $window, $routeParams, GoogleAuth) {

	$scope.users = [];
	
	$scope.followUser = function(user) {
		console.log("followUser");
		var timeBefore = new Date().getTime();
		gapi.client.tinytwittendpoint.followUser({userId: +(GoogleAuth.getIdGoogleAuth()), userToFollowId: +(user.id)}).execute(
			function(resp) {
				var timeAfter = new Date().getTime();
				M.toast({html: "Following user : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
				console.log(resp);
				if(user.followers == null){
					user.followers = [];
				}
				user.followers.push((+GoogleAuth.getIdGoogleAuth()).toString());
				$scope.$apply();
			}
		);
	};
	
	$scope.unfollowUser = function(user) {
		console.log("unfollowUser");
		var timeBefore = new Date().getTime();
		gapi.client.tinytwittendpoint.followUser({userId: +(GoogleAuth.getIdGoogleAuth()), userToFollowId: +(user.id)}).execute(
			function(resp) {
				var timeAfter = new Date().getTime();
				M.toast({html: "Unfollowing user : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
				console.log(resp);
				user.followers.splice(user.followers.indexOf((+GoogleAuth.getIdGoogleAuth()).toString()), 1 );
				$scope.$apply();
			}
		);
	};
	
	$scope.followButton = function(user){
		if(user.followers) {
			return user.followers.includes((+GoogleAuth.getIdGoogleAuth()).toString());
		} else {
			return false;
		}
	};
	
	$scope.checkLogin = function(){
		if(GoogleAuth.getIdGoogleAuth() == null) {
			window.location.href = "#!";
		} else {
			$scope.searchUsers($routeParams.search);
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
	
	$scope.searchUsers = function(search) {
		if(search != ""){
			var timeBefore = new Date().getTime();
			gapi.client.tinytwittendpoint.findUsersByUsername({username: search}).execute(
				function(resp) {
					var timeAfter = new Date().getTime();
					M.toast({html: "Loading users : "+(timeAfter-timeBefore)+"ms", classes: 'rounded'});
					console.log(resp);
					$scope.users.length = 0;
					if(resp.items != null){
						for(var i=0; i<resp.items.length; i++){
							if(resp.items[i].id != (+GoogleAuth.getIdGoogleAuth()).toString()){
								$scope.users.push(resp.items[i]);
							}
						}
					}
					$scope.$apply();
				}
			);
		}
	};

	$scope.checkLogin();
}]);
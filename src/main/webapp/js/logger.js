app.controller('loggerCtrl', ['$scope', '$window', '$document', 'GoogleAuth', function($scope, $window, $document, GoogleAuth) {
	
	$scope.user = null;
	$scope.waitingForApi = false;
	$scope.googleUser = null;
    
    $window.onSuccess = function(googleUser) {
    	GoogleAuth.setIdGoogleAuth(googleUser.getBasicProfile().getId());
    	GoogleAuth.setNameGoogleAuth(googleUser.getBasicProfile().getName());
    	GoogleAuth.setImageUrlGoogleAuth(googleUser.getBasicProfile().getImageUrl());
    	console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
    	
    	if(gapi.client == null){
        	$scope.waitingForApi = true;
        	$scope.googleUser = googleUser;
    	} else {
    		gapi.client.tinytwittendpoint.getUser({userId: +(GoogleAuth.getIdGoogleAuth())}).execute(
	          function(resp) {
	            if(resp.id == null){
	            	gapi.client.tinytwittendpoint.addUser({userId: +(GoogleAuth.getIdGoogleAuth()), pseudo: GoogleAuth.getNameGoogleAuth(), profilePic: GoogleAuth.getImageUrlGoogleAuth()}).execute(
	            		function(resp) {
	            			$scope.user = resp;
	            	    	window.location.href = "#!main";
	            		}
	            	);
	            } else {
	            	$scope.user = resp;
	            	if($scope.user.profilePic != GoogleAuth.getImageUrlGoogleAuth() || $scope.user.username != GoogleAuth.getNameGoogleAuth()) {
	            		gapi.client.tinytwittendpoint.updateUser({userId: +(GoogleAuth.getIdGoogleAuth()), pseudo: GoogleAuth.getNameGoogleAuth(), profilePic: GoogleAuth.getImageUrlGoogleAuth()}).execute(
            				function(resp) {
    	            			$scope.user = resp;
    	            	    	window.location.href = "#!main";
    	            		}
    	            	);
	            	} else {
		            	window.location.href = "#!main";
	            	}
	            }
	          }
	        );
    	}
    };
    
    
    $window.init = function() {
        console.log("windowinit called");
        var rootApi = 'https://tinytwitt-227514.appspot.com/_ah/api/';
        gapi.client.load('tinytwittendpoint', 'v1', function() {
        	console.log("message api loaded");
        	renderButton();
        	
        	if($scope.waitingForApi){
        		$scope.waitingForApi = false;
        		onSuccess($scope.googleUser);
        	}
        }, rootApi);
    }
    
    $document.ready(function(){
    	if(gapi.client != null){
        	renderButton();
    	}
    });
}]);

function init() {
	console.log("init called");
	window.init();
}

function renderButton() {
    gapi.signin2.render('my-signin2', {
      'scope': 'profile email',
      'width': 300,
      'height': 60,
      'longtitle': true,
      'theme': 'dark',
      'onsuccess': onSuccess,
      'onfailure': onFailure
    });
}

function onSuccess(googleUser) {
	window.onSuccess(googleUser);
}
	
function onFailure(error) {
	console.log(error);
}
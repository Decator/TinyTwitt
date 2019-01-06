app.controller('loggerCtrl', ['$scope', '$window', '$document', 'GoogleAuth', function($scope, $window, $document, GoogleAuth) {
	
	$scope.user = null;
    
    $window.onSuccess = function(googleUser) {
    	GoogleAuth.setIdGoogleAuth(googleUser.getBasicProfile().getId());
    	GoogleAuth.setNameGoogleAuth(googleUser.getBasicProfile().getName());
    	GoogleAuth.setImageUrlGoogleAuth(googleUser.getBasicProfile().getImageUrl());
    	console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
    	
    	gapi.client.tinytwittendpoint.getUser({userId: +(GoogleAuth.getIdGoogleAuth())}).execute(
          function(resp) {
            if(resp.id == null){
            	gapi.client.tinytwittendpoint.addUser({userId: +(GoogleAuth.getIdGoogleAuth()), pseudo: GoogleAuth.getNameGoogleAuth(), profilePic: GoogleAuth.getImageUrlGoogleAuth()}).execute(
            		function(resp) {
            			console.log(resp);
            			$scope.user = resp;
            	    	window.location.href = "#!main";
            		}
            	);
            } else {
    			console.log(resp);
            	$scope.user = resp;
            	window.location.href = "#!main";
            }
          }
        );
    };
    
    $window.init = function() {
        console.log("windowinit called");
        var rootApi = 'https://tinytwitt-227514.appspot.com/_ah/api/';
        gapi.client.load('tinytwittendpoint', 'v1', function() {
        	console.log("message api loaded");
        	renderButton();
        }, rootApi);
    }
    
    $document.ready(function(){
    	if(gapi.client.tinytwittendpoint != null){
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
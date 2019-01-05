app.controller('mainCtrl', ['$scope', '$window', 'GoogleAuth', function($scope, $window, GoogleAuth) {
	
	$scope.user = null;
    
    $window.onSuccess = function(googleUser) {
    	console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
    	GoogleAuth.setGoogleAuth(googleUser.getBasicProfile());
    	$scope.profile = googleUser.getBasicProfile();
    	
    	gapi.client.tinytwittendpoint.getUser({userId: +($scope.profile.getId())}).execute(
          function(resp) {
            if(resp.id == null){
            	gapi.client.tinytwittendpoint.addUser({userId: +($scope.profile.getId()), pseudo:$scope.profile.getName()}).execute(
            		function(resp) {
            			$scope.user = resp;
            	    	window.location.href = "#!twitt";
            		}
            	);
            } else {
            	$scope.user = resp;
            	window.location.href = "#!twitt";
            }
          }
        );
    };
    
    $window.init = function() {
	    console.log("windowinit called");
	    var rootApi = 'https://tinytwitt-227514.appspot.com/_ah/api/';
//	    gapi.client.load('tinytwittendpoint', 'v1', function() {
//	    	console.log("message api loaded");
//	    }, rootApi);
    }
}]);
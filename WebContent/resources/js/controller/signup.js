function SignupCtrl($scope,$http,sharedService,$routeParams) {
	
	$scope.signupUser = {};
	$scope.signupUser.acceptTerms = false;
	
	$scope.signup = function() {
		console.log('logging in');
		
		$http({
			url : baseUrl + '/signup',
			data : $scope.signupUser,
			method : 'POST',
			headers : {'Content-Type':'application/json; charset=UTF-8'}
		}).success($scope.signupSuccess);
	};
	
	$scope.signupSuccess = function(data, status, headers, config) {
		if( !data.success )
		{
			$scope.errorMessage = data.message;
			$scope.error = true;
		}
		else
		{
			sharedService.broadcastItem(data,'handleBroadcast');
			if( $rootScope.currentPage ) {
				window.location.href = $rootScope.currentPage;
			}
			else if( data && data.lawyer ) {
				window.location.href = '#!/lawyer/profile';
			}
			else {
				window.location.href = '#!/customer/questions';
			}
		}
	};
	
	$scope.setAsLawyer = function(asLawyer) {
		$scope.signupUser.asLawyer = asLawyer;
		if( asLawyer ) {
			$scope.radioClassUser = "";
			$scope.radioClassLawyer = "checked"
		}
		else {
			$scope.radioClassUser = "checked";
			$scope.radioClassLawyer = ""
		}
	};
	
	if( $routeParams.asLawyer ) {
		if( $routeParams.asLawyer == 'lawyer' ) $scope.setAsLawyer(true);
		else $scope.setAsLawyer(false);
	}
	else {
		$scope.setAsLawyer(true); // set to lawyer as default as only lawyers will be signing up
	}
}
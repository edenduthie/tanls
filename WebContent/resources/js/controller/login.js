function LoginCtrl($scope,$http,sharedService,$rootScope) {
	
	$scope.loginUser = {};
	
	if($rootScope.loginMessage) {
		$scope.loginMessage = $rootScope.loginMessage;
		$rootScope.loginMessage = undefined;
	}
	
	$scope.login = function() {
		console.log('logging in');
		
		$http({
			url : baseUrl + '/login',
			data : $scope.loginUser,
			method : 'POST',
			headers : {'Content-Type':'application/json; charset=UTF-8'}
		}).success($scope.loginSuccess);
	};
	
	$scope.loginSuccess = function(data, status, headers, config) {
		if( !data.success )
		{
			$scope.errorMessage = "Invalid email/password, please try again.";
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
}
function UserSettingsCtrl($scope,$http) {
	
	$scope.emailPassword = {};
	
	$scope.updateEmailPassword = function() {
		
		$scope.emailPassword.email = $scope.user.email;
		
		$http({
			url : baseUrl + '/account',
			data : $scope.emailPassword,
			method : 'PUT',
			headers : {'Content-Type':'application/json; charset=UTF-8'}
		}).success(function(data, status, headers, config){
			if( !data.success ) {
		    	$scope.errorMessage = data.message;
		    	$scope.error = true;
		    }
		    else {
		    	$scope.successMessage = "Changes saved";
		    	$scope.success = true;
		    	$scope.user = data;
		    }
		});
	};
}
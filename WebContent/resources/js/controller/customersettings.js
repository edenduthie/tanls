function CustomerSettingsCtrl($scope,$http,User,$rootScope,Customer) {
	
	$scope.isLawyer = true;
	
	if( !$scope.user ) {
		$scope.user = User.get({}, function(user) {
		    if( !user.success ) {
		    	$rootScope.currentPage = '#!/customer/settings';
		    	window.location.href = '#!/login';
		    }
		});
	}
	
	$scope.customer = Customer.query();
	
	$scope.edit = function() {
		$scope.customer = Customer.update($scope.customer,function(data){
		    if( !data.success ) {
		    	$scope.globalErrorMessage = data.message;
		    	$scope.globalError = true;
		    }
		    else {
		    	$scope.globalSuccessMessage = "Changes saved";
		    	$scope.globalSuccess = true;
		    }
		});
	};
}
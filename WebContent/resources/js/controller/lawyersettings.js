function LawyerSettingsCtrl($scope,$http,Lawyer,User,$rootScope) {
	
	$scope.isLawyer = true;
	
	if( !$scope.user ) {
		$scope.user = User.get({}, function(user) {
		    if( !user.success ) {
		    	$rootScope.currentPage = '#!/lawyer/settings';
		    	window.location.href = '#!/login';
		    }
		});
	}
	
	$scope.lawyer = Lawyer.query();
	
	$scope.edit = function() {
		$scope.lawyer = Lawyer.update($scope.lawyer,function(data){
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
function BaseCtrl($scope,$http,sharedService) 
{	
	var baseCtrl = {};
    
    $scope.baseUrl = baseUrl;
    
    $scope.$on('handleBroadcast', function() {
        $scope.user = sharedService.items['handleBroadcast'];
    });
    
    $scope.loadUser = function() {
    	
		$http({
			url : baseUrl + '/user',
			method : 'GET'
		}).success(function(data, status, headers, config) {
			if( data.success ){
				$scope.user = data;
			}
		});
    };
    
    $scope.loadUser();
	
	baseCtrl.loadUserResult = function(data, status, headers, config) {
		if( data.success ){
			$scope.user = data;
			User.setUser($scope.user);
		}
	};
}
function TimelineCtrl($scope,$http,sharedService) {
	
	$scope.profileItems = [];
	
	$scope.type = 'timeline';
	
	$scope.init = function(type) {
		$scope.type = type;
	}
	
	$scope.load = function() {
		var nocache = new Date().getTime();
		var url = baseUrl+'/lawyer/'+$scope.lawyer.id+'/items';
		var type = '';
		if( $scope.type == 'timeline' ) url += '?';
		else if( $scope.type == 'career' ) url += '?type=JOB&'; 
		url += 'nocache='+nocache;
		
		$http.get(url).success(function(data) {
		    $scope.lawyer = data;
		    if( !data.success ) {
		    	console.log(error);
		    }
		    else {
		    	$scope.profileItems = data.profileItems;
		    	$scope.toggleReload();
		    }
		});
	};
}
function FileCtrl($scope,$http)
{
	
	$scope.setSlider = function(value) {
		if( !$scope.fileHolder.currentFile.feedback ) $scope.fileHolder.currentFile.feedback = {};
		$scope.fileHolder.currentFile.feedback.rating = value;
		console.log('rating: ' + value);
	};
	
	$scope.init = function(fileHolder) {
		$scope.fileHolder = fileHolder;
	};
	
	$scope.completing = false;
	
	$scope.complete = function() {
		if( !$scope.completing ) {
			
			$scope.completing = true;
			
			var nocache = new Date().getTime();
			$http.get(baseUrl+'/lawyer/files/'+$scope.fileHolder.currentFile.id+'/complete'+'?nocache='+nocache).success(function(data) {
			    if( !data.success ) {
					$scope.errorMessage = data.message;
					$scope.error = true;
			    }
			    else {
			    	$scope.fileHolder.currentFile.status = data.status;
			    }
			    $scope.completing = false;
			}).error(function(){
				$scope.errorMessage = 'There was a problem contacting the server, please try again.';
				$scope.error = true;
				$scope.completing = false;
			});
		}
	};
	
	$scope.submittingFeedback = false;
	
	$scope.feedback = function(close) {
		
		if( !$scope.submittingFeedback ) {
			
			$scope.submittingFeedback = true;
			
			var profileItem = clone($scope.fileHolder.currentFile.feedback);
			
			profileItem.createdTime = new Date().getTime();
			profileItem.timelineTime = profileItem.createdTime;
			profileItem.status = "NEW";
			if( !profileItem.rating ) profileItem.rating = 5;
			
			var nocache = new Date().getTime();
			$http.post(baseUrl+'/customer/files/'+$scope.fileHolder.currentFile.id+'/feedback'+'?nocache='+nocache,profileItem).success(function(data) {
			    if( !data.success ) {
					$scope.errorMessage = data.message;
					$scope.error = true;
			    }
			    else {
			    	$scope.fileHolder.currentFile.feedback = data;
			    	$scope.fileHolder.currentFile.status = 'FEEDBACK RECEIVED';
			    }
			    $scope.submittingFeedback = false;
			}).error(function(){
				$scope.errorMessage = 'There was a problem contacting the server, please try again.';
				$scope.error = true;
				$scope.submittingFeedback = false;
			});
		}
	};
}
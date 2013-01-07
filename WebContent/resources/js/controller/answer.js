function AnswerCtrl($scope,$http) {
    
	if( $scope.question.answer ) $scope.whatToShow = 'answered';
	else $scope.whatToShow = 'write-a-response';
	
	$scope.setWhatToShow = function(value) {
		$scope.whatToShow = value;
	};
    
	$scope.answer = function() {
		$scope.error = false;
		
		var answer = clone($scope.answer);
		answer.question = $scope.question;
		
		var nocache = new Date().getTime();
		$http.post(baseUrl+'/answers'+'?nocache='+nocache,answer).success(function(data) {
		    if( !data.success ) {
		    	$scope.errorMessage = data.message;
		    	$scope.error = true;
		    }
		    else {
		    	$scope.question.answer = data;
		    	$scope.setWhatToShow('answered');
		    	$scope.question.answers += 1;
		    	$('.tooltip').hide();
		    }
		}).error(function(){
			$scope.errorMessage = "Failed to connect to the server, please try again.";
			$scope.error = true;
		});
	};
	
	$scope.edit = function(close) {
		$scope.error = false;
		
		var answer = clone($scope.question.answer);
		answer.question = clone($scope.question);
		answer.question.answer = null;
		
		var nocache = new Date().getTime();
		$http.put(baseUrl+'/answers'+'?nocache='+nocache,answer).success(function(data) {
		    if( !data.success ) {
		    	$scope.errorMessage = data.message;
		    	$scope.error = true;
		    }
		    else {
		    	$scope.question.answer = data;
		    	$scope.setWhatToShow('answered');
		    }
		}).error(function(){
			$scope.errorMessage = "Failed to connect to the server, please try again.";
			$scope.error = true;
		});
	};
}
function QuestionCtrl($scope,$http,$rootScope) {
	
	$scope.question = {};
	
	if( $rootScope.question ) $scope.question = $rootScope.question;
	
	$scope.lawyerSignUp = function() {
		window.location.href = '#!/signup/lawyer';
	};
	
	if( !$scope.user || !$scope.user.success ) {
		$rootScope.loginMessage = 'Log in or sign up before sending your question to our community of lawyers so you can receive the responses!';
		$rootScope.currentPage = '#!/question/ask';
		window.location.href = '#!/login';	
	}
	
	$scope.areas = function() {
		var nocache = new Date().getTime();
		$http.get(baseUrl+'/autocomplete/areasofpractise'+'?nocache='+nocache).success(function(data) {
		    $scope.areasOrPractise = data;
		});
	};
	
	$scope.areas();
	
	$scope.customer = function() {
		var nocache = new Date().getTime();
		$http.get(baseUrl+'/customer'+'?nocache='+nocache).success(function(data) {
		    $scope.question.customer = data;
		});
	};
	
	$scope.customer();
	
	$scope.asking = false;
	
	$scope.ask = function(close) {
		if( !$scope.asking ) {
			$scope.asking = true;
			$scope.error = false;
			
			var question = clone($scope.question);
			if( question.customer.companyName ) question.businessQuestion = true;
			else question.businessQuestion = false;
			
			var nocache = new Date().getTime();
			$http.post(baseUrl+'/questions'+'?nocache='+nocache,question).success(function(data) {
			    if( !data.success ) {
			    	$scope.errorMessage = data.message;
			    	$scope.error = true;
			    }
			    else {
			    	$('.tooltip').hide();
			    	window.location.href = '#!/customer/questions';
			    }
			    $scope.asking = false;
			}).error(function(){
				$scope.errorMessage = "Failed to connect to the server, please try again.";
				$scope.asking = false;
				$scope.error = true;
			});
		}
	};
}
function AskQuestionCtrl($scope,$http,$rootScope) {
	
	$scope.question = {};
	
	if( $rootScope.question ) $scope.question = $rootScope.question;
	
	$scope.lawyerSignUp = function() {
		window.location.href = '#!/signup/lawyer';
	};
	
	$scope.go = function() {
		console.log($scope.question.text);
		$rootScope.question = $scope.question;
		if( $scope.user && $scope.user.success )
		{
			window.location.href = '#!/question/ask';	
		}
		else
		{
			$rootScope.loginMessage = 'Log in or sign up before sending your question to our community of lawyers so you can receive the responses!';
			$rootScope.currentPage = '#!/question/ask';
			window.location.href = '#!/login';	
		}
	};
}
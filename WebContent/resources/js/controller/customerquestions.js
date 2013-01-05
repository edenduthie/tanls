function CustomerQuestionsCtrl($scope,$http,$routeParams,$rootScope,User) {
	
	$scope.limit = 15;
	$scope.offset = 0;
	$scope.loadMore = true;
	
	$scope.hideAnswers = false; 
	if( isMobile() ) { $scope.hideAnswers = true; }
	
	if( !$scope.user ) {
		$scope.user = User.get({}, function(user) {
		    if( !user.success ) {
		    	$rootScope.currentPage = '#!/customer/questions';
		    	if($routeParams.questionId) $rootScope.currentPage += '/' + $routeParams.questionId;
		    	window.location.href = '#!/login';
		    }
		});
	}
	
	if( $routeParams.questionId )
	{
		$scope.hideAnswers = false; // always show answers for a single question even if mobile
		
		$scope.questions = new Array();
		$scope.loadMore = false;
		
		$scope.getQuestion = function() {
			var nocache = new Date().getTime();
			
			var url = baseUrl+'/customer/questions/' + $routeParams.questionId;
			url += '?nocache='+nocache;
			
			$http.get(url).success(function(data) {
			    $scope.questions.push(data);
			    $scope.loadAnswers(data);
			});
		};
		
		$scope.getQuestion();
	}
	else
	{
	
	    $scope.questions = new Array();
	
		$scope.getQuestions = function() {
			var nocache = new Date().getTime();
			
			var url = baseUrl+'/customer/questions'+'?nocache='+nocache;
			url += "&limit=" + $scope.limit;
			url += "&offset=" + $scope.offset;
			
			$http.get(url).success(function(data) {
			    $scope.questions = $scope.questions.concat(data);
			    $scope.offset += data.length;
			    if( data.length <=0 ) $scope.loadMore = false;
			});
		};
		
		$scope.getQuestions();
	}
	
	$scope.answersLimit = 30;
	//$scope.currentStatus = 'NEW';
	$scope.messageText = "Click on a question to see lawyer responses.";
	
	/*
	$scope.changeStatus = function(newStatus) {
		$scope.currentStatus = newStatus;
		$scope.selectNavValue = newStatus;
		if( $scope.currentQuestion ) $scope.loadAnswers($scope.currentQuestion);
	};
	*/
	
	$scope.getAnswers = function() {
		var nocache = new Date().getTime();
		
		var url = baseUrl+'/answers/all/'+$scope.currentQuestion.id+'?nocache='+nocache;
		url += "&limit=" + $scope.answersLimit;
		url += "&offset=" + $scope.answersOffset;
		//url += "&status=" + $scope.currentStatus;
		
		$http.get(url).success(function(data) {
		    $scope.answers = $scope.answers.concat(data);
		    
		    if( $scope.answersOffset == 0 ) $(window).scrollTop(0);
		    
		    $scope.answersOffset += data.length;
		    if( data.length <=0 ) 
		    {
		    	$scope.messageText = "No more answers for this question.";
		    	$scope.loadMoreAnswers = false;
		    }
		});
	};
	
	$scope.loadAnswers = function(question) {
		
		if( isMobile() ) window.location.href = '#!/customer/questions/' + question.id;
		
		$scope.currentQuestion = question;
		$scope.answersOffset = 0;
		$scope.answers = new Array();
		$scope.loadMoreAnswers = true;
		
		$scope.getAnswers();
	};
	
	$scope.status = function(answer,status) {
		$scope.error = false;
		
		answer.status = status;
		
		var answer = clone(answer);
		answer.question = clone($scope.question);
		answer.question.answer = null;
		
		var nocache = new Date().getTime();
		$http.put(baseUrl+'/answers/status'+'?nocache='+nocache,answer).success(function(data) {
		    if( !data.success ) {
		    	alert(data.message);
		    }
		    else {
		    	console.log('status changed');
		    }
		}).error(function(){
			alert('Failed to connect to the server, please try again.');
		});
	};
	
	$scope.book = function(quote) {
		var url = '#!/customer/book/' + quote.id;
		$('.tooltip').hide();
		window.location.href = url;
	};
};
	
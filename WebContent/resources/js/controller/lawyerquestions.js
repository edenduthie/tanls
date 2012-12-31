function LawyerQuestionsCtrl($scope,$http,$rootScope,User,$routeParams) {
	
	$scope.isLawyer = true;
	$scope.editProfile = true;
	
	$scope.load = function() {
		
		var nocache = new Date().getTime();
		$http.get(baseUrl+'/lawyer/profile'+'?nocache='+nocache).success(function(data) {
		    $scope.lawyer = data;
		    if( !data.success ) {
		    	console.log('oops, we could not find your profile. Please try again or email eden@easylaw.com.au for help');
		    }
		});
	};
	
	$scope.limit = 10;
	$scope.offset = 0;
	$scope.questions = new Array();
	$scope.loading = false;
	
	$scope.getQuestions = function() {
		var nocache = new Date().getTime();
		$scope.loading = true;
		
		var url = baseUrl+'/lawyer/questions'+'?nocache='+nocache;
		url += "&limit=" + $scope.limit;
		url += "&offset=" + $scope.offset;
		
		$http.get(url).success(function(data) {
		    $scope.questions = $scope.questions.concat(data);
		    $scope.offset += data.length;
		    if( data.length > 0 ) $scope.startScroll();
		    $scope.loading = false;
		}).error(function(){
			$scope.loading = false;
		});
	};
	
	$scope.getQuestion = function() {
		var nocache = new Date().getTime();
		$scope.loading = true;
		
		var url = baseUrl+'/lawyer/questions/'+$scope.questionId+'?nocache='+nocache;
		
		$http.get(url).success(function(data) {
			if( data.success )
			{
		        $scope.questions.push(data);
			}
			$scope.loading = false;
		}).error(function(){
			$scope.loading = false;
		});
	};
	
	$scope.start = function() {
	    $scope.load();
	    if( $routeParams.questionId ) 
	    {
	    	$scope.questionId = $routeParams.questionId;
	    	$scope.getQuestion();
	    }
	    else $scope.getQuestions();
	};
	
	if( !$scope.user ) {
		$scope.user = User.get({}, function(user) {
		    if( !user.success ) {
		    	$rootScope.currentPage = '#!/lawyer/questions';
		    	window.location.href = '#!/login';
		    }
		    else $scope.start();
		});
	}
	else $scope.start();
	
    $scope.startScroll = function() {
	    $(window).scroll(function() {
		   if($(window).scrollTop() + $(window).height() > $(document).height() - 2) {
		       $(window).unbind('scroll');
		       $scope.getQuestions(true);
		   }
		});
    };
};
	
function ProfileCtrl($scope,$http,$routeParams,sharedService) {
	
	$scope.lawyer = {};
	$scope.username = $routeParams.username;
	$scope.isLawyer = true;
	if( $scope.username ) $scope.isLawyer = false;
	
	$scope.modified = false;
	
	$scope.load = function() {
		var nocache = new Date().getTime();
		$http.get(baseUrl+'/lawyer/profile'+'?nocache='+nocache).success(function(data) {
		    $scope.lawyer = data;
		    if( !data.success ) {
		    	console.log('oops, we could not find your profile. Please try again or email eden@easylaw.com.au for help');
		    	window.location.href = '#!/';
		    }
		    else {
		    	$scope.toDate($scope.lawyer);
		    	$scope.goTo('timeline');
		    }
		});
	};
	
	if( $scope.username ) {
		var nocache = new Date().getTime();
		$http.get(baseUrl+'/lawyers/'+$scope.username+'?nocache='+nocache).success(function(data) {
		    $scope.lawyer = data;
		    if( !data.success ) {
		    	console.log('oops, we could not find the lawyer. Please try again or email eden@easylaw.com.au for help');
		    	window.location.href = '#!/';
		    }
		    else $scope.goTo('timeline');
		});
	}
	else {
		$scope.load();	
	}
	
	$scope.editing = false;
	
	$scope.toDate = function(lawyer) {
		if( lawyer.timeAdmitted ) lawyer.timeAdmitted = new Date(lawyer.timeAdmitted);
	};
	$scope.fromDate = function(lawyer) {
		if( lawyer.timeAdmitted ) {
			lawyer.timeAdmitted = lawyer.timeAdmitted.getTime();
		}
	};
	
	$scope.edit = function(close) {
		if( !$scope.editing ) {
			$scope.editing = true;
			var nocache = new Date().getTime();
			var copy = clone($scope.lawyer);
			$scope.fromDate(copy);
			$http.put(baseUrl+'/lawyer/profile'+'?nocache='+nocache,copy).success(function(data) {
			    if( !data.success ) {
			    	alert(data.message);
			    }
			    else {
			    	$scope.modified = false;
			    }
			    $scope.editing = false;
			}).error(function(){
				$scope.editing = false;
			});
		}
	};
	
	$scope.currentLocation = 'timeline';
	
	$scope.goTo = function(location) {
		$scope.currentLocation = location;
		if( $scope.currentLocation == 'timeline' ) {
			$scope.jobOrEducationSwitch('STATUS_UPDATE');
		}
		else if( $scope.currentLocation == 'career' ) {
			$scope.jobOrEducationSwitch('JOB');
		}
		else if( $scope.currentLocation == 'articles' ) {
			$scope.jobOrEducationSwitch('ARTICLE');
		}
		else if( $scope.currentLocation == 'reviews' ) {
			$scope.jobOrEducationSwitch('FEEDBACK');
		}
		$scope.loadTimeline();
	};
	
	$scope.limit = 5;
	
	$scope.lastTimelineTime = new Array();
	
	$scope.loading = false;
	
	$scope.loadTimeline = function(next) {
		var nocache = new Date().getTime();
		var url = baseUrl+'/lawyer/'+$scope.lawyer.id+'/items';
		var type = '';
		if( $scope.currentLocation == 'timeline' ) url += '?';
		else if( $scope.currentLocation == 'career' ) url += '?type=JOB&'; 
		else if( $scope.currentLocation == 'articles' ) url += '?type=ARTICLE&';
		else if( $scope.currentLocation == 'reviews' ) url += '?type=FEEDBACK&';
		url += 'limit=' + $scope.limit + "&";
		if( next && $scope.lastTimelineTime[$scope.currentLocation]) {
			url += 'lastTimelineTime=' + $scope.lastTimelineTime[$scope.currentLocation] + "&";
		}
		url += 'nocache='+nocache;
		
		$scope.loading = true;
		
		$http.get(url).success(function(data) {
			
			$scope.loading = false;
			
		    if( !data.success ) {
		    	console.log(data.message);
		    }
		    else {
		    	if( $scope.currentLocation == 'timeline' ) {
		    		if( next ) {
		    			$scope.timelineItems = $scope.timelineItems.concat(data.profileItems);
		    		    $scope.profileItems = $scope.profileItems.concat(data.profileItems);
		    		}
		    		else {
		    		    $scope.timelineItems = data.profileItems;
		    		    $scope.profileItems = $scope.timelineItems;
		    		}
		    	}
		    	else if( $scope.currentLocation == 'career' ) {
		    		if( next ) {
		    			$scope.careerItems = $scope.careerItems.concat(data.profileItems);
		    		    $scope.profileItems = $scope.profileItems.concat(data.profileItems);
		    		}
		    		else {
		    		    $scope.careerItems = data.profileItems;
		    		    $scope.profileItems = $scope.careerItems;
		    		}
		    	}
		    	else if( $scope.currentLocation == 'articles' ) {
		    		if( next ) {
		    			$scope.articleItems = $scope.articleItems.concat(data.profileItems);
		    		    $scope.profileItems = $scope.profileItems.concat(data.profileItems);
		    		}
		    		else {
		    		    $scope.articleItems = data.profileItems;
		    		    $scope.profileItems = $scope.articleItems;
		    		}
		    	}
		    	else if( $scope.currentLocation == 'reviews' ) {
		    		if( next ) {
		    			$scope.reviewItems = $scope.articleItems.concat(data.profileItems);
		    		    $scope.profileItems = $scope.profileItems.concat(data.profileItems);
		    		}
		    		else {
		    		    $scope.reviewItems = data.profileItems;
		    		    $scope.profileItems = $scope.reviewItems;
		    		}
		    	}
		    	if( data.profileItems && data.profileItems.length > 0 )
		    	{
		    		var lastIndex = data.profileItems.length - 1;
		    	    $scope.lastTimelineTime[$scope.currentLocation] = data.profileItems[lastIndex].timelineTime;
		    	    $scope.startScroll();
		    	}
		    }
		}).error(function(){
			$scope.loading = false;
		});
	};
	
	$scope.timelineItems = [];
	$scope.careerItems = [];
	$scope.articleItems = [];
	$scope.profileItems = [];
	
	
    $scope.jobOrEducation = 'STATUS_UPDATE';
	
	$scope.jobOrEducationSwitch = function(target) {
		$scope.jobOrEducation = target;
	};
	
    $scope.$on('reloadTimeline', function() {
        $scope.loadTimeline();
    });
    
    $scope.$on('globalError', function() {
    	$scope.globalErrorMessage = sharedService.items['globalError'];
    	if( !$scope.globalErrorMessage ) $scope.globalErrorMessage = "There has been an error, please try again.";
    	$scope.globalError = true;
    });
    
    $scope.$on('lawyerChanged', function() {
    	$scope.lawyer = sharedService.items['lawyerChanged'];
    });
    
    $scope.goHome = function() {
    	window.location.href = '#!/';
    };
    
    $scope.globalError = false;
    
    $scope.startScroll = function() {
	    $(window).scroll(function() {
		   if($(window).scrollTop() + $(window).height() > $(document).height() - 2) {
		       $(window).unbind('scroll');
		       $scope.loadTimeline(true);
		   }
		});
    };
}
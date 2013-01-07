function ArticlesCtrl($scope,$http,$routeParams) {
	
	$scope.singleArticle = false;
	$scope.singleCSSClass = '';
	
	var title = $routeParams.title;
	
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
	
	if( title && title.length > 0 && title != 'unapproved'  ) {
		
		$scope.singleArticle = true;
		$scope.singleCSSClass = 'single';
		
		$scope.loadArticle = function(next) {
			var nocache = new Date().getTime();
			var url = baseUrl+'/items/' + title;
			url += '?nocache='+nocache;
			
			$http.get(url).success(function(data) {
				
			    if( !data.success ) {
			    	console.log(data.message);
			    }
			    else {
			    	$scope.profileItems = new Array();
			    	$scope.profileItems.push(data);
			    }
			});
		};
		
		$scope.loadArticle();
	}
	else {
		$scope.loading = false;
		$scope.limit = 5;
		
		$scope.profileItems = [];
		
		$scope.loadArticles = function(next) {
			var nocache = new Date().getTime();
			var url = baseUrl+'/items?';
			url += 'limit=' + $scope.limit + "&";
			url += 'type=ARTICLE&';
			if( title && title == 'unapproved' ) {}
			else url += 'status=APPROVED&';
			if( next && $scope.lastTimelineTime ) {
				url += 'lastTimelineTime=' + $scope.lastTimelineTime + "&";
			}
			url += 'nocache='+nocache;
			
			$scope.loading = true;
			
			$http.get(url).success(function(data) {
				
				$scope.loading = false;
				
			    if( !data.success ) {
			    	console.log(data.message);
			    }
			    else {
			    	$scope.profileItems = $scope.profileItems.concat(data.profileItems); 
			    	if( data.profileItems && data.profileItems.length > 0 )
			    	{
			    		var lastIndex = data.profileItems.length - 1;
			    	    $scope.lastTimelineTime = data.profileItems[lastIndex].timelineTime;
			    	    $scope.startScroll();
			    	}
			    }
			}).error(function(){
				$scope.loading = false;
			});
		};
		
	    function getDocHeight() {
	        var D = document;
	        return Math.max(
	            Math.max(D.body.scrollHeight, D.documentElement.scrollHeight),
	            Math.max(D.body.offsetHeight, D.documentElement.offsetHeight),
	            Math.max(D.body.clientHeight, D.documentElement.clientHeight)
	        );
	    };
	    
	    $scope.startScroll = function() {
		    $(window).scroll(function() {
			   if($(window).scrollTop() + $(window).height() > $(document).height() - 2) {
			       $(window).unbind('scroll');
			       $scope.loadArticles(true);
			   }
			});
	    };
	    
	    $scope.loadArticles();
	}
    
	$scope.archive = [];
	$scope.archiveLoading = false;
	$scope.archiveLimit = 50;
    
	$scope.loadArchive = function(next) {
		var nocache = new Date().getTime();
		var url = baseUrl+'/itemsNoText?';
		url += 'limit=' + $scope.archiveLimit + "&";
		url += 'type=ARTICLE&';
		if( title && title == 'unapproved' ) {}
		else url += 'status=APPROVED&';
		if( next && $scope.lastArchiveTime ) {
			url += 'lastTimelineTime=' + $scope.lastArchiveTime + "&";
		}
		url += 'nocache='+nocache;
		
		$scope.archiveLoading = true;
		
		$http.get(url).success(function(data) {
			
			$scope.archiveLoading = false;
			
		    if( !data.success ) {
		    	console.log(data.message);
		    }
		    else {
		    	$scope.archive = $scope.archive.concat(data.profileItems); 
		    	if( data.profileItems && data.profileItems.length > 0 )
		    	{
		    		var lastIndex = data.profileItems.length - 1;
		    	    $scope.lastArchiveTime = data.profileItems[lastIndex].timelineTime;
		    	}
		    }
		}).error(function(){
			$scope.archiveLoading = false;
		});
	};
	
	$scope.loadMore = function() {
		$scope.loadArchive(true);
	};
	
	$scope.loadArchive();
	
	$scope.setStatus = function(profileItem,status) {
		
		var copy = clone(profileItem);
		copy.status = status;
		var lawyerSkeleton = { id : profileItem.lawyer.id };
		copy.lawyer = lawyerSkeleton;
		
		var nocache = new Date().getTime();
		$http.put(baseUrl+'/lawyer/items/status'+'?nocache='+nocache,copy).success(function(data) {
		    if( !data.success ) {
		    	if( data.message ) alert(data.message);
		    	else alert("Invalid input, please try again");
		    }
		    else {
		    	profileItem.status = status;
		    	alert('status has been changed to: ' + status);
		    }
		}).error(function(){
			alert('There was a problem contacting the server, please try again.');
		});
	};
}
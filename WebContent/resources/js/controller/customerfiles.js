function CustomerFilesCtrl($scope,$http,$rootScope,$timeout,$routeParams,$location,User)
{
	$scope.fileHolder = {};
	$scope.mobile = isMobile();
	$scope.editProfile = true;
	
	var thePath = $location.path() + "";
    if(thePath.search('/customer') != -1) $scope.customer = true;
    else {
    	$scope.isLawyer = true;
    	$scope.customer = false;
    	
    	$scope.loadLawyer = function() {
    		var nocache = new Date().getTime();
    		$http.get(baseUrl+'/lawyer/profile'+'?nocache='+nocache).success(function(data) {
    		    $scope.lawyer = data;
    		    if( !data.success ) {
    		    	console.log('oops, we could not find your profile. Please try again or email eden@easylaw.com.au for help');
    		    }
    		});
    	};
    	$scope.loadLawyer();
    }
	
	if( !$scope.user ) {
		$scope.user = User.get({}, function(user) {
		    if( !user.success ) {
		    	$rootScope.currentPage = '#!/customer/files';
		    	window.location.href = '#!/login';
		    }
		});
	}
	
	$scope.setSlider = function(value) {
		$scope.rating = value;
	};
	
	$scope.limit = 20;
	$scope.offset = 0;
	$scope.loadMore = true;
	
	$scope.files = [];
	
	$scope.displayFirstFileAutomatically = true;
	
	//$scope.haveListScroll = false;
	//$scope.haveFileScroll = false;
	
	$scope.getFile = function() {
		var nocache = new Date().getTime();
		
		var url = baseUrl+'/';
		if( $scope.customer ) url += 'customer';
		else url += 'lawyer';
		url += '/files/'+$routeParams.fileId+'?nocache='+nocache;
		url += "&limit=" + $scope.limit;
		url += "&offset=" + $scope.offset;
		
		$http.get(url).success(function(data) {
            if( data )
            {
            	$scope.fileHolder.currentFile = data;
            	/*
			    if( !$scope.haveFileScroll ) {
			    	$timeout(function(){$('.file-content').jScrollPane()});
			    	$scope.haveFileScroll = true;
			    }
			    */
            }
		});
	};
	
	$scope.hideList = false;
	
	if( $routeParams.fileId ) {
		$scope.displayFirstFileAutomatically = false;
		$scope.getFile();
		if( isMobile() ) $scope.hideList = true;
	}
	
	$scope.getFiles = function() {
		var nocache = new Date().getTime();
		
		var url = baseUrl+'/';
		if( $scope.customer ) url += 'customer';
		else url += 'lawyer';
		url += '/files'+'?nocache='+nocache;
		url += "&limit=" + $scope.limit;
		url += "&offset=" + $scope.offset;
		
		$http.get(url).success(function(data) {
		    $scope.files = $scope.files.concat(data);
		    if( $scope.offset == 0 && data.length > 0 )
		    {
		    	if( $scope.displayFirstFileAutomatically ) 
		    	{
		    		$scope.fileHolder.currentFile = data[0];
		    		$('.files ul.list li').first().addClass('selected');
		    	}
		    	/*
		    	if( !$scope.haveListScroll ) {
			    	$timeout(function(){$('.files ul.list').jScrollPane();});
			    	$scope.haveListScroll = true;
			    }
			    if( !$scope.haveFileScroll ) {
			    	$timeout(function(){$('.file-content').jScrollPane();});
			    	$scope.haveFileScroll = true;
			    }
			    */
		    }
		    else
		    {
		    	//$scope.scrollPaneAll();
		    }
		    $scope.offset += data.length;
		    if( data.length <=0 ) $scope.loadMore = false;
		    //else $scope.getFiles();
		});
	};
	
	/*
	$scope.scrollPaneAll = function() {
		var list = $('.files ul.list').data('jsp');
		$scope.scrollPaneResize(list);
		var content = $('.file-content').data('jsp');
		$scope.scrollPaneResize(content);
	};
	
	$scope.scrollPaneResize = function(api) {
		if( api ) {
			var throttleTimeout;
			if ($.browser.msie) {
				if (!throttleTimeout) {
					throttleTimeout = setTimeout(
						function()
						{
							api.reinitialise();
							throttleTimeout = null;
						},
						50
					);
				}
			} else {
				api.reinitialise();
			}
		}
	};
	*/
	
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
	
	if( !$scope.hideList ) $scope.getFiles();
	
	$scope.showFile = function(file) {
		if( isMobile() ) {
			var url = '#!/'
		    if( $scope.customer ) url += 'customer';
			else url += 'lawyer';
			url += '/files/' + file.id;
			window.location.href = url;
		}
		else {
		    $scope.fileHolder.currentFile = file;
		}
	};
	
	$scope.submittingFeedback = false;
	
	$scope.feedback = function(close) {
		if( !$scope.submittingFeedback ) {
			
			$scope.submittingFeedback = true;
			
			var profileItem = clone($scope.feedback);
			
			profileItem.createdTime = new Date().getTime();
			profileItem.timelineTime = profileItem.createdTime;
			profileItem.status = "NEW";
			profileItem.rating = $scope.rating;
			
			var nocache = new Date().getTime();
			$http.post(baseUrl+'/customer/files/'+$scope.fileHolder.currentFile.id+'/feedback'+'?nocache='+nocache,profileItem).success(function(data) {
			    if( !data.success ) {
					$scope.errorMessage = data.message;
					$scope.error = true;
			    }
			    else {
			    	$scope.fileHolder.currentFile.feedback = profileItem;
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
function ProfileItemCtrl($scope,$http,sharedService) 
{	
	var profileItemCtrl = {};
	
	$scope.init = function(profileItem) {
		if(profileItem) $scope.profileItem = $scope.convertToDateObject(profileItem);
	};
	
	$scope.newProfileItem = {};
	
	$scope.dateOptions = {
	    changeYear: true,
	    changeMonth: true,
	    dateFormat: 'dd/mm/yy',
	    yearRange: '1930:2014'
	};
	
	$scope.addingNewProfileItem = false;
	
	$scope.newProfileItem = function(close) {
		if( !$scope.addingNewProfileItem ) {
			
			$scope.addingNewProfileItem = true;
			
			var profileItem = $scope.clone($scope.newProfileItem);
			
			profileItem.type = $scope.jobOrEducation;
			if( profileItem.startTime ) profileItem.startTime = profileItem.startTime.getTime();
			if( profileItem.endTime ) profileItem.endTime = profileItem.endTime.getTime();
			profileItem.createdTime = new Date().getTime();
			if( profileItem.type == 'JOB' ) profileItem.timelineTime = profileItem.startTime;
			else if( profileItem.type == 'EDUCATION' ) profileItem.timelineTime = profileItem.endTime;
			else if( profileItem.type == 'STATUS_UPDATE' ) profileItem.timelineTime = profileItem.createdTime;
			else if( profileItem.type == 'ARTICLE' ) {
				profileItem.timelineTime = profileItem.createdTime;
			}
			var lawyerSkeleton = { id : $scope.lawyer.id };
			profileItem.lawyer = lawyerSkeleton;
			profileItem.status = "NEW";
			
			var nocache = new Date().getTime();
			$http.post(baseUrl+'/lawyer/items'+'?nocache='+nocache,profileItem).success(function(data) {
			    if( !data.success ) {
					$scope.errorMessage = data.message;
					$scope.error = true;
			    }
			    else {
			    	if( close ) close();
			    	$scope.successMessage = "Added!";
			    	$scope.success = true;
			    	sharedService.broadcastItem(data,'reloadTimeline');
			    }
			    $scope.addingNewProfileItem = false;
			}).error(function(){
				alert('There was a problem contacting the server, please try again.')
				$scope.addingNewProfileItem = false;
			});
		}
	};
	
	$scope.clone = function(object) {
        var newObj = (object instanceof Array) ? [] : {};
		for (i in object) {
		    newObj[i] = object[i]
		} return newObj;
	};
	
	$scope.editingProfileItem = false;
	
	$scope.edit = function(close) {
		if( !$scope.editingProfileItem ) {
			
			$scope.editingProfileItem = true;
			
			var profileItem = $scope.clone($scope.profileItem);
			
			profileItem = $scope.convertFromDateObject(profileItem);
			
			if( profileItem.type == 'JOB' ) profileItem.timelineTime = profileItem.startTime;
			else if( profileItem.type == 'EDUCATION' ) profileItem.timelineTime = profileItem.endTime;
			else if( profileItem.type == 'STATUS_UPDATE' ) profileItem.timelineTime = profileItem.createdTime;
			var lawyerSkeleton = { id : $scope.lawyer.id };
			profileItem.lawyer = lawyerSkeleton;
			
			var nocache = new Date().getTime();
			$http.put(baseUrl+'/lawyer/items'+'?nocache='+nocache,profileItem).success(function(data) {
			    if( !data.success ) {
			    	if( data.message ) alert(data.message);
			    	else alert("Invalid input, please try again");
			    }
			    else {
			    	close();
			    }
			    $scope.editingProfileItem = false;
			}).error(function(){
				alert('There was a problem contacting the server, please try again.')
				$scope.editingProfileItem = false;
			});
		}
	};
	
	$scope.convertToDateObject= function(profileItem) {
		if( profileItem.startTime ) profileItem.startTime = new Date(profileItem.startTime);
		if( profileItem.endTime ) profileItem.endTime = new Date(profileItem.endTime);
		profileItem.timelineTime = new Date(profileItem.timelineTime);
		profileItem.createdTime = new Date(profileItem.createdTime);
		return profileItem;
	};
	
	$scope.convertFromDateObject = function(profileItem) {
		if( profileItem.startTime ) profileItem.startTime = profileItem.startTime.getTime();
		if( profileItem.endTime ) profileItem.endTime = profileItem.endTime.getTime();
		profileItem.timelineTime = profileItem.timelineTime.getTime();
		profileItem.createdTime = profileItem.createdTime.getTime();
		return profileItem;
	};
	
    $scope.remove = function(item) {
    	if( confirm('Are you sure?') ) {
    	    $scope.deleteItem(item);
    	}
    };
	
    $scope.deleteItem = function(item) {
    	
		var nocache = new Date().getTime();
		
		var url = baseUrl+'/lawyer/items/delete'+'?nocache='+nocache;
		
		var profileItemSkeleton = {id : item.id};
		var lawyerSkeleton = { id : $scope.lawyer.id };
		profileItemSkeleton.lawyer = lawyerSkeleton;
		
		$http({method: 'POST', url: url, data : profileItemSkeleton}).
		    success(function(data, status, headers, config) {
			    if( !data.success ) {
					sharedService.broadcastItem(data.message,'globalError');
			    }
			    else {
			    	sharedService.broadcastItem(true,'reloadTimeline');
			    }
		      }).
		      error(function(data, status, headers, config) {
		    	  sharedService.broadcastItem('Error connecting to server, please try again','globalError');
		      }
	    );
	};
	
	/********* article functionality ***********/
	
	$scope.hideWriteArticlePopup = true;
	
	$scope.postArticle = function(close) {
		$scope.newProfileItem.text = $scope.editor.getData();
		if( $scope.newProfileItem.text && $scope.newProfileItem.text.length > 0 && $scope.newProfileItem.title && $scope.newProfileItem.title.length > 0 ) {
		    $scope.newProfileItem($scope.hidePopup);
		}
	};
	
	$scope.editArticle = function(close) {
		$scope.profileItem.text = $scope.editor.getData();
		if( $scope.profileItem.text && $scope.profileItem.text.length > 0 && $scope.profileItem.title && $scope.profileItem.title.length > 0 ) {
		    $scope.edit($scope.hidePopup);
		}
	};
	
	$scope.showPopup = function() {
		$scope.showPopupValue = true;
	};
	
	$scope.hidePopup = function() {
		$scope.showPopupValue = false;
	};
	
	$scope.setEditor = function(editor) {
		$scope.editor = editor;
	};
    
}
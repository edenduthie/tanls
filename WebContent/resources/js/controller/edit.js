function EditCtrl($scope,$http,$routeParams,sharedService) {
	
	$scope.lawyer = {};
	$scope.isLawyer = true;
	$scope.inWizard = false;
	$scope.showWizard = true;
	$scope.backupLawyer = {};
	
	$scope.init = function(inWizard) {
		$scope.inWizard = inWizard;
	};
	
	$scope.load = function() {
		
		console.log('loading lawyer in edit');
		
		var nocache = new Date().getTime();
		$http.get(baseUrl+'/lawyer/profile'+'?nocache='+nocache).success(function(data) {
			
		    $scope.toDate(data);
		    $scope.lawyer = data;
		    if( !data.success ) {
		    	console.log('trouble loading lawyer in edit');
		    	console.log('oops, we could not find your profile. Please try again or email eden@easylaw.com.au for help');
		    	//window.location.href = '#!/';
		    }
		    else {
		    	$scope.backupLawyer = $scope.clone($scope.lawyer);
		    	sharedService.broadcastItem($scope.backupLawyer,'lawyerChanged');
		    	//$scope.lawyer.currentSignUpStage = 2;
		    	if( data.currentSignUpStage > 2 ) $scope.showWizard = false;
		    	$('.tooltip').hide();
		    }
		});
	};
	
	$scope.areas = function() {
		var nocache = new Date().getTime();
		$http.get(baseUrl+'/autocomplete/areasofpractise'+'?nocache='+nocache).success(function(data) {
		    $scope.areasOrPractise = data;
		});
	};
	
	$scope.editing = false;
	
	$scope.edit = function(close) {
		if( !$scope.editing ) {
			$scope.editing = true;
			$scope.error = false;
			$scope.success = false;
			
			$('.tooltip').hide();
			
			var lawyerCopy = $scope.clone($scope.lawyer);
			$scope.fromDate(lawyerCopy);
			if( $scope.inWizard ) ++lawyerCopy.currentSignUpStage;
			else if( lawyerCopy.currentSignUpStage == 1 ) ++lawyerCopy.currentSignUpStage;
			
			var nocache = new Date().getTime();
			$http.put(baseUrl+'/lawyer/profile'+'?nocache='+nocache,lawyerCopy).success(function(data) {
			    if( !data.success ) {
			    	$scope.errorMessage = data.message;
			    	$scope.error = true;
			    }
			    else {
			    	$scope.successMessage = "Changes saved";
			    	$scope.success = true;
			    	$scope.toDate(data);
			    	$scope.lawyer = data;
			    	$scope.backupLawyer = $scope.clone($scope.lawyer);
			    	sharedService.broadcastItem($scope.backupLawyer,'lawyerChanged');
			    }
			    $scope.editing = false;
			}).error(function(){
				$scope.errorMessage = "Failed to connect to the server, please try again.";
				$scope.editing = false;
				$scope.error = true;
			});
		}
	};
	
	$scope.toDate = function(lawyer) {
		if( lawyer.timeAdmitted ) lawyer.timeAdmitted = new Date(lawyer.timeAdmitted);
	};
	$scope.fromDate = function(lawyer) {
		if( lawyer.timeAdmitted ) {
			lawyer.timeAdmitted = lawyer.timeAdmitted.getTime();
		}
	};
	
	$scope.clone = function(object) {
		  var newObj = (object instanceof Array) ? [] : {};
		  for (i in object) {
		    newObj[i] = object[i]
		  } return newObj;
		};
	
	$scope.clear = function() {
		$scope.error = false;
		$scope.success = false;
	}
	
	$scope.states = ["NSW","VIC","QLD","SA","WA","NT","TAS"];
	
	$scope.dateOptions = {
        changeYear: true,
        changeMonth: true,
        dateFormat: 'dd/mm/yy',
        yearRange: '1930:2014'
    };
	
	$scope.load();
	
	$scope.skip = function() {
		$scope.lawyer = $scope.backupLawyer;
		$scope.edit();
	};
	
	$scope.areas();
	
	$scope.addArea = function(input) {
		var area = input;
		if( area ) {
			if( !$scope.lawyer.areasOfPractise ) $scope.lawyer.areasOfPractise = new Array();
			$scope.lawyer.areasOfPractise.push(area);
		}
	};
	
	$scope.addNewArea = function(input) {
		var area = input;
		if( area && area.length > 0 ) {
			var areaObject = { id:null, name:area };
			if( !$scope.lawyer.areasOfPractise ) $scope.lawyer.areasOfPractise = new Array();
			$scope.lawyer.areasOfPractise.push(areaObject);
		}
	};
	
	$scope.closeArea = function(index) {
		$scope.lawyer.areasOfPractise.splice(index,1);
	};
}
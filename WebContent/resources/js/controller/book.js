function BookCtrl($scope,$http,$routeParams,Quote,User) {
	
	$scope.quoteId = $routeParams.quoteId;
	
	$scope.file = {};
	$scope.file.payment = {};
	$scope.file.payment.useExistingCustomer = false;
	Quote.get( { quoteId : $scope.quoteId }, function(data){
		$scope.file.quote = data;
		$scope.file.payment.customer = $scope.file.quote.question.customer;
		if( $scope.file.payment.customer.maskedNumber ) $scope.file.payment.useExistingCustomer = true;
		if( $scope.file.quote.question.proBono ) $scope.file.payment.useExistingCustomer = true;
	});
	

	
	if( !$scope.user ) {
		$scope.user = User.get({}, function(user) {
		    if( !user.success ) {
		    	$rootScope.currentPage = '#!/customer/book/' + $scope.quoteId;
		    	window.location.href = '#!/login';
		    }
		});
	}
	
	$scope.states = ["VIC","NSW","QLD","ACT","SA","WA","NT","TAS"];
	
	$scope.booking = false;
	
	$scope.book = function() {
		if( !$scope.booking ) {
			$scope.booking = true;
			$scope.error = false;
			
			var file = clone($scope.file);
			var dummyQuote = {id:file.quote.id};
			file.quote = dummyQuote;
			file.payment.customer.billingAddress.suburb = $scope.suburb;
			
			var nocache = new Date().getTime();
			$http.post(baseUrl+'/customer/book'+'?nocache='+nocache,file).success(function(data) {
			    if( !data.success ) {
			    	$scope.errorMessage = data.message;
			    	$scope.error = true;
			    }
			    else {
			    	window.location.href = '#!/customer/files/'+data.id;
			    }
			    $scope.booking = false;
			}).error(function(){
				$scope.errorMessage = "Failed to connect to the server, please try again.";
				$scope.booking = false;
				$scope.error = true;
			});
		}
	};
	
	$scope.setAutocompleteValue = function(value)
	{
		$scope.suburb = value;
	};
}
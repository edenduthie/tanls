function LawyersCtrl($scope,$http) {
	
	$scope.lawyers = [];
	
	$scope.offset = 0;
	$scope.limit = 20;
	
	$scope.loading = false;
	
	$scope.loadLawyers = function() {
		$scope.loading = true;
		var nocache = new Date().getTime();
		var url = baseUrl+'/lawyers'+'?offset='+$scope.offset+'&limit='+$scope.limit+'&nocache='+nocache;
		$http.get(url).success(function(data) {
		    $scope.lawyer = data;
		    if( !data.success ) {
		    	console.log('no lawyers found')
		    }
		    else {
		    	if( data.lawyers.length > 0 ) {
		    	    $scope.lawyers = $scope.lawyers.concat(data.lawyers);
		    	    $scope.offset = $scope.lawyers.length;
		    	    $scope.startScroll();
		    	}
		    	     
		    }
		    $scope.loading = false;
		}).error(function(){
			$scope.loading = false;
		});
	};
	
	$scope.loadLawyers();
	
    function getDocHeight() {
        var D = document;
        return Math.max(
            Math.max(D.body.scrollHeight, D.documentElement.scrollHeight),
            Math.max(D.body.offsetHeight, D.documentElement.offsetHeight),
            Math.max(D.body.clientHeight, D.documentElement.clientHeight)
        );
    }
    
    $scope.startScroll = function() {
	    $(window).scroll(function() {
		   if($(window).scrollTop() + $(window).height() > $(document).height() - 2) {
		       $(window).unbind('scroll');
		       $scope.loadLawyers();
		   }
		});
    }
}
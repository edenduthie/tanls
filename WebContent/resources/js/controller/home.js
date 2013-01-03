function HomeCtrl($scope,$http) 
{	
	$scope.nextItem = function(id) {
		var scrollPosition = $("#"+id).offset().top;
		$('html, body').animate({ scrollTop: scrollPosition }, 500);
	};
	
	$scope.currentScrollItem = 0;
	
	$scope.numberOfItems = 2;
	
	$scope.scrollRight = function() {
		var nextItem = $scope.currentScrollItem + 1;
		if( nextItem >= $scope.numberOfItems ) $scope.currentScrollItem = 0;
		else $scope.currentScrollItem = nextItem;
	};
	
	$scope.scrollLeft = function() {
		var nextItem = $scope.currentScrollItem - 1;
		if( nextItem < 0 ) $scope.currentScrollItem = $scope.numberOfItems - 1;
		else $scope.currentScrollItem = nextItem;
	};
	
	$scope.loadArticles = function(next) {
		var nocache = new Date().getTime();
		var url = baseUrl+'/items?limit=2&type=ARTICLE&status=APPROVED&nocache='+nocache;
		
		$http.get(url).success(function(data) {
			
		    if( !data.success ) {
		    	console.log(data.message);
		    }
		    else {
		    	$scope.profileItems = data.profileItems; 
		    }
		}).error(function(){
			console.log('error loading article');
		});
	};
	
	$scope.loadArticles();
}
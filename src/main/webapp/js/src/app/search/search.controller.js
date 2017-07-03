/**
 * Module for the main search screen.
 */
angular.module('jcsw.search.controller', ['ngResource', 'jcsw.search.factory', 'jcsw.search.directives', 'jcsw.escape.filter'])

.controller('searchController', ['$scope', 'searchFactory', '$location', function($scope, searchFactory, $location) {
	
	$scope.sel = function(evt) {
		evt.target.select();
	};
	
	$scope.search = searchFactory.searchFields;
	$scope.searchData = searchFactory.searchData;
	$scope.versions = searchFactory.versions;
	$scope.branches = searchFactory.branches;
	$scope.directories = searchFactory.directories;
	$scope.fileExtensions = searchFactory.fileExtensions;
	$scope.setFile = searchFactory.setFile;
	
	$scope.doSearch = function() {
		searchFactory.searchFields.start = 0;
		searchFactory.doSearch(false, true);
	}
	
	$scope.ofCount = function() {
		var startPlusRows = searchFactory.searchFields.start + searchFactory.searchFields.rows;
		var count = searchFactory.searchData.searchResultCount;
		
		console.log(startPlusRows);
		console.log(count);
		
		return count < startPlusRows ? count : startPlusRows;
		
	}
	
	/**
	 * Next and previous page links.
	 * @param isNext if true go to next page else previous page
	 */
	$scope.nextPrev = function(isNext) {
		var newStart;
		if(isNext) {
			newStart = $scope.search.start + $scope.search.rows;
		} else {
			newStart = $scope.search.start - $scope.search.rows;
			newStart = Math.max(newStart, 0); // don't go below 0
		}
		$scope.search.start = newStart; //update scope
		$location.search('start', newStart); // change the location for browser
		searchFactory.doSearch(false, true); // do the search
		$('html,body').animate({scrollTop: 0}); // scroll to top, non-angular, just jquery
	}
	
}]);
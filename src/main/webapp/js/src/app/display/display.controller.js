/**
 * Display a file controller.
 */
angular.module('jcsw.display.controller', ['ngResource', 'jcsw.search.factory', 'jcsw.display.factory', 'jcsw.display.directive', 'jcsw.configData'])

.controller('displayController', 
	['$rootScope', '$scope', 'searchFactory', 'displayFactory', '$location', 'configData', function($rootScope, $scope, searchFactory, displayFactory, $location, configData) {
	
	var showFile = function(f) {
		var fileObject = displayFactory.getFile(f);
		$scope.file = fileObject;
	};
	
	$scope.backOne = function() {
		var file = searchFactory.getLastFile();
		searchFactory.setFile(file);
		$location.search('file', file);
	};
	
	$scope.showBackOne = function() {
		return searchFactory.showBackOne();
	};
	
	$scope.toSearchResults = function() {
		searchFactory.clearFileStack();
		$location.url($rootScope.lastSearchUrl);
	};
	
	$scope.showLinkToSearchResults = $rootScope.lastSearchUrl != null;
	
	$scope.showFile = showFile;
	
	$scope.configData = configData;

	if(null == searchFactory.file.file) { // this is null when arriving at a search page via a bookmark
		var f = $location.search().file;
		searchFactory.setFile(f);
		showFile(f);
	} else {
		showFile(searchFactory.file.file);
	}
	
	var maxSizeToPrettyPrint = 100000;
	
	$scope.displayData = { 'maxSizeToPrettyPrint' : maxSizeToPrettyPrint};
	
	
}]);
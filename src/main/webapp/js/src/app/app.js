/**
 * Main app module.
 */
angular.module('app', ['ngRoute', 'jcsw.search.controller', 'jcsw.search.factory', 'jcsw.display.controller', 'jcsw.configData', 'jcsw.search.factory'])

.config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
	$locationProvider.html5Mode(true);
	
	$routeProvider.when('/search/home', {
		templateUrl: 'search/search'
	})
	
	.when('/search/display', {
		templateUrl: 'search/displayPage'
	})
	
	.otherwise({redirectTo: 'search/home'});
}])

.run(['$rootScope', 'searchFactory', '$resource', '$location', 'configData', function($rootScope, searchFactory, $resource, $location, configData) {
	
	// setup click navigation when going from one display (of a file) to another
	var r = $resource(configData.base + 'display/findFile');
	jcsw.setupClickGo(r, $location, searchFactory);
	
	// handle the "Home" hyperlink
	$rootScope.$on('$locationChangeSuccess', function(event, newUrl) {
		if(newUrl.endsWith('/search/home')) {
			searchFactory.reset();
		} else if(newUrl.indexOf('/search/home') > -1) {
			$rootScope.lastSearchUrl = $location.url();
		}
	})
}])
;
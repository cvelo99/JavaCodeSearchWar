/**
 * Call encodeURIComponent.
 */
angular.module('jcsw.escape.filter', [])

.filter('escape', [function() {
	return window.encodeURIComponent;
}]);
/**
 * Directives
 */
angular.module('jcsw.display.directive', [])

.directive('display', [function() {
	
	return {
		restrict : 'A',
		scope: {
			file: '=',
			displayData: '='
		},
		link: function(scope, element, attrs) { // update formatted source when file changes
			scope.$watch('file', function(n) {
				var s = typeof n != 'undefined' ? n["java_source"] : undefined;
				if(typeof s != 'undefined') {
					if(element.html() != '') {
						element.empty();
					} else {
						element.html(s);
					}
					if(s.length > scope.displayData.maxSizeToPrettyPrint) {
						scope.displayData.showNoFormat = true;
					} else {
						prettyPrint();
					}
				}
			}, true); // true says to compare using angular.equals not object comparison
		}
		
	}
	
}]);
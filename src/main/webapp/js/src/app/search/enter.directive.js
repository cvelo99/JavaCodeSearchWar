/**
 * Perform a search when pressing enter.
 */
angular.module('jcsw.search.directives', ['jcsw.search.factory'])

.directive('enterSearch', ['searchFactory', function(searchFactory) {
	
	return {
		restrict : 'A',
		link: function(scope, element, attrs) {
			element.on('keydown', function(event) {
				if(event.which == 13) {
					if($(event.target).is(':input')) {
						searchFactory.searchFields.start = 0;
						searchFactory.doSearch(true);
					}
				}
			});
		}
		
	}
	
}]);
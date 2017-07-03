/**
 * Default value and config data.
 */
angular.module('jcsw.configData', [])

.constant('defaultBranch', 'jboss5')

.factory('configData', ['$resource', 'defaultBranch', function($resource, defaultBranch) {
	
	var base = $('base').attr('href');
	
	var svnInfo = $resource(base + 'data/configData');
	
	init();
	
	var result = {
		'base' : base,
		'defaultBranch' : defaultBranch,
		svnPre: '',
		svnPost: ''
	};
	
	return result;
	
	//////////////////
	
	function init() {
		svnInfo.get(function(data) {
			angular.extend(result, data);
		});
	}
	
	
}]);


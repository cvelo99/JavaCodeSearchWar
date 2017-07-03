/**
 * Factory module to get the contents of a file.
 */
angular.module('jcsw.display.factory', ['jcsw.configData'])

.factory('displayFactory', ['configData', '$resource', function(configData, $resource) {
	
	var r = $resource(configData.base + 'display/file');
	
	return {
		getFile: getFile
	};
	
	function getFile(f) {
		return r.get({'file': f});
	};
	
}]);
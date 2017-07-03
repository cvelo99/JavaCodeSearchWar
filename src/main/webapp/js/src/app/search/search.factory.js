/**
 * Factory for handling search.
 */
angular.module('jcsw.search.factory', ['jcsw.configData'])

.factory('searchFactory', ['configData', 'filterFilter', '$http', '$location', '$rootScope', '$resource', function(configData, filterFilter, $http, $location, $rootScope, $resource) {
	
	var solrQueryResource = $resource(configData.base + 'data/search/:rows/:start/:text');
	
	var searchFieldsTemplate = { 'rows' : 10, 'start' : 0};
	var searchFields = angular.copy(searchFieldsTemplate);
	
	var templateSearchData = {
			searched : false,
			lastIndexedDate : 0,
			searchTime : 0,
			searchResultCount : 0,
			searchResults : []
		};
	
	var searchData = angular.copy(templateSearchData);
	
	var versions = [];
	var branches = [];
	var directories = [];
	var fileExtensions = [];
	
	var bookMarkedSearchData = {};
	var fileStack = [];
	var file = { file: null};
	
	init();
	
	return {
		'file' : file,
		'setFile' : setFile,
		
		// stack related
		'getLastFile' : getLastFile,
		'showBackOne' : showBackOne,
		'clearFileStack' : clearFileStack,
		
		'searchFields': searchFields, // field we input
		'searchData' : searchData, // display data
		
		// drop downs
		'versions' : versions,
		'branches' : branches,
		'directories' : directories,
		'fileExtensions' : fileExtensions,
		// search function
		'doSearch' : doSearch,
		
		// reset function
		'reset' : reset,
		'setBookMarkedSearchData' : setBookMarkedSearchData
	}
	
	
	function setBookMarkedSearchData(o) {
		angular.copy(o, bookMarkedSearchData);
		angular.extend(searchFields, bookMarkedSearchData);	
	}
	
	function reset() {
		angular.copy(templateSearchData, searchData);
		angular.copy(searchFieldsTemplate, searchFields);
	}
	
	function init() {
		$http.get(configData.base + 'data/allSearchData').then(
			function(data) {
				
				var s = $location.search();
				var pick = function(key, o) {
					return typeof s[key] != 'undefined' ? s[key] : o;
				};
				
				angular.copy(data.data.VERSIONS, versions);
				searchFieldsTemplate.version = versions[0];
				searchFields.version = Number(pick('version', versions[0]));
				
				angular.copy(data.data.BRANCHES, branches);
				searchFieldsTemplate.branch = filterFilter(branches, configData.defaultBranch)[0]; // set default branch
				searchFields.branch = pick('branch', filterFilter(branches, configData.defaultBranch)[0]); // set default branch
				
				angular.copy(data.data.DIRECTORIES, directories);
				searchFields.directory = pick('directory', undefined);
				
				angular.copy(data.data.FILE_EXTENSIONS, fileExtensions);
				searchFields.fileExtension = pick('fileExtension', undefined);
				
				searchFields.text = pick('text', undefined);
				
				searchData.lastIndexedDate = data.data.LAST_INDEX_DATE[0];
				templateSearchData.lastIndexedDate = data.data.LAST_INDEX_DATE[0];
				
				angular.extend(searchData, bookMarkedSearchData);
				
				doSearch(false, false); // handles coming here from a bookmarked url
			
			}
		)
	};
	
	function getSolrResults() {
		solrQueryResource.get(searchFields, function(data) {
			searchData.searched = true;
			searchData.searchTime = data.queryResultTime;
			searchData.searchResultCount = data.numberResultsFound;
			searchData.rows = data.results;
		});
	}
	
	/**
	 * callApply: true when searching from a click event, we don't want to wait for the model to update in onChange
	 * forceSearch: if true, always search
	 */
	function doSearch(callApply, forceSearch) {
		
		// clicking enter will call the onchange event, updating the model, pressing enter will not
		// so for pressing enter, callApply is false
		if(angular.isDefined(callApply) && true == callApply) {
			$rootScope.$apply(); // react to the location change right away rather than on blur/change
		}
		
		if(forceSearch || !angular.equals(searchFields, searchFieldsTemplate)) {
			$location.search(searchFields);
			getSolrResults();
		}
	};
	
	function setFile(f) {
		file.file = f;
		fileStack.push(f);
	}
	
	function getLastFile() {
		fileStack.pop();
		var file = fileStack.pop();
		return file;
	}
	
	function showBackOne() {
		return fileStack.length > 1;
	}
	
	function clearFileStack() {
		fileStack.length = 0;
	}
	
	
}]);
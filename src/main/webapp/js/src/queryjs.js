
var jcsw = (function() {
	
	
	var setupClickGo = function(r, $location, searchFactory) {
		$('body').on('click', '.typ', function() {
			var className = $(this).html();
			var params = {
				'dsp.className': className,
				'dsp.id': $('.idOfFile').html()
			};
			
			var scope = angular.element(this).parents('pre.prettyprint').scope();
			var id = scope.file.id;
			r.get({filePath: id, elementClickedOn : className}, function(data) {
				if(null != data && null != data.file) {
					searchFactory.setFile(data.file);
					$location.search('file', data.file);
				} else {
					alert('Unable to resolve ' + className);
				}
			});
			return false;
		});
	};
	
	return {
		setupClickGo: setupClickGo
	}
	
})();
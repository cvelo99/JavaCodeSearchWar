<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div ng-app="display" ng-controller="displayController">

	<div class="no_format" ng-show="displayData.showNoFormat">This file was not formatted because the size is greater than {{ displayData.maxSizeToPrettyPrint | number }} characters.
		Click <a href="" onClick="prettyPrint(); return false;"> here </a> to format it.</div>
	
	<div class="fr">
		<div>branch: {{ file.branch }}</div>
		<div>version: {{ file.jboss_version }}</div>
		<div><a target="_blank" href="{{ configData.svnPre }}{{ file.full_file_path }}{{ configData.svnPost }}">view in svn</a></div>
	</div>

	<div>
		<div class="returnUrl"></div>
		<div class="idOfFile">{{ file.full_file_path }}</div>
	</div>
	
	<div>
		<a class="displayNav" ng-if="showLinkToSearchResults == false" ng-href="search/home">&#8592; Home</a>
		<a class="displayNav" ng-click="toSearchResults();" ng-if="showLinkToSearchResults">&#8592; Return to Search Results</a>
		<a class="displayNav" style="padding-left:20px" ng-click="backOne();" ng-if="showBackOne();">&#8592; Back One</a>
	</div>
	
	<div style="clear:both"></div>
	
	<pre class="prettyprint linenums" ng-show="file.size > 0"><%-- avoid showing a code block then having text appear --%>
	
		<code display file="file" display-data="displayData"></code>
	
	</pre>
	
</div>

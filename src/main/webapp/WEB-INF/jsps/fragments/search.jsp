<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<div class="page" ng-app="searchArea" ng-controller="searchController" enter-search>

	<div class="pull-left panel searchLeftPanel">
		<div class="searchLine"><a class="home" href="search/home">Search</a></div>
		<div>
			<label>Version:</label>
			<span><select ng-model="search.version" ng-options="v for v in versions"><option value=""></option></select></span>		
		</div>
		<div>
			<label>Branch:</label>
			<span><select ng-model="search.branch" ng-options="v for v in branches"><option value=""></option></select></span>		
		</div>
		<div>
			<label>Directory:</label>
			<span><select ng-model="search.directory" ng-options="v for v in directories"><option value=""></option></select></span>		
		</div>
		<div>
			<label>File Ext:</label>
			<span><select ng-model="search.fileExtension" ng-options="v for v in fileExtensions"><option value=""></option></select></span>		
		</div>
		<div style="margin-top:20px">
			last indexed: {{ searchData.lastIndexedDate | date: 'MM/dd/yy' }}
		</div>
	</div>
	
	<div class="pull-left">
		
		Query: <input ng-model="search.text" size="100" autofocus ng-focus="sel($event)"><button type="button" class="searchButton" ng-click="doSearch()">Search</button>
	
		<div class="results">
				<div ng-if="searchData.searched && searchData.searchResultCount == 0">No results.</div>
				<div class="counter">
					<div ng-if="searchData.searched && searchData.searchResultCount > 0">
						Displaying results {{ search.start + 1 }} - {{ ofCount() }} of {{ searchData.searchResultCount }} in {{ searchData.searchTime }} milliseconds.
					</div>
				</div>
				
				<div class="fl pt10 w100" ng-if="searchData.searched && searchData.searchResultCount > 0">
					<div ng-repeat="row in searchData.rows" class="searchrow w100">
						<a class="fileLink" ng-href="search/display?file={{row.full_file_path | escape}}" ng-click="setFile(row.full_file_path)" style="display:block">
							{{ row.file_name }}
							<span class="fr silver">
								v: {{ row.jboss_version }} branch: {{ row.branch }}
							</span>
							<div style="clear:both"> </div>
							<span class="path_line silver">
								{{ row.full_file_path }}
							</span>
						</a>
						<div style="clear:both; border-bottom: solid thin #c0c0c0;"> </div>
					</div>
				</div>
			<div class="counter" ng-if="
					searchData.searched
					&& (
					search.start > 0
					||
					search.start + search.rows < searchData.searchResultCount
					)
			">
				<span ng-if="search.start > 0">
					<a ng-click="nextPrev(false)" href="">Previous</a>				
				</span>
				<span ng-if="search.start + search.rows < searchData.searchResultCount">
					<a ng-click="nextPrev(true)" href="">Next</a>				
				</span>
			</div>
		</div>
	</div>
</div>

<script>var e = $('div.pull-left input').first(); e.focus(); e.select();</script>

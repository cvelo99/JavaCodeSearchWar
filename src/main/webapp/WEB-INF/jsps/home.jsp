<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<base href="<%= request.getContextPath() %>/">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="js/src/libs/jquery-2.2.3.min.js"></script>
<script src="js/src/libs/bootstrap.js"></script>
<script src="js/src/libs/angular.min.js"></script>
<script src="js/src/libs/angular-resource.min.js"></script>
<script src="js/src/libs/angular-route.min.js"></script>
<script src="js/src/libs/prettify.js"></script>

<script src="js/src/app/app.js"></script>

<script src="js/src/app/search/search.controller.js"></script>
<script src="js/src/app/search/enter.directive.js"></script>
<script src="js/src/app/search/search.factory.js"></script>

<script src="js/src/app/display/display.controller.js"></script>
<script src="js/src/app/display/display.factory.js"></script>
<script src="js/src/app/display/display.directive.js"></script>

<script src="js/src/app/utils/escape.filter.js"></script>
<script src="js/src/app/utils/config.data.factory.js"></script>

<script src="js/src/queryjs.js"></script>

<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/searchcss.css">
<link rel="stylesheet" href="css/prettify.css">
<title>Java Code Search</title>
</head>
<body ng-app="app">
<ng-view/>
</body>
</html>
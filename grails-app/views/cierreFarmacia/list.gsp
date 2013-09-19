<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'entrada.label', default: 'Cierre')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
	
		<g:render template="/cierre/listTemplate" 
		model="[cierreInstanceList: cierreInstanceList,
			 cierreInstanceTotal: cierreInstanceTotal,
			 almacen:almacen,isAdmin:isAdmin]" />		
		
		
	</body>
</html>

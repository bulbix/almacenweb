<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'entrada.label', default: 'Entrada')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
	
		<g:render template="/entrada/listTemplate" 
			model="[entradaInstanceList: entradaInstanceList, entradaInstanceTotal: entradaInstanceTotal]" />
	</body>
</html>

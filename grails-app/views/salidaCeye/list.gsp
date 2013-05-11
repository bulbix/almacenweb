

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'salida.label', default: 'Salida')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<g:render template="/salida/listTemplate" 
			model="[salidaInstanceList: almacenInstanceList, salidaInstanceTotal: almacenInstanceTotal,almacen:almacen]" />
	</body>
</html>

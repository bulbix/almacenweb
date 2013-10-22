<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName"	value="${message(code: 'entrada.label', default: 'Reporte')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>

	<g:render template="/reporte/reporteTemplate" 
	model="[reportName:'ceye/reporteSala',
		    methodName:'reporteSala',
		   reportDisplay:'ReporteSala',
		   fechaInicial:fechaInicial,
		   fechaFinal:fechaFinal,
		   claveInicial:claveInicial,
		   claveFinal:claveFinal,
		   partidaList:partidaList,
		   almacen:almacen]" />
		   
</body>
</html>
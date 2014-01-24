<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName"	value="${message(code: 'entrada.label', default: 'Reporte')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>

	<g:render template="/reporte/reporteDynamicTemplate" 
	model="[methodName:'reporteConcentradoServicioEntrada',
		   reportDisplay:'ReporteConcentradoServicioEntrada',
		   fechaInicial:fechaInicial,
		   fechaFinal:fechaFinal,
		   almacen:almacen]" />
		   
</body>
</html>
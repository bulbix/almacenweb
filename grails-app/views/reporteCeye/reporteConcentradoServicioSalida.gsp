<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName"	value="${message(code: 'entrada.label', default: 'Reporte')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>

	<g:render template="/reporte/reporteItextTemplate" 
	model="[methodName:'reporteConcentradoServicioSalida',
		   reportDisplay:'ReporteConcentradoServicioSalida',
		   fechaInicial:fechaInicial,
		   fechaFinal:fechaFinal,
		   claveInicial:claveInicial,
		   claveFinal:claveFinal,
		   partidaList:partidaList,
		   almacen:almacen]" />
		   
</body>
</html>
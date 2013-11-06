<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName"	value="${message(code: 'entrada.label', default: 'Reporte')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>

	
	

	<g:render template="/reporte/reporteTemplate" 
	model="[reportName:'ceye/reporteExistenciaConjunto',
		   methodName:'reporteExistenciaConjunto',
		   reportDisplay:'ReporteExistenciaConjunto',
		   fechaInicial:fechaInicial,
		   fechaFinal:fechaFinal,
		   claveInicial:claveInicial,
		   claveFinal:claveFinal,
		   partidaList:partidaList,
		   almacen:almacen]" />
		   
	<label style="color:blue;font-size:16px">Para generar el reporte de manera correcta debe existir cierre en el mes anterior a la fecha inicial en los tres modulos</label>
		   
</body>
</html>
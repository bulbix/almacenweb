<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName"	value="${message(code: 'entrada.label', default: 'Reporte')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
		   
	<g:jasperReport jasper="ceye/reporteConvertidora" format="PDF,XLSX" delimiter=" " name="ReporteConvertidora" 
		controller="${controllerName}" action="reporte" id="reportForm">
		
		<input type="hidden" name="almacen" value="${almacen}" />
		<input type="hidden" name="methodName" value="reporteConvertidora" />
	</g:jasperReport>	
	
</body>
</html>
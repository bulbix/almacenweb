<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName"	value="${message(code: 'entrada.label', default: 'Entrada')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>

	<script type="text/javascript">
			var url = '/almacenWeb/entradaFarmacia'
	</script>
				
	<g:render template="/salida/createTemplate" model="[usuariosList:usuariosList,salidaInstance: salidaInstance]" />
	
</body>
</html>

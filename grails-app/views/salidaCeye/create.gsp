
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'salida.label', default: 'Salida')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
	
		<script type="text/javascript">
			var url = '/almacenWeb/salidaCeye'
			var almacen = 'C'
		</script>
				
		<g:render template="/salida/createTemplate" model="[usuariosList:usuariosList,salidaInstance: almacenInstance,
		existeCierre:existeCierre,isDueno:isDueno]" />
		
	</body>
</html>

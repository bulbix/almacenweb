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
	
	<g:render template="/entrada/createTemplate" 
	model="[usuariosList:usuariosList,entradaInstance:almacenInstance, existeCierre:existeCierre,isDueno:isDueno]" />
	
</body>
</html>

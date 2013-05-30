<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName"	value="${message(code: 'cierre.label', default: 'Inventario')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>


	<script type="text/javascript">
			var url = '/almacenWeb/inventarioFarmacia'
	</script>
			
	<g:render template="/inventario/createTemplate"  model="[fecha:fecha]" />
	
</body>
</html>

<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName"	value="${message(code: 'cierre.label', default: 'Cierre')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>

	<script type="text/javascript">
			var url = '/almacenWeb/cierreFarmacia'
	</script>
				
	<g:render template="/cierre/createTemplate"  model="[cierreInstance:cierreInstance]" />
	
</body>
</html>



<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'salida.label', default: 'Salida')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<g:render template="/salida/listTemplate" 
			model="[salidaInstanceList: almacenInstanceList, salidaInstanceTotal: almacenInstanceTotal,almacen:almacen]" />
			
			
		<div class="paginateButtons">
		    <filterpane:paginate total="${almacenInstanceTotal}" domainBean="mx.gob.inr.ceye.SalidaCeye"/>
		    <filterpane:filterButton text="Busqueda" appliedText="Cambiar Filtro"/>
		    <filterpane:isNotFiltered>Lista Completa, No Filtrada!</filterpane:isNotFiltered>
		    <filterpane:isFiltered>Filtro Aplicado!</filterpane:isFiltered>
  		</div>	
		
			
		<filterpane:filterPane domain="mx.gob.inr.ceye.SalidaCeye" 
		associatedProperties="usuario.username,area.desArea,diagnostico.descdiag" 
		filterProperties="folio,fecha,usuario,area,diagnostico,paqueteq,estado,almacen"/>
	</body>
</html>

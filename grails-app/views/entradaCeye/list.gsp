<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'entrada.label', default: 'Entrada')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
	
		<g:render template="/entrada/listTemplate" 
			model="[entradaInstanceList: almacenInstanceList, entradaInstanceTotal: almacenInstanceTotal, almacen:almacen]" />
		
		
	<div class="paginateButtons">
		    <filterpane:paginate total="${entradaInstanceTotal}" domainBean="mx.gob.inr.ceye.EntradaCeye"/>
		    <filterpane:filterButton text="Busqueda" appliedText="Cambiar Filtro"/>
		    <filterpane:isNotFiltered>Lista Completa, No Filtrada!</filterpane:isNotFiltered>
		    <filterpane:isFiltered>Filtro Aplicado!</filterpane:isFiltered>
  	</div>	
		
			
	<filterpane:filterPane domain="mx.gob.inr.ceye.EntradaCeye" 
		associatedProperties="usuario.username,area.desArea" 
		filterProperties="folio,fecha,numeroFactura,usuario,paqueteq,area,estado"/>
		
		
	</body>
</html>

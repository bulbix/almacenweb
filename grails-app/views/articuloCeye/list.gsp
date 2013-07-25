

<%@ page import="mx.gob.inr.ceye.ArticuloCeye" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'articuloCeye.label', default: 'ArticuloCeye')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	
	
	<body>
		
		<filterpane:includes />
		
		<a href="#list-articuloCeye" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>				
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				<li><a class="home" href="${createLink(action: 'list')}">
				<g:message code="default.refresh.label"/></a>
				</li>
			</ul>
		</div>
		<div id="list-articuloCeye" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="id" 
						title="Clave" />
					
						<g:sortableColumn property="desArticulo" 
						title="Descripcion" />
											
						<g:sortableColumn property="unidad" 
						title="Unidad" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${articuloCeyeInstanceList}" status="i" var="articuloCeyeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="create" id="${articuloCeyeInstance.id}">
						${fieldValue(bean: articuloCeyeInstance, field: "id")}</g:link></td>
					
						<td>${fieldValue(bean: articuloCeyeInstance, field: "desArticulo")}</td>
					
						<td>${fieldValue(bean: articuloCeyeInstance, field: "unidad")}</td>					
					</tr>
				</g:each>
				</tbody>
			</table>
			
			<div class="paginateButtons">
			    <filterpane:paginate total="${articuloCeyeInstanceTotal}" domainBean="mx.gob.inr.ceye.ArticuloCeye"/>
			    <filterpane:filterButton text="Busqueda" appliedText="Cambiar Filtro"/>
			    <filterpane:isNotFiltered>Lista Completa, No Filtrada!</filterpane:isNotFiltered>
			    <filterpane:isFiltered>Filtro Aplicado!</filterpane:isFiltered>
  			</div>	
		
			
			<filterpane:filterPane domain="mx.gob.inr.ceye.ArticuloCeye"			
			filterProperties="desArticulo,unidad" additionalProperties="id"/>
			
		</div>
	</body>
</html>

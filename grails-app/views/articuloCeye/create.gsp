<%@ page import="mx.gob.inr.ceye.ArticuloCeye" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'articuloCeye.label', default: 'ArticuloCeye')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#create-articuloCeye" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>				
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>				
			</ul>
		</div>
		<div id="create-articuloCeye" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${articuloCeyeInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${articuloCeyeInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>

		<g:form id="captura" method="post">
		
			<input type="hidden" value="${estatus}">

			<table>
				<tr>
					<td colspan="4"><label>Clave</label> <g:field type="number" name="clave"
							value="${articuloCeyeInstance?.id}" size="5" readonly="${sololectura}" />
					
					<g:actionSubmit value="Cargar" action="load" disabled="${sololectura}" />
					<g:actionSubmit value="Guardar"  action="save"/>
				</tr>
				<tr>
					<td colspan="2"><label>Descripcion Almacen</label> <g:textArea
							name="descripcionAlmacen"
							value="${articuloCeyeInstance?.descripcionAlmacen?.trim()}" readonly="${sololectura}"/></td>				
					<td colspan="2"><label>Descripcion Ceye</label> <g:textArea
							name="descripcionCeye"
							value="${articuloCeyeInstance?.desArticulo?.trim()}" /></td>
				</tr>
				<tr>
					<td colspan="2"><label>Unidad Almacen</label> <g:textField
							name="unidadAlmacen" value="${articuloCeyeInstance?.unidadAlmacen}"
							size="20" disabled="${sololectura}" /></td>				
					<td colspan="2"><label>Unidad Ceye</label> <g:textField name="unidadCeye"
							value="${articuloCeyeInstance?.unidad}" size="20" /></td>
				</tr>	
				
				<tr>				
					<td colspan="2"><label>Cantidad Almacen</label> <g:textField
							name="cantidadAlmacen" value="${articuloCeyeInstance?.cantidadAlmacen}"
							size="5" disabled="${sololectura}" /></td>				
					<td colspan="2"><label>Cantidad Ceye</label> <g:textField
							name="cantidadCeye" value="${articuloCeyeInstance?.cantidadCeye}"
							size="5" disabled="${sololectura}" /></td>
				</tr>
			</table>
			
			

		</g:form>
	</div>

</body>
</html>


<%@ page import="mx.gob.inr.ceye.PaqueteTipoQuirurgicoCeye" %>

<g:javascript src="validaciones.js"/>
<g:javascript src="comunes.js"/>
<g:javascript src="entrada.js" />

<a href="#create-entrada" class="skip" tabindex="-1"><g:message
		code="default.link.skip.label" default="Skip to content&hellip;" /></a>

<div class="nav" role="navigation">
	<ul>
		<li><g:link class="list" action="list">
				<g:message code="default.list.label" args="[entityName]" />
			</g:link></li>

		<li><a href="${createLink(action: 'create')}">Nuevo</a></li>		
		
		<g:if test="${entradaInstance?.estado != 'C'}">
			<g:if test="${entradaInstance?.id != null}">
				<li><input type="button" id="actualizar" value="Actualizar" />
				</li>

				<li><input type="button" id="cancelar" value="Cancelar" /></li>
			</g:if>
		</g:if>
		
		
	</ul>
</div>

<div id="create-entrada" class="content scaffold-create" role="main">
	<h1>
		<g:message code="default.create.label" args="[entityName]" />
	</h1>

	<g:if test="${flash.message}">
		<div class="message" role="status">
			${flash.message}
		</div>
	</g:if>

	<g:hasErrors bean="${entradaInstance}">
		<ul class="errors" role="alert">
			<g:eachError bean="${entradaInstance}" var="error">
				<li
					<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
						error="${error}" /></li>
			</g:eachError>
		</ul>
	</g:hasErrors>

	<div id="mensaje"></div>


	<form id="formPadre">

		<input type="hidden" name="idSalAlma" id="idSalAlma" />		
		<input type="hidden" name="idPadre" id="idPadre" value="${entradaInstance?.id}" />

		<table>
			<tr>
				
				<td><label for="fecha">Fecha</label>
				 <g:textField name="fecha" 
				 value="${entradaInstance?.fecha?.format('dd/MM/yyy')}" size="8"  />
				</td>
				
				<td><label for="folio">Folio</label>
				<g:textField name="folio" value="${entradaInstance?.folio}" size="5" /></td>
				
				<g:if test="${entradaInstance.almacen == 'F'}">
				
					<td>
						<label for="folioAlmacen">Folio Almacen</label> 
						<g:textField name="folioAlmacen" value="${entradaInstance?.folioAlmacen}" size="5" />
						<input type="button" id="guardar" style="display:none" value="Guardar" />
					</td>				
				</g:if>
				
				<td><label for="remision">Remision</label> 
					<g:textField name="remision" size="5" value="${entradaInstance?.numeroFactura}" />
				</td>
			</tr>
		</table>
			
			
		<g:if test="${entradaInstance?.almacen != 'F'}" >		
			<table>		
				<tr>
					<td >
						<label for="areaauto">Area</label>
						<g:textField name="areaauto" size="50" value="${entradaInstance?.area}" />
						<input type="hidden" name="cveArea" id="cveArea" value="${entradaInstance?.area?.id}" />
					</td>				
					<td >
						<label for="paqueteq">Paquete Quirurgico</label>
						<g:select name="paqueteq" from="${PaqueteTipoQuirurgicoCeye.list()}" optionKey="tipo"
						optionValue="descripcion" value="${entradaInstance?.paqueteq}" 
						noSelection="${['':'SELECCIONE PAQUETE']}" />
						<input type="button" id="guardarPaquete" style="display:none" value="Guardar" />
					</td>
				</tr>
			</table>				
		</g:if>
		
		<table>
			<tr>
				<td><label for="registra">Registra</label> <g:select
						name="registra" from="${usuariosList}" optionKey="id"
						optionValue="nombre" value="${entradaInstance?.usuario?.id}" 
						noSelection="${['':'SELECCIONE REGISTRA']}" /></td>			
				<td><label for="supervisa">Supervisa</label> <g:select
						name="supervisa" from="${usuariosList}" optionKey="id"
						optionValue="nombre" value="${entradaInstance?.supervisor?.id}" 
						noSelection="${['':'SELECCIONE SUPERVISA']}" /></td>
			</tr>
			<tr>
				<td><label for="recibe">Recibe</label> <g:select name="recibe"
						from="${usuariosList}" optionKey="id" optionValue="nombre" 
						value="${entradaInstance?.recibio?.id}" 
						noSelection="${['':'SELECCIONE RECIBE']}" /></td>
			
				<g:if test="${entradaInstance?.almacen == 'F'}">
					<td><label for="devolucion"> Es Devolucion?</label> <g:checkBox
						name="devolucion" value="${entradaInstance.devolucion == '1'}" /></td>
				</g:if>
				<g:else>
					<td></td>
				</g:else>
			</tr>
		</table>
	

		<table id="tblBusqueda" class="busqueda">
			<tr>
				<td colspan="6"><label for="artauto">Descripción
						Articulo</label> <g:textField name="artauto" style="width: 700px;" /> <input
					type="hidden" name="desArticulo" id="desArticulo" /></td>
			</tr>
			<tr>
				<td><label for="insumo">Clave</label> <g:textField
							name="insumo" size="3" /></td>
				
				<td><label for="unidad">Unidad</label> <g:textField
						name="unidad" readonly="true" /></td>
						
				<td><label for="cantidad">Cantidad</label> <g:textField
							name="cantidad" size="3" /></td>
							
				<td><label for="precio">Precio U.</label> <g:textField
				name="precio" size="8" readonly="${entradaInstance?.almacen != 'F'}" /></td>
				
				<g:if test="${entradaInstance?.almacen == 'F'}">					
					<td><label for="nolote">Lote</label> <g:textField
						name="noLote" size="5" /></td>
	
					<td><label for="nolote">F. Caducidad</label> <g:textField
						name="fechaCaducidad" size="8" /></td>				
				</g:if>
				<g:else>
					<td><label for="convertido">Convertido</label> <g:textField
							name="convertido" size="5" /></td>
					<td></td>
				</g:else>
			</tr>
		</table>
	
	</form>
	
	<g:if test="${entradaInstance?.almacen != 'F'}">
		<table class="busqueda">
			<tr>
				<td><label for="ualmacen">U. Almacen</label> <g:textField
						name="ualmacen" readonly="true" /></td>

				<td><label for="calmacen">C. Almacen</label> <g:textField
						name="calmacen" size="5" readonly="true" /></td>

				<td><label for="uceye">U. Ceye</label> <g:textField
						name="uceye" readonly="true" /></td>

				<td><label for="cceye">C. Ceye</label> <g:textField
						name="cceye" size="5" readonly="true" /></td>

				<td><label for="cociente">Cociente</label> <g:textField
						name="cociente" size="5" readonly="true" /></td>
			</tr>
		</table>
	</g:if>
		
		
	

	<table class="busqueda">
		<thead>
			<tr>
				<td><label>Clave</label></td>
				<td><label>Descripcion</label></td>
				<td><label>Unidad</label></td>
				<td><label>Cantidad</label></td>
				<td><label>Precio U.</label></td>
				<td><label>Lote</label></td>
				<td><label>F. Caducidad</label></td>

			</tr>

		</thead>

		<tbody>
			<tr>
				<td><label id="clavelast"></label></td>
				<td><label id="deslast"></label></td>
				<td><label id="unidadlast"></label></td>
				<td><label id="cantidadlast"></label></td>
				<td><label id="preciolast"></label></td>
				<td><label id="lotelast"></label></td>
				<td><label id="caducidadlast"></label></td>

			</tr>

		</tbody>
	</table>

	<input type="button" id="btnActualizar" value="Actualizar" class="busqueda" />
	<input type="button" id="btnBorrar" value="Borrar" class="busqueda" />

	<form id="formDetalle">
		<div class="list">
			<table id="detalle"></table>
			<div id="pager"></div>
		</div>
	</form>
</div>
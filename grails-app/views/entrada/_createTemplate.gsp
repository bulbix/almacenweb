
<%@ page import="mx.gob.inr.ceye.PaqueteTipoQuirurgicoCeye" %>

<g:javascript src="comunes.js"/>
<g:javascript src="entrada.js" />

<a href="#create-entrada" class="skip" tabindex="-1"><g:message
		code="default.link.skip.label" default="Skip to content&hellip;" /></a>

<div class="nav" role="navigation">
	<ul>
		<li><g:link class="list" action="list">
				<g:message code="default.list.label" args="[entityName]" />
			</g:link></li>

		<li><input type="button" id="nuevo" value="Nuevo" /></li>

		<li><input type="button" id="guardar" value="Guardar" /></li>
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


	<form id="formEntrada">

		<input type="hidden" name="idSalAlma" id="idSalAlma" />		
		<input type="hidden" name="idEntrada" id="idEntrada" value="${entradaInstance?.id}" />

		<table>
			<tr>
				
				<td><label for="fechaEntrada">Fecha</label>
				 <g:textField name="fechaEntrada" 
				 value="${entradaInstance?.fechaEntrada?.format('dd/MM/yyy')}" size="8"  />
				</td>
				
				<td><label for="folioEntrada">Folio</label>
				<g:field min="1" max="10000" name="folioEntrada" type="number"
						value="${entradaInstance?.numeroEntrada}" size="5" /></td>
				
				<td><label for="folioAlmacen">Folio Almacen</label> <g:field
						min="1" max="10000" name="folioAlmacen" type="number" value="${entradaInstance?.folioAlmacen}" 
							size="5" />
				</td>
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
						noSelection="${['null':'SELECCIONE PAQUETE']}" />
					</td>
				</tr>
			</table>
				
		</g:if>
		<table>
			<tr>
				<td><label for="registra">Registra</label> <g:select
						name="registra" from="${usuariosList}" optionKey="id"
						optionValue="nombre" value="${entradaInstance?.usuario?.id}" 
						noSelection="${['null':'SELECCIONE REGISTRA']}" /></td>			
				<td><label for="supervisa">Supervisa</label> <g:select
						name="supervisa" from="${usuariosList}" optionKey="id"
						optionValue="nombre" value="${entradaInstance?.supervisor?.id}" 
						noSelection="${['null':'SELECCIONE SUPERVISA']}" /></td>
			</tr>
			<tr>
				<td><label for="recibe">Recibe</label> <g:select name="recibe"
						from="${usuariosList}" optionKey="id" optionValue="nombre" 
						value="${entradaInstance?.recibio?.id}" 
						noSelection="${['null':'SELECCIONE RECIBE']}" /></td>
			
				<g:if test="${entradaInstance?.almacen == 'F'}">
					<td><label for="devolucion"> Es Devolucion?</label> <g:checkBox
						name="devolucion" value="${entradaInstance.devolucion == '1'}" /></td>
				</g:if>
				<g:else>
					<td></td>
				</g:else>
			</tr>
		</table>
	</form>

	<table id="tblBusqueda">
		<tr>
			<td colspan="6"><label for="artauto">Descripción
					Articulo</label> <g:textField name="artauto" style="width: 700px;" /> <input
				type="hidden" name="desArticulo" id="desArticulo" /></td>
		</tr>
		<tr>
			<td><label for="insumo">Insumo</label> <g:textField
						name="insumo" size="3" /></td>
			
			<td><label for="unidad">Unidad</label> <g:textField
					name="unidad" readonly="true" /></td>
					
			<td><label for="cantidad">Cantidad</label> <g:textField
						name="solicitado" name="cantidad" size="3" /></td>
						
			<td><label for="precio">Precio Unitario</label> <g:textField
			name="precio" size="5" readonly="${entradaInstance?.almacen != 'F'}" /></td>
			
			<g:if test="${entradaInstance?.almacen == 'F'}">					
				<td><label for="nolote">No Lote</label> <g:textField
					name="noLote" size="5" /></td>

				<td><label for="nolote">Fecha Caducidad</label> <g:textField
					name="fechaCaducidad" size="8" /></td>				
			</g:if>
			<g:else>
				<td><label for="convertido">Convertido</label> <g:textField
						name="convertido" size="5" /></td>
				<td></td>
			</g:else>
		</tr>
	</table>
	
	<g:if test="${entradaInstance?.almacen != 'F'}">
			<table>
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
	
	<table>


		<thead>
			<tr>
				<td><label>Clave</label></td>
				<td><label>Descripcion</label></td>
				<td><label>Unidad</label></td>
				<td><label>Cantidad</label></td>
				<td><label>Precio U.</label></td>
				<td><label>Lote</label></td>
				<td><label>Fecha Caducidad</label></td>

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

	<input type="button" id="btnActualizar" value="Actualizar" />
	<input type="button" id="btnBorrar" value="Borrar" />

	<form id="formEntradaDetalle">
		<div class="list">
			<table id="entradadetalle"></table>
			<div id="pager"></div>
		</div>
	</form>
</div>
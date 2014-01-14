
<%@ page import="mx.gob.inr.ceye.PaqueteTipoQuirurgicoCeye" %>

<g:javascript src="entrada.js" />

<a href="#create-entrada" class="skip" tabindex="-1"><g:message
		code="default.link.skip.label" default="Skip to content&hellip;" /></a>

<div class="nav" role="navigation">
	<ul>
		<li><g:link class="list" action="list">
				<g:message code="default.list.label" args="[entityName]" />
			</g:link>
		</li>
		
		<sec:noAccess expression="hasRole('ROLE_FARMACIA_LECTURA')">
			<li><a href="${createLink(action: 'create')}" class="nuevo">Nuevo</a></li>
			<li><a href="#" id="actualizar" style="display:none" class="actualizar botonOperacion">Actualizar</a></li>	
			<li><a href="#" id="cancelar" style="display:none" class="cancelar botonOperacion">Cancelar</a></li>			
		</sec:noAccess>
		
		<sec:access expression="hasRole('ROLE_CEYE')">
			<li><a href="/almacenWeb/articuloCeye" class="catalogo" 
			onclick="window.open(this.href, 'child', 'scrollbars,width=900,height=600'); return false">Catalogo Articulo</a>
			</li>
		</sec:access>
		
	</ul>
</div>

<div id="imprimir" style="display:none" class="botonOperacion" >
		<g:jasperReport jasper="reportValeEntrada" format="PDF,XLSX" delimiter=" " name="ValeEntrada" 
			controller="${controllerName}" action="reporte">		
			<input type="hidden" name="id" value="${entradaInstance?.id}" />
			<input type="hidden" name="almacen" value="${entradaInstance?.almacen}" />			
		</g:jasperReport>
</div>


<div id="create-entrada" class="content scaffold-create" role="main">
	
	<h1>
		<g:almacenDescripcion code="default.create.entrada.label" almacen="${entradaInstance?.almacen}"/>		
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



	<g:if test="${existeCierre == true}">	
		<div id="mensaje" style="font-size:20px;color:red">
			Cierre existente no se pueden hacer cambios
		</div>
	</g:if>
	
	<g:if test="${entradaInstance?.estado == 'C'}">	
		<div id="mensaje" style="font-size:20px;color:red">
			Folio Cancelado
		</div>
	</g:if>
	
	<input type="hidden" id="isDueno" value="${isDueno}" />
	<input type="hidden" id="isAdmin" value="${isAdmin}" />
	<input type="hidden" id="existeCierre" value="${existeCierre}" />
	<input type="hidden" id="estado" value="${entradaInstance?.estado}" />
	
	

	<form id="formPadre">

		<input type="hidden" name="idSalAlma" id="idSalAlma" />		
		<input type="hidden" name="idPadre" id="idPadre" value="${entradaInstance?.id}" />

		<table>
			<tr>
				
				<td><label for="fecha">Fecha</label>
				 <g:textField name="fecha" 
				 value="${entradaInstance?.fecha?.format('dd/MM/yyyy')}" size="8" class="cabecera"  />
				</td>
				
				<td><label for="folio">Folio</label>
				<g:textField name="folio" value="${entradaInstance?.folio}" size="5" /></td>
				
				<g:if test="${entradaInstance.almacen == 'F'}">
				
					<td>
						<label for="folioAlmacen">Folio Almacen</label> 
						<g:textField name="folioAlmacen" value="${entradaInstance?.folioAlmacen}" size="5" />
						<input type="button" id="guardarAlmacen" style="display:none" value="Guardar" />
					</td>				
				</g:if>
				
				
				<g:if test="${entradaInstance?.almacen == 'F'}" >				
					<td><label for="remision">Remision</label> 
						<g:textField name="remision" size="5" value="${entradaInstance?.numeroFactura}" />
					</td>
				</g:if>
				<g:else>
					<td><label for="tipoVale">Tipo Vale</label>								
						<g:select name="tipoVale" from="${['instituto', 'paciente']}" 
							value="${entradaInstance?.tipoVale}"  />
					</td>					
				</g:else>
			</tr>
		</table>
			
			
		<g:if test="${entradaInstance?.almacen != 'F'}" >		
			<table>		
				<tr>
					<td >
						<label for="areaauto">Area</label>
						<g:textField name="areaauto" size="90" value="${entradaInstance?.area}" />
						<input type="hidden" name="cveArea" id="cveArea" value="${entradaInstance?.area?.id}" />
					</td>
				</tr>
				<tr>					
					<td>
						<label for="pacienteauto">Paciente</label>
						<g:textField name="pacienteauto" size="90" value="${entradaInstance?.paciente}" />
						<input type="hidden" name="idPaciente" id="idPaciente" value="${entradaInstance?.paciente?.id}" />
					</td>								
				<tr>
					<td>
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
				<td>
				
				<g:if test="${entradaInstance?.almacen == 'F'}">				
						<label for="supervisa">Supervisa</label> <g:select
						name="supervisa" from="${usuariosList}" optionKey="id"
						optionValue="nombre" value="${entradaInstance?.supervisor?.id}" 
						noSelection="${['':'SELECCIONE SUPERVISA']}" class="cabecera" />
				</g:if>
				<g:else>
					<label for="supervisa">Solicita</label> <g:select
						name="supervisa" from="${usuariosList}" optionKey="id"
						optionValue="nombre" value="${entradaInstance?.supervisor?.id}" 
						noSelection="${['':'SELECCIONE SOLICITA']}" class="cabecera" />
				</g:else>
				
				</td>			
				<td><label for="recibe">Recibe</label> <g:select name="recibe"
						from="${usuariosList}" optionKey="id" optionValue="nombre" 
						value="${entradaInstance?.recibio?.id}" 
						noSelection="${['':'SELECCIONE RECIBE']}" class="cabecera" /></td>			
				<g:if test="${entradaInstance?.almacen == 'F'}">
					<td><label for="devolucion">Devolucion?</label> <g:checkBox
						name="devolucion" value="${entradaInstance.devolucion == '1'}" /></td>
				</g:if>
				<g:else>
					<td></td>
				</g:else>
			</tr>
		</table>
	

		<table id="tblBusqueda" class="busqueda">
			<tr>
				<td colspan="7"><label for="artauto">Descripción
						Articulo</label> <g:textField name="artauto" style="width: 700px;" />
				</td>
			</tr>
			<tr>
				<td><label for="insumo">Clave</label> <g:textField
							name="insumo" size="3" /></td>
				
				<td><label for="unidad">Unidad</label> <g:textField
						name="unidad" readonly="true" /></td>

				<td>						
					<g:if test="${entradaInstance?.almacen != 'F'}">
						<label for="solicitado">Solicitado</label> <g:textField
								name="solicitado" size="3" />
					</g:if>
				</td>
						
				<td><label for="cantidad">Cantidad</label> <g:textField
							name="cantidad" size="3" /></td>
							
				<td><label for="precio">Precio U.</label> <g:textField
				name="precio" size="10" readonly="${entradaInstance?.almacen != 'F'}" /></td>
				
				<g:if test="${entradaInstance?.almacen == 'F'}">					
					<td><label for="nolote">Lote</label> <g:textField
						name="noLote" size="5" /></td>
	
					<td><label for="nolote">F. Caducidad</label> <g:textField
						name="fechaCaducidad" size="8" /></td>				
				</g:if>
				<g:else>
					
					<td><label for="convertidoSolicitado">Solicitado</label> <g:textField
							name="convertidoSolicitado" size="5" /></td>
					
					<td><label for="convertido">Convertido</label> <g:textField
							name="convertido" size="5" /></td>
					
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
		<caption>Ultima clave capturada</caption>
		<thead>
			<tr>
				<th><label>Clave</label></th>
				<th><label>Descripcion</label></th>
				<th><label>Unidad</label></th>
				
				<g:if test="${entradaInstance.almacen != 'F'}">
					<th><label>Solicitado</label></th>
				</g:if>
				
				<th><label>Cantidad</label></th>
				<th><label>Precio U.</label></th>
				
				<g:if test="${entradaInstance.almacen == 'F'}">
					<th><label>Lote</label></th>
					<th><label>F. Caducidad</label></th>
				</g:if>

			</tr>

		</thead>

		<tbody>
			<tr>
				<td><label id="clavelast"></label></td>
				<td><label id="deslast"></label></td>
				<td><label id="unidadlast"></label></td>
				
				<g:if test="${entradaInstance.almacen != 'F'}">
					<td><label id="solicitadolast"></label></td>
				</g:if>
				
				<td><label id="cantidadlast"></label></td>
				<td><label id="preciolast"></label></td>
				<g:if test="${entradaInstance.almacen == 'F'}">
					<td><label id="lotelast"></label></td>
					<td><label id="caducidadlast"></label></td>
				</g:if>

			</tr>

		</tbody>
	</table>

	<g:if test="${existeCierre == false && entradaInstance?.estado == 'A'  }">	
		<input type="button" id="btnActualizar" value="Actualizar Clave" class="busqueda" />
		<input type="button" id="btnBorrar" value="Borrar Clave" class="busqueda" />
	</g:if>

	<form id="formDetalle">
		<div class="list">
			<table id="detalle"></table>
			<div id="pager"></div>
		</div>
	</form>
</div>
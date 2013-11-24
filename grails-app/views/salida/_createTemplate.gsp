
<%@ page import="mx.gob.inr.ceye.PaqueteTipoQuirurgicoCeye" %>

<g:javascript src="salida.js" />

<a href="#create-salida" class="skip" tabindex="-1"><g:message
		code="default.link.skip.label" default="Skip to content&hellip;" /></a>

<div class="nav" role="navigation">
	<ul>
		<li><g:link class="list" action="list">
				<g:message code="default.list.label" args="[entityName]" />
		</g:link></li>
		
		<sec:noAccess expression="hasRole('ROLE_FARMACIA_LECTURA')">
			<li><a href="${createLink(action: 'create')}" class="nuevo">Nuevo</a></li>
			<li><a href="#" id="actualizar" style="display:none" class="actualizar botonOperacion">Actualizar</a></li>	
			<li><a href="#" id="cancelar" style="display:none" class="cancelar botonOperacion">Cancelar</a></li>
		</sec:noAccess>
	</ul>
</div>


<div id="imprimir" style="display:none" class="botonOperacion" >
		<g:jasperReport jasper="reportValeSalida" format="PDF,XLSX" delimiter=" " name="ValeSalida" 
			controller="${controllerName}" action="reporte">		
			<input type="hidden" name="id" value="${salidaInstance?.id}" />
			<input type="hidden" name="almacen" value="${salidaInstance?.almacen}" />			
		</g:jasperReport>
</div>

<div id="create-salida" class="content scaffold-create" role="main">
	<h1>		
		<g:almacenDescripcion code="default.create.salida.label" almacen="${salidaInstance?.almacen}"/>	
	</h1>
	<g:if test="${flash.message}">
		<div class="message" role="status">
			${flash.message}
		</div>
	</g:if>
	<g:hasErrors bean="${salidaInstance}">
		<ul class="errors" role="alert">
			<g:eachError bean="${salidaInstance}" var="error">
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
	
	
	<g:if test="${salidaInstance?.estado == 'C'}">	
		<div id="mensaje" style="font-size:20px;color:red">
			Folio Cancelado
		</div>
	</g:if>
	
	
	<input type="hidden" id="isDueno" value="${isDueno}" />
	<input type="hidden" id="isAdmin" value="${isAdmin}" />
	<input type="hidden" id="existeCierre" value="${existeCierre}" />
	<input type="hidden" id="estado" value="${salidaInstance?.estado}" />

	<form id="formPadre" action="">

		<input type="hidden" value="${salidaInstance?.id}" name="idPadre" id="idPadre" />

		<table>
			<tr>
				<td><label for="fecha">Fecha</label> <g:textField
						name="fecha" class="cabecera"
						value="${salidaInstance?.fecha.format('dd/MM/yyyy')}"
						size="9" /></td>
			
				<td><label for="folio">Folio</label> <g:textField
						name="folio" 
						value="${salidaInstance?.folio}" size="5" /></td>
				
				<td>		
					<g:if test="${salidaInstance?.almacen != 'F'}">
						<label for="nosala">No. Sala</label> <g:textField
								 name="nosala" 	value="${salidaInstance?.nosala}" size="5" />
					</g:if>
				</td>
			</tr>
		</table>
		
		<table>
			<tr>
				<td><label for="areaauto">Area</label> <g:textField
						name="areaauto" style="width: 700px;" 
						value="${salidaInstance?.area}" /> <input type="hidden" class="cabecera" 
					name="cveArea" id="cveArea" value="${salidaInstance?.area?.id}" />
				</td>
			</tr>
			<tr>
				<td><label for="pacienteauto">Paciente</label> <g:textField
						name="pacienteauto" style="width: 700px;"
						value="${salidaInstance?.paciente}" /> <input type="hidden"
					name="idPaciente" id="idPaciente"
					value="${salidaInstance?.paciente?.id}" /></td>
			<tr>
			
			<g:if test="${salidaInstance?.almacen != 'F'}">
				<tr>
					<td><label for="procedimientoauto">Procedimiento</label> <g:textField
							name="procedimientoauto" style="width: 700px;"
							value="${salidaInstance?.diagnostico}" /> <input type="hidden"
						name="idProcedimiento" id="idProcedimiento"
						value="${salidaInstance?.diagnostico?.id}" /></td>
				<tr>
			
			</g:if>	
		</table>		
			
		<table>
			<tr>
				<td>				
					<g:if test="${salidaInstance?.almacen == 'F'}">	
						<label for="recibe">Recibe</label> 
					</g:if>
					<g:else>
						<label for="recibe">Solicita</label>
					</g:else>
					
					<g:textField name="recibeauto" value="${salidaInstance?.recibio}" class="cabecera" size="50" />				
				</td>

				<td>
					<label for="autoriza">Autoriza</label>
					<g:textField name="autorizaauto" value="${salidaInstance?.jefeServicio}" 
					class="cabecera" size="50" />
				</td>
			</tr>			
			
			<tr>
				<td><label for="entrega">Entrega</label><g:select
						name="entrega" from="${usuariosList}" optionKey="id" class="cabecera" 
						optionValue="nombre" value="${salidaInstance?.entrego?.id}" 
						noSelection="${['':'SELECCIONE ENTREGA']}" />
				</td>
				
				<g:if test="${salidaInstance?.almacen != 'F'}" >
					<td>
						<label for="paqueteq">Paquete Quirurgico</label>
						<g:select name="paqueteq" from="${PaqueteTipoQuirurgicoCeye.list()}" optionKey="tipo"
						optionValue="descripcion" value="${salidaInstance?.paqueteq}" 
						noSelection="${['':'SELECCIONE PAQUETE']}" />
						<input type="button" id="guardarPaquete" style="display:none" value="Guardar" />
					</td>				
				</g:if>				
			</tr>

		</table>
		
		<g:if test="${salidaInstance?.almacen == 'F'}" >
			<table>
				<tr>
					<td>
						<label for="horaEntrega">Hora Entrega</label>
						<g:textField name="horaEntrega" value="${salidaInstance?.horaEntrega?.format('HH:mm')}" 
						class="cabecera" size="5" readonly="true" />
					</td>
					<td>
						<label for="tipoSolicitud">Tipo Solicitud</label>								
						<g:select name="tipoSolicitud" from="${['paciente', 'stock']}" 
							value="${salidaInstance?.tipoSolicitud}"  />
						
					</td>
				</tr>
			</table>		
		
		
		</g:if>


		<table id="tblBusqueda" class="busqueda">
			<tr>
				<td colspan="6"><label for="artauto">Descripción
						Articulo</label> <g:textField name="artauto" style="width: 700px;" />
				</td>
			</tr>
			<tr>
				<td><label for="insumo">Clave</label> <g:textField
						name="insumo" size="3" /></td>
				<td><label for="unidad">Unidad</label> <g:textField
						name="unidad" readonly="true" size="15" /></td>
				<td><label for="costo">Costo</label> <g:textField
						name="costo" readonly="true" size="10" />
				</td>			
				<td><label for="disponible">Disponible</label> <g:textField
						name="disponible" readonly="true" size="3" /></td>
				<td><label for="solicitado">Solicitado</label> <g:textField
						name="solicitado" name="solicitado" size="3" /></td>
				<td><label for="surtido">Surtido</label> <g:textField
						name="surtido" name="surtido" size="3" /></td>
			</tr>
		</table>
	</form>


	<table class="busqueda">
		<caption>Ultima clave capturada</caption>
		<thead>
			<tr>
				<th><label>Clave</label></th>

				<th><label>Descripcion</label></th>

				<th><label>Unidad</label></th>
				<th><label>Costo</label></th>
				<th><label>Disponible</label></th>
				<th><label>Solicitado</label></th>
				<th><label>Surtido</label></th>

			</tr>

		</thead>

		<tbody>
			<tr>
				<td><label id="clavelast"></label></td>

				<td><label id="deslast"></label></td>

				<td><label id="unidadlast"></label></td>
				<td><label id="costolast"></label></td>
				<td><label id="disponiblelast"></label></td>
				<td><label id="solicitadolast"></label></td>
				<td><label id="surtidolast"></label></td>

			</tr>

		</tbody>
	</table>
	
	<g:if test="${existeCierre == false && salidaInstance?.estado == 'A'}">
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
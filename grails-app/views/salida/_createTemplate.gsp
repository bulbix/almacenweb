<g:javascript src="comunes.js"/>
<g:javascript src="salida.js" />

<a href="#create-salida" class="skip" tabindex="-1"><g:message
		code="default.link.skip.label" default="Skip to content&hellip;" /></a>

<div class="nav" role="navigation">
	<ul>
		<li><g:link class="list" action="list">
				<g:message code="default.list.label" args="[entityName]" />
			</g:link></li>
		<li><a href="${createLink(action: 'create')}">Nuevo</a></li>

		<g:if test="${salidaInstance?.estadoSalida != 'C'}">
			<g:if test="${salidaInstance?.id != null}">
				<li><input type="button" id="actualizar" value="Actualizar" />
				</li>

				<li><input type="button" id="cancelar" value="Cancelar" /></li>
			</g:if>
		</g:if>

		<li><input type="button" id="imprimir" value="Imprimir" /></li>

	</ul>
</div>

<div id="create-salida" class="content scaffold-create" role="main">
	<h1>
		<g:message code="default.create.label" args="[entityName]" />
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

	<div id="mensaje"></div>

	<form id="formSalida">

		<input type="hidden" value="${salidaInstance?.id}" name="idSalida" id="idSalida" />

		<table>
			<tr>
				<td><label for="folioSalida">Folio Salida</label> <g:field
						min="1" max="10000" name="folioSalida" type="number"
						value="${salidaInstance?.numeroSalida}" size="5" /></td>

				<td><label for="fechaSalida">Fecha Salida</label> <g:textField
						name="fechaSalida"
						value="${salidaInstance?.fechaSalida.format('dd/MM/yyy')}"
						size="8" /></td>
			</tr>

			<tr>
				<td colspan="3"><label for="areaauto">Area</label> <g:textField
						name="areaauto" style="width: 700px;"
						value="${salidaInstance?.area}" /> <input type="hidden"
					name="cveArea" id="cveArea" value="${salidaInstance?.area?.id}" />
				</td>
			<tr>
			<tr>
				<td colspan="3"><label for="pacienteauto">Paciente</label> <g:textField
						name="pacienteauto" style="width: 700px;"
						value="${salidaInstance?.paciente}" /> <input type="hidden"
					name="idPaciente" id="idPaciente"
					value="${salidaInstance?.paciente?.id}" /></td>
			<tr>
			<tr>
				<td><label for="entrega">Entrega</label> <g:select
						name="entrega" from="${usuariosList}" optionKey="id"
						optionValue="nombre" value="${salidaInstance?.entrego?.id}" /></td>

				<td><label for="recibe">Recibe</label> <g:textField
						name="recibeauto" value="${salidaInstance?.recibio}" /></td>

				<td><label for="autoriza">Autoriza</label> <g:textField
						name="autorizaauto" value="${salidaInstance?.jefeServicio}" /></td>
			</tr>

		</table>


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
						name="unidad" readonly="true" size="15" /></td>
				<td><label for="costo">Costo</label> <g:textField
						name="costoDisplay" readonly="true" size="5" />
						<input type="hidden"
					name="costo" id="costo" />
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


	<table>


		<thead>
			<tr>
				<td><label>Clave</label></td>

				<td><label>Descripcion</label></td>

				<td><label>Unidad</label></td>
				<td><label>Costo</label></td>
				<td><label>Disponible</label></td>
				<td><label>Solicitado</label></td>
				<td><label>Surtido</label></td>

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

	<input type="button" id="btnActualizar" value="Actualizar" />
	<input type="button" id="btnBorrar" value="Borrar" />

	<form id="formDetalle">
		<div class="list">
			<table id="salidadetalle"></table>
			<div id="pager"></div>
		</div>
	</form>


</div>
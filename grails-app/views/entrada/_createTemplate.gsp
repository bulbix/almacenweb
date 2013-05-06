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

		<table>
			<tr>
				<td><label for="fechaEntrada">Fecha Entrada</label> <g:textField
						name="fechaEntrada" value="${fechaActual}" /></td>
				<td><label for="folioEntrada">Folio Entrada</label> <g:field
						min="1" max="10000" name="folioEntrada" type="number"
						value="${consecutivoNumeroEntrada}" /></td>
			</tr>

			<tr>
				<td><label for="folioAlmacen">Folio Almacen</label> <g:field
						min="1" max="10000" name="folioAlmacen" type="number" value="" />
				</td>
				<td><label for="remision">Remision</label> <g:textField
						name="remision" size="5" value="" /></td>
			</tr>

			<tr>
				<td><label for="registra">Registra</label> <g:select
						name="registra" from="${usuariosList}" optionKey="id"
						optionValue="nombre" /></td>
				<td><label for="estado">Estado</label> <g:select name="estado"
						from="${['ACTIVO','CANCELADO']}" /></td>
			</tr>

			<tr>
				<td><label for="supervisa">Supervisa</label> <g:select
						name="supervisa" from="${usuariosList}" optionKey="id"
						optionValue="nombre" /></td>
				<td><label for="recibe">Recibe</label> <g:select name="recibe"
						from="${usuariosList}" optionKey="id" optionValue="nombre" /></td>
			</tr>
			<tr>
				<td><label for="devolucion"> Es Devolucion?</label> <g:checkBox
						name="devolucion" value="" /></td>
			</tr>
		</table>
	</form>

	<table id="tblBusqueda">
		<tr>
			<td colspan="2"><label for="artauto">Descripción
					Articulo</label> <g:textField name="artauto" style="width: 700px;" /> <input
				type="hidden" name="desArticulo" id="desArticulo" /></td>
		</tr>
		<tr>
			<td><label for="insumo">Insumo</label> <g:field min="1"
					max="10000" name="insumo" type="number" /></td>
			<td><label for="unidad">Unidad</label> <g:textField
					name="unidad" readonly="true" /></td>

		</tr>
		<tr>
			<td><label for="cantidad">Cantidad</label> <g:field min="1"
					max="10000" name="cantidad" type="number" /></td>
			<td><label for="precio">Precio Unitario</label> <g:textField
					name="precio" name="precio" /></td>
		</tr>

		<tr>
			<td><label for="nolote">No Lote</label> <g:textField
					name="noLote" /></td>

			<td><label for="nolote">Fecha Caducidad</label> <g:textField
					name="fechaCaducidad" /></td>
		</tr>

	</table>

	<input type="button" id="bDelete" value="Eliminar Renglon" />

	<form id="formEntradaDetalle">
		<div class="list">
			<table id="entradadetalle"></table>
			<div id="pager"></div>
		</div>
	</form>
</div>
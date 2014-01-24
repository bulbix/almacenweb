<g:javascript src="reporte.js" />

<div id="create-reporte" class="content scaffold-create" role="main">
	
	<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
	</g:if>
	
	
	<h1>
		<g:almacenDescripcion code="default.reporte.label" almacen="${almacen}"/>		
	</h1>
	
	
	<h1>${reportDisplay}</h1>
	
	<g:form controller="${controllerName}">		
		<table>	
			<tr>
				<td>
					<label for="fechaInicial">Fecha Inicial</label>
					<g:textField name="fechaInicial" value="${fechaInicial.format('dd/MM/yyyy')}" size="8"/>
				</td>
				
				<td>
					<label for="fechaFinal">Fecha Final</label>
					<g:textField name="fechaFinal" value="${fechaFinal.format('dd/MM/yyyy')}" size="8"/>
				</td>
			</tr>	
			
			
			<g:if test="${almacen != 'F'}">
						
				<tr>
					<td colspan="2">
						<label for="tipoVale">Tipo Vale</label>						
							<g:select name="tipoVale" from="${['todos','instituto', 'paciente', 'traslado']}"/>		
					</td>			
				</tr>
			
			</g:if>
			
			<tr>
				<td colspan="2">
					<label for="tipoImpresion">Tipo Impresion</label>						
						<g:select name="tipoImpresion" from="${['pdf','excel']}"/>		
				</td>			
			</tr>
							
		</table>
				
		<input type="hidden" name="almacen" value="${almacen}" />
		<input type="hidden" name="methodName" value="${methodName}" />
		<input type="hidden" name="reportDisplay" value="${reportDisplay}" />
		
		<g:actionSubmit value="Generar Reporte" action="reporteDynamic"/>
	
	</g:form>	
</div>

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
	
	<g:jasperReport jasper="${reportName}" format="PDF,XLSX" delimiter=" " name=" ${reportDisplay}" 
		controller="${controllerName}" action="reporte" id="formReporte">	
		<table>
		
		
			<g:if test="${!['reporteExistencia'].contains(methodName)}">
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
			</g:if>
			
			<g:if test="${!['reportePartida','reporteSurtimiento','reporteSala','reporteFoliosAlmacen',
							'reporteConcentradoValeEntrada'].contains(methodName)}">
				<tr>
					<td>
						<label for="claveInicial">Clave Inicial</label>
						<g:textField name="claveInicial" value="${claveInicial}" size="5" />
					</td>
					<td>
						<label for="claveFinal">Clave Final</label> 
						<g:textField name="claveFinal" value="${claveFinal}" size="5" />
					</td>
				</tr>
				
				<tr>
					<td colspan="2">
						<label for="partida">Partida</label>
						<g:select name="partida" from="${partidaList}" optionKey="partida"
						optionValue="desPart" noSelection="${['':'SELECCIONE PARTIDA']}" />			
					</td>			
				</tr>				
			</g:if>		
			
			
			<g:if test="${methodName in ['reporteConsumo','reporteProporcionado','reporteSala','reporteConcentradoValeEntrada']}">
				<tr>
					<td colspan="2">
						<label for="partida">Area</label>
						<g:select name="area" from="${areaList}" optionKey="id"
						optionValue="desArea" noSelection="${['':'SELECCIONE AREA']}" />			
					</td>			
				</tr>
			</g:if>
			
			<g:if test="${methodName in ['reporteConcentradoValeEntrada', 'reporteConsumo'] && almacen != 'F'}">
				<tr>
					<td colspan="2">
						<label for="tipoVale">Tipo Vale</label>						
						<g:select name="tipoVale" from="${['todos','instituto', 'paciente', 'traslado']}"/>		
					</td>			
				</tr>
			</g:if>
			
							
		</table>
		
		<input type="hidden" id="download_token_value_id" name="download_token_value_id"/>		
		<input type="hidden" name="almacen" value="${almacen}" />
		<input type="hidden" name="methodName" value="${methodName}" />
	
	</g:jasperReport>	
</div>

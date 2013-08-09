
<g:javascript src="validaciones.js"/>
<g:javascript src="comunes.js"/>
<g:javascript src="inventario.js" />

<form id="formPadre">
	<table>
		<tr>
			<td><label for="fechaCierre">Fecha Cierre</label> 
				<g:textField name="fechaCierre" size="8" 
				 value="${fechaCierre?.format('dd/MM/yyyy')}" readonly="true" />
			</td>
			
			<td><label for="marbeteInicial">Marbete Inicial</label> 
				<g:textField name="marbeteInicial" size="8"  value="1" />
			</td>			
		</tr>
		
		<tr>
			<td colspan="2"><input type="button" id="asignar" name="asignar" value="Asignar Marbetes" /></td>
		</tr>
	</table>
	
	
	<div id="mensaje" style="font-size:18px;color:red">
	</div>
	
	
</form>
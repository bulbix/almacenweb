
<a href="#list-entrada" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>				
				<sec:noAccess expression="hasRole('ROLE_FARMACIA_LECTURA')">
					<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				</sec:noAccess>
				
				<li><a class="home" href="${createLink(action: 'list')}">
					<g:message code="default.refresh.label"/></a>
				</li>
				
			</ul>
		</div>
		<div id="list-entrada" class="content scaffold-list" role="main">
			<h1>
			<g:almacenDescripcion code="default.list.cierre.label" almacen="${almacen}"/>
			
			</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			

	<div id="cierreList">
		<div class="list">
			<table>
				<thead>
					<tr>											
						<%-- <td>Fecha Cierre<td>					
						<td>Reporte<td>
						<td>Eliminar<td>--%>
					</tr>					
				</thead>
				<tbody>
					<g:each in="${cierreInstanceList}" status="i" var="fechaCierre">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
							<td>
								${fechaCierre?.format('dd/MM/yyyy')}
							</td>
							
							<td>
								<g:jasperReport jasper="reportCierre" format="PDF,XLSX" delimiter=" " name="ReporteCierre" 
									controller="${controllerName}" action="reporte">		
								<input type="hidden" name="fechaCierre" value="${fechaCierre?.format('dd/MM/yyyy')}" />
								<input type="hidden" name="almacen" value="${almacen}" />								
								</g:jasperReport>
							
							</td>
							<sec:noAccess expression="hasRole('ROLE_FARMACIA_LECTURA')">							
								<td>
									<g:link  action="eliminar" onclick="return confirm('Esta seguro de eliminar el cierre?');"  params="[fechaCierre: fechaCierre?.format('dd/MM/yyyy')]" >Eliminar</g:link>
								</td>
							</sec:noAccess>							
						</tr>
					</g:each>
				</tbody>
			</table>
		</div>
	</div>

</div>
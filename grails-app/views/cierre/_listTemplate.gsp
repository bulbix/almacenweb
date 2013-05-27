
<a href="#list-entrada" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-entrada" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
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
								${fechaCierre?.format('dd/MM/yyy')}
							</td>
							
							<td>
								<g:jasperReport jasper="reportCierre" format="PDF,XLSX" delimiter=" " name="ReporteCierre" 
									controller="${controllerName}" action="reporte">		
								<input type="hidden" name="fechaCierre" value="${fechaCierre?.format('dd/MM/yyy')}" />
								<input type="hidden" name="almacen" value="${almacen}" />								
								</g:jasperReport>
							
							</td>
														
							<td>
								<g:link  action="eliminar" params="[fechaCierre: fechaCierre?.format('dd/MM/yyy')]" >Eliminar</g:link>
							</td>							
						</tr>
					</g:each>
				</tbody>
			</table>
		</div>
	</div>

</div>
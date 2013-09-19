<filterpane:includes />

<a href="#list-salida" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
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
		<div id="list-salida" class="content scaffold-list" role="main">
			<h1>
				<g:almacenDescripcion code="default.list.salida.label" almacen="${almacen}"/>				
			</h1>
			
			
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			
			<div id="salidaList">
			<div class="list">
				<table>
					<thead>
						<tr>
						<g:sortableColumn property="folio" title="Folio" params="${filterParams}" />					
						<g:sortableColumn property="fecha" title="Fecha" params="${filterParams}"  />										
						<g:sortableColumn property="usuario" title="Registro" params="${filterParams}" />
						<g:sortableColumn property="area" title="Area" params="${filterParams}" />
						
						<g:if test="${almacen != 'F'}">
								<g:sortableColumn property="diagnostico" title="Procedimiento" params="${filterParams}" />
								<g:sortableColumn property="paqueteq" title="Paquete" params="${filterParams}" />						
						</g:if>
											
						<g:sortableColumn property="estado" title="Estado" params="${filterParams}" />					
						</tr>
					</thead>
					<tbody>
					<g:each in="${salidaInstanceList}" status="i" var="salidaInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						
							<td>${fieldValue(bean: salidaInstance, field: "folio")}</td>					
							<td><g:link action="create" id="${salidaInstance.id}">
								<g:formatDate date="${salidaInstance.fecha}" format="dd/MM/yyyy" /></g:link></td>
							<td>${fieldValue(bean: salidaInstance, field: "usuario")}</td>
							<td>${fieldValue(bean: salidaInstance, field: "area")}</td>
							
							<g:if test="${almacen != 'F'}">
								<td>${fieldValue(bean: salidaInstance, field: "diagnostico")}</td>					
								<td>${fieldValue(bean: salidaInstance, field: "paqueteq")}</td>						
							</g:if>
							
							<td>${fieldValue(bean: salidaInstance, field: "estado")=='A'?'ACTIVO':'CANCELADO'}</td>
							
							<g:if test="${salidaInstance.estado == 'C' && (salidaInstance?.dueno || isAdmin)}" >
								<td><g:link action="eliminar" id="${salidaInstance.id}" onclick="return confirm('Esta seguro de eliminar la salida?');">Eliminar</g:link></td>
							</g:if>
							
						
						</tr>
					</g:each>
					</tbody>
				</table>
			</div>
			</div>
		</div>
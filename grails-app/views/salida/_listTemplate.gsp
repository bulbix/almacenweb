<a href="#list-salida" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-salida" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					<g:sortableColumn property="folio" title="Folio" />					
					<g:sortableColumn property="fecha" title="Fecha" />										
					<g:sortableColumn property="usuario" title="Registro" />
					<g:sortableColumn property="area" title="Area" />
					
					<g:if test="${almacen != 'F'}">
							<g:sortableColumn property="diagnostico" title="Procedimiento" />
							<g:sortableColumn property="paqueteq" title="Paquete" />						
					</g:if>
										
					<g:sortableColumn property="estado" title="Estado" />					
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
						
						<g:if test="${salidaInstance.estado == 'C'}" >
							<td><g:link action="eliminar" id="${salidaInstance.id}">Eliminar</g:link></td>
						</g:if>
						
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${salidaInstanceTotal}" />
			</div>
		</div>
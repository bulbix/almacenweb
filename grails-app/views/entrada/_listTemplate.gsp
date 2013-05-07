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
			<table style="text-align:center">
				<thead>
					<tr>					
						<g:sortableColumn property="numeroEntrada" title="Folio Entrada" />					
						<g:sortableColumn property="fechaEntrada" title="Fecha Entrada" />					
						<g:sortableColumn property="idSalAlma" title="Salida Almacen" />					
						<g:sortableColumn property="numeroFactura" title="Remision" />
						<g:sortableColumn property="usuario" title="Registro" />
						<g:sortableColumn property="estadoEntrada" title="Estado" />					
					</tr>
				</thead>
				<tbody>
				<g:each in="${entradaInstanceList}" status="i" var="entradaInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td>${fieldValue(bean: entradaInstance, field: "numeroEntrada")}</td>					
						<td><g:link action="create" id="${entradaInstance.id}">
							<g:formatDate date="${entradaInstance.fechaEntrada}" format="dd/MM/yyyy" /></g:link></td>											
						<td>${fieldValue(bean: entradaInstance, field: "folioAlmacen")}</td>					
						<td>${fieldValue(bean: entradaInstance, field: "numeroFactura")}</td>					
						<td>${fieldValue(bean: entradaInstance, field: "usuario")}</td>
						<td>${fieldValue(bean: entradaInstance, field: "estadoEntrada")}</td>					
											
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${entradaInstanceTotal}" />
			</div>
		</div>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'cierreFarmacia.label', default: 'CierreFarmacia')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		
		<style>
		.progress-label {
			float: left;
			margin-left: 50%;
			margin-top: 5px;
			font-weight: bold;
			text-shadow: 1px 1px 0 #fff;
		}
		</style>
		
	</head>
	<body>
	
		<g:javascript src="cierre.js" />	
			
		<a href="#create-cierreFarmacia" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		
		<table>
			<tr>
				<td>
					<label for="fechaCierre">Fecha Cierre</label>							
					<g:textField name="fechaCierre" size="8" value="30/04/2013" />
				</td>
			</tr>
						
			<tr>
				<td>
					<input type="button" id="generar" name="generar" value="Generar Cierre"/>
				</td>
			</tr>
		
		</table>		
		
		<div id="progressbar"><div class="progress-label">Loading...</div></div>
		
	</body>
</html>

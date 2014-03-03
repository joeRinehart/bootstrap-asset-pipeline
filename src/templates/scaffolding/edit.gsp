<%=packageName%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="bootstrap">
        <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
	<body>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="text-left col-sm-6" style="padding-left:0px">
                <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            </div>
            <div class="text-right col-sm-6" style="padding-top:20px">
                <g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-arrow-left"></span>&nbsp;<g:message code="default.list.label" args="[entityName]" /></g:link>
            </div>
        </div>
    </div>
    <div class="container-fluid">
        <g:hasErrors>
            <div class="alert alert-danger" role="status"><g:message code="default.errors.notification" default="Please correct the errors highlighted below." /></div>
        </g:hasErrors>
        <g:if test="\${flash.message}">
            <div class="alert alert-info" role="status">\${flash.message}</div>
        </g:if>
        <g:form url="[resource:${propertyName}, action:'update']" method="PUT" <%= multiPart ? ' enctype="multipart/form-data"' : '' %> class="form form-horizontal">
        <g:hiddenField name="version" value="\${${propertyName}?.version}" />
        <g:render template="form"/>

        <div class="form-group">
            <div class="\${BootstrapTagLib.labelColumnClass}"></div>
            <div class="\${BootstrapTagLib.controlColumnClass}">
                <g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-arrow-left"></span>&nbsp;<g:message code="default.list.label" args="[entityName]" /></g:link>
                <g:actionSubmit class="btn btn-primary" action="update" value="\${message(code: 'default.button.update.label', default: 'Update')}" />
            </div>
        </div>
        </g:form>
    </div>
	</body>
</html>

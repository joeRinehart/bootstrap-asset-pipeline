<% import grails.persistence.Event %>
<%=packageName%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="bootstrap">
		<g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

    <div class="container-fluid">
        <div class="row-fluid">
            <div class="text-left col-sm-6" style="padding-left:0px">
                <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            </div>
            <div class="text-right col-sm-6" style="padding-top:20px">
                <g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-arrow-left"></span>&nbsp;<g:message code="default.list.label" args="[entityName]" /></g:link>
            </div>
        </div>
    </div>
    <div class="container-fluid">
        <g:if test="\${flash.message}">
            <div class="row-fluid">
                <div class="alert alert-info" role="status">\${flash.message}</div>
            </div>
        </g:if>
        <g:form url="[resource:${propertyName}, action:'delete']" method="DELETE" class="form form-horizontal">

            <%  excludedProps = Event.allEvents.toList() << 'id' << 'version'
            allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
            props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) }
            Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
            props.each { p -> %>
            <g:if test="\${${propertyName}?.${p.name}}">
                <bootstrap:field
                        bean="\${${propertyName}}"
                        field="${domainClass.propertyName}"
                        label="\${message(code:'${domainClass.propertyName}.${p.name}.label', default:"${p.naturalName}" )}"
                        required="false"
                >
                    <%  if (p.isEnum()) { %>
                    <p class="form-control-static" aria-labelledby="${p.name}-label"><g:fieldValue bean="\${${propertyName}}" field="${p.name}"/></p>
                    <%  } else if (p.oneToMany || p.manyToMany) { %>
                    <g:each in="\${${propertyName}.${p.name}}" var="${p.name[0]}">
                        <p class="form-control-static" aria-labelledby="${p.name}-label"><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link></p>
                    </g:each>
                    <%  } else if (p.manyToOne || p.oneToOne) { %>
                    <p class="form-control-static" aria-labelledby="${p.name}-label"><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link></p>
                    <%  } else if (p.type == Boolean || p.type == boolean) { %>
                    <p class="form-control-static" aria-labelledby="${p.name}-label"><g:formatBoolean boolean="\${${propertyName}?.${p.name}}" /></p>
                    <%  } else if (p.type == Date || p.type == java.sql.Date || p.type == java.sql.Time || p.type == Calendar) { %>
                    <p class="form-control-static" aria-labelledby="${p.name}-label"><g:formatDate date="\${${propertyName}?.${p.name}}" /></p>
                    <%  } else if (!p.type.isArray()) { %>
                    <p class="form-control-static" aria-labelledby="${p.name}-label"><g:fieldValue bean="\${${propertyName}}" field="${p.name}"/></p>
                    <%  } %>
                </bootstrap:field>
                </li>
            </g:if>
            <%  } %>


        <div class="form-group">
            <div class="\${BootstrapTagLib.labelColumnClass}"></div>
            <div class="\${BootstrapTagLib.controlColumnClass}">
                <g:link action="index" class="btn btn-default"><span class="glyphicon glyphicon-arrow-left"></span>&nbsp;<g:message code="default.list.label" args="[entityName]" /></g:link>
                <g:link class="btn btn-primary" action="edit" resource="\${${propertyName}}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                <g:actionSubmit class="btn btn-danger" action="delete" value="\${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('\${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </div>
        </div>
        </g:form>
    </div>

	</body>
</html>

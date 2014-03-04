import groovy.xml.MarkupBuilder
import org.apache.commons.lang.StringEscapeUtils
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.web.servlet.support.RequestContextUtils

class BootstrapTagLib extends ValidationTagLib {

    static namespace="bootstrap"
    static excludedProps=["dateCreated","lastUpdated","metaClass","class","validationSkipMap","gormPersistentEntity","properties","gormDynamicFinders","all","domainClass","attached","constraints","version","validationErrorsMap","dirtyPropertyNames","errors","dirty","count"]

    static labelColumnClass = 'col-sm-4'
    static controlColumnClass = 'col-sm-7'

    def field = { attrs, body ->

        out << fieldContainer( attrs,
                fieldLabel( attrs, body )
                        << fieldControls( attrs,
                        body()
                                << ( fieldHelp( attrs, body ) )
                )
        )
    }

    def fieldContainer = { attrs, body ->
        def bean = attrs.bean
        String field = attrs.field
        String className = attrs.className ?: "form-group"

        def required = ( ( attrs?.required && attrs.required ) ? true : false )

        if ( !field ) {
            throwTagError "'field' is required"
        }

        out << "\n<div class=\"${className} ${hasErrors(bean: bean, field: field, 'has-error')} ${required ? 'required' : '' }\">\n\t"
        out << body()
        out << '\n</div>'
    }

    def fieldLabel = { attrs, body ->
        def bean = attrs.bean
        String field = attrs.field
        String label = attrs.label
        def required = ( ( attrs?.required && attrs.required.toString() == 'true' ) ? true : false )

        if ( !field || !label ) {
            throwTagError "'field', and 'label' are required"
        }

        out << "<label class=\"" + labelColumnClass + ( required ? '' : ' optional-label' ) + " control-label\" for=\"${attrs.id ?: field}\">\n\t\t"
        if ( required ) {
            out << '<span class="required-indicator">*</span>\n\t'
        }
        out << label


        out << '</label>'
    }

    def fieldControls = { attrs, body ->
        String className = attrs.className ?: controlColumnClass

        out << "\n\t<div class=\"${className}\">\n"
        out << body()
        out << '\n\t</div>'
    }


    def fieldHelp = { attrs, body ->
        def bean = attrs.bean
        String field = attrs.field
        String description = attrs.description

        if ( !field ) {
            throwTagError "'field' is required"
        }

        if ( !description && !bean?.errors?.hasFieldErrors( field ) ) {
            return
        }

        out << '\n\t\t<span class="help-block">'

        if ( bean?.errors?.hasFieldErrors( field ) ) {
            renderErrorsWithClass( [ bean: bean, field: field ], body, out )
        } else {
            out << description
        }

        out << '\n\t\t</span>'
    }


    Closure renderErrorsWithClass = { attrs, body, out ->
        def renderAs = attrs.remove('as')
        if (!renderAs) renderAs = 'list'

        if (renderAs == 'list') {
            def codec = attrs.codec ?: 'HTML'
            if (codec == 'none') codec = ''

            def errorsList = extractErrors(attrs)
            if (errorsList) {
                out << '\n<ul class="unstyled">'
                out << eachErrorInternalForList(attrs, errorsList, {
                    out << "\n<li>${message(error:it, encodeAs:codec)}</li>"
                })
                out << '\n</ul>'
            }
        }
        else if (renderAs.equalsIgnoreCase("xml")) {
            def mkp = new MarkupBuilder(out)
            mkp.errors() {
                eachErrorInternal(attrs, {
                    error(object: it.objectName,
                            field: it.field,
                            message: message(error:it)?.toString(),
                            'rejected-value': StringEscapeUtils.escapeXml(it.rejectedValue))
                })
            }
        }
    }

    /*
     * Based on the resources Bootstrap plugin at https://raw.github.com/groovydev/twitter-bootstrap-grails-plugin/master/grails-app/taglib/org/groovydev/TwitterBootstrapTagLib.groovy
     *
     * I've made tweaks to correctly (not) use spans within the pagination, etc. and do SR-friendly 'current'. If this publishes,
     * send a diff back to the resources bootstrap plugin dev.
     */

    /**
     * Creates next/previous links to support pagination for the current controller.<br/>
     *
     * &lt;g:paginate total="${Account.count()}" /&gt;<br/>
     *
     * @emptyTag
     *
     * @attr total REQUIRED The total number of results to paginate
     * @attr action the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id The id to use in the link
     * @attr params A map containing request parameters
     * @attr prev The text to display for the previous link (defaults to "Previous" as defined by default.paginate.prev property in I18n messages.properties)
     * @attr next The text to display for the next link (defaults to "Next" as defined by default.paginate.next property in I18n messages.properties)
     * @attr max The number of records displayed per page (defaults to 10). Used ONLY if params.max is empty
     * @attr maxsteps The number of steps displayed for pagination (defaults to 10). Used ONLY if params.maxsteps is empty
     * @attr offset Used only if params.offset is empty
     * @attr fragment The link fragment (often called anchor tag) to use
     */
    def paginate = { attrs ->

        def writer = out
        if (attrs.total == null) {
            throwTagError("Tag [paginate] is missing required attribute [total]")
        }
        def messageSource = grailsAttributes.messageSource
        def locale = RequestContextUtils.getLocale(request)

        def total = attrs.int('total') ?: 0
        def action = (attrs.action ? attrs.action : (params.action ? params.action : "index"))
        def offset = params.int('offset') ?: 0
        def max = params.int('max')
        def maxsteps = (attrs.int('maxsteps') ?: 10)

        if (!offset) offset = (attrs.int('offset') ?: 0)
        if (!max) max = (attrs.int('max') ?: 10)

        def linkParams = [:]
        if (attrs.params) linkParams.putAll(attrs.params)
        linkParams.offset = offset - max
        linkParams.max = max
        if (params.sort) linkParams.sort = params.sort
        if (params.order) linkParams.order = params.order

        def linkTagAttrs = [action:action]
        if (attrs.controller) {
            linkTagAttrs.controller = attrs.controller
        }
        if (attrs.id != null) {
            linkTagAttrs.id = attrs.id
        }
        if (attrs.fragment != null) {
            linkTagAttrs.fragment = attrs.fragment
        }
        //add the mapping attribute if present
        if (attrs.mapping) {
            linkTagAttrs.mapping = attrs.mapping
        }

        linkTagAttrs.params = linkParams

        def cssClasses = "pagination"
        if (attrs.class) {
            cssClasses = "pagination " + attrs.class
        }

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = (offset / max) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil(total / max))

        writer << "<ul class=\"${cssClasses}\">"
        // display previous link when not on firststep
        if (currentstep > firststep) {
            linkParams.offset = offset - max
            writer << '<li class="prev">'
            writer << link(linkTagAttrs.clone()) {
                (attrs.prev ?: messageSource.getMessage('paginate.prev', null, '&laquo;', locale))
            }
            writer << '</li>'
        }
        else {
            writer << '<li class="prev disabled"><a href="#">'
            writer << (attrs.prev ?: messageSource.getMessage('paginate.prev', null, '&laquo;', locale))
            writer << '</a></li>'
        }

        // display steps when steps are enabled and laststep is not firststep
        if (steps && laststep > firststep) {
            linkTagAttrs.class = 'step'

            // determine begin and endstep paging variables
            int beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
            int endstep = currentstep + Math.round(maxsteps / 2) - 1

            if (beginstep < firststep) {
                beginstep = firststep
                endstep = maxsteps
            }
            if (endstep > laststep) {
                beginstep = laststep - maxsteps + 1
                if (beginstep < firststep) {
                    beginstep = firststep
                }
                endstep = laststep
            }

            // display firststep link when beginstep is not firststep
            if (beginstep > firststep) {
                linkParams.offset = 0
                writer << '<li>'
                writer << link(linkTagAttrs.clone()) {firststep.toString()}
                writer << '</li>'
                writer << '<li class="disabled"><a href="#">...</a></li>'
            }

            // display paginate steps
            (beginstep..endstep).each { i ->
                if (currentstep == i) {
                    writer << "<li class=\"active\"><a href='#'>"
                    writer << "${i}"
                    writer << "<span class=\"sr-only\">(current)</span></a></li>";
                }
                else {
                    linkParams.offset = (i - 1) * max
                    writer << "<li>";
                    writer << link(linkTagAttrs.clone()) {i.toString()}
                    writer << "</li>";
                }
            }

            // display laststep link when endstep is not laststep
            if (endstep < laststep) {
                writer << '<li class="disabled"><a href="#">...</a></li>'
                linkParams.offset = (laststep -1) * max
                writer << '<li>'
                writer << link(linkTagAttrs.clone()) { laststep.toString() }
                writer << '</li>'
            }
        }

        // display next link when not on laststep
        if (currentstep < laststep) {
            linkParams.offset = offset + max
            writer << '<li class="next">'
            writer << link(linkTagAttrs.clone()) {
                (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, '&raquo;', locale))
            }
            writer << '</li>'
        }
        else {
            linkParams.offset = offset + max
            writer << '<li class="disabled"><a href="#">'
            writer << (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, '&raquo;', locale))
            writer << '</a></li>'
        }

        writer << '</ul>'
    }

}

class BootstrapAssetPipelineGrailsPlugin {
    // the plugin version
    def version = "1.0"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.3 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Bootstrap Asset Pipeline Plugin" // Headline display name of the plugin
    def author = "Joe Rinehart"
    def authorEmail = "joe.rinehart@gmail.com"
    def description = '''\
Quickly sets up Twitter Bootstrap via the asset-pipeline plugin, providing asset- and CDN-based layouts and basic scaffold templates.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/bootstrap-asset-pipeline"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "github", url: "https://github.com/joeRinehart/bootstrap-asset-pipeline/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/joeRinehart/bootstrap-asset-pipeline" ]

}

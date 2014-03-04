target (installBootstrapScaffolds: "Creates the Bootstrap templates") {
    templatesDir = "${basedir}/src/templates/scaffolding"
    srcDir = "${bootstrapAssetPipelinePluginDir}/src/templates/scaffolding"

    ant.input(addProperty: "installBootstrapScaffolds.doIt", message: "Replace .gsp contents of ${templatesDir}? ", validArgs: "y,n")
    if (ant.antProject.properties['installBootstrapScaffolds.doIt'] == 'y') {
        ant.copy(todir: templatesDir, overwrite: true) {
            fileset(dir: srcDir)
        }
        println "Templates created in src/templates/scaffolding."
    }
}
setDefaultTarget 'installBootstrapScaffolds'

grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    plugins {
        build(":release:3.0.1",
              ":rest-client-builder:1.0.3") {
            export = false
        }

        // asset management
        compile ":asset-pipeline:1.6.1"
        compile ":less-asset-pipeline:1.5.3"
    }
}

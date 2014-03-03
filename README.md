Bootstrap for Asset Pipeline
=====================

Getting Started
---------------

If you've installed the plugin, you're 95% there. I mainly created it so that us server-side types can get up and running with Bootstrap via asset-pipeline without having to deal with downloads, arrangement, or boilerplate

If you know Grails and don't like reading documentation, read the following line and go have fun:

    <!-- Uses asset-pipeline to link to locally bundled Bootstrap and jQuery -->
    <meta name="layout" content="bootstrap">

Otherwise, read on. There's not much to learn.

Using the layouts
-----------------

Once you've installed it, just switch the layout of your view to one of two different included layouts:

    <!-- Uses asset-pipeline to link to locally bundled Bootstrap and jQuery -->
    <meta name="layout" content="bootstrap">

...or...

    <!-- Use vanilla links to CDN-distibuted  -->
    <meta name="layout" content="bootstrap-cdn">

Using the scaffolds
-------------------

If you like to scaffold (I generally don't), run the following script to install the bootstrap-specific templates:

    grails install-bootstrap-scaffolds

After that, scaffold away as normal:

    grails generate-all Person

The included scaffolds are intentionally basic: if I took them any further, they'd probably include things you'd want to change, replace, tailor, or discard altogether. For example, I don't like to include a 'show' action at all, but I kept it to keep in line with default Grails scaffolding.

Things to Know
--------------

* When using the included layouts, you're IE8-safe out of the box
* It transitively depends on both the asset-pipeline and less-asset-pipeline plugins, allowing you to modify Bootstrap using LESS instead of the messy thing that is CSS.


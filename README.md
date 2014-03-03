Bootstrap for Asset Pipeline
=====================

Things to Know
--------------

* When using the included layouts, you're IE8-safe out of the box
* It transitively depends on both the asset-pipeline and less-asset-pipeline plugins, allowing you to modify Bootstrap using LESS instead of the messy thing that is CSS.

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

The included scaffolds are intentionally basic: I'm not huge on scaffolds to begin with, and if I took them any further, they'd probably include things you'd want to change, replace, tailor, or discard altogether. For example, I don't like to include a 'show' action at all, but I kept it to keep in line with default Grails scaffolding.

Development guides
=============

This document contains some instructions about developing this application.

Required software
-------

You need to have the following software installed:

* Maven
* Android SDK (API level 8)
* Git

Preferred IDE is IntelliJ IDEA.


Project installation and configuration
-------

1. Checkout the sources from GitHub:
`git@github.com:mvestola/helsinki-testbed2-android.git`
2. To the root directory (where the pom.xml is), add file local.properties
and specify where you have installed your Android SDK to "sdk.dir" property
(see file conf/default.properties for example)
3. If you want to build signed
3. Build the project with maven: `mvn clean install`

You should also set the Android SDK to environmental variable "ANDROID_HOME"

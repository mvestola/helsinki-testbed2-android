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
`git clone git@github.com:mvestola/helsinki-testbed2-android.git`
2. To the root directory (where the pom.xml is), add file local.properties
and specify where you have installed your Android SDK to "sdk.dir" property
(see file conf/default.properties for example). This file is required by the
Robolectric framework.
3. Also add local.properties file to conf/local.properties. Just make a copy of
the file default.properties and change the field to match your configuration.
3. Build the project with maven: `mvn clean install`

You should also set the Android SDK to environmental variable "ANDROID_HOME"


Adding new custom jars to the project's Maven repository
-------

The maven repository for this project is hosted at github:
https://raw.github.com/mvestola/helsinki-testbed2-android-repo/master/repo
(yes, it returns 404 if you go to the url with your browser, but that does not matter)

The repo was made by following the instructions below:
http://blog.marrowboy.co.uk/2011/11/08/how-to-host-a-maven-repo-on-github/

You can add more jars to the repo by cloning the repo and executing `mvn deploy:deploy-file`
(http://maven.apache.org/guides/mini/guide-3rd-party-jars-remote.html).
For example (all in one line):
`mvn deploy:deploy-file
-DgroupId=com.jhlabs
-DartifactId=javaproj-noawt
-Dversion=1.0.6
-Dpackaging=jar
-Dfile=/path/in/your/computer/javaproj-1.0.6-noawt.jar
-DrepositoryId=testbed2repo
-Durl=file:///path/in/your/computer/to/the/clonedrepo/helsinki-testbed2-android-repo/repo`

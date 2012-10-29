Development guides
=============

This document contains some instructions about developing this application.

Required software
-------

You need to have the following software installed:

* Maven
* Android SDK (API level 13)
* Git

Preferred IDE is IntelliJ IDEA and preferred operating system is Linux.


Android frameworks used
-------

The following Android frameworks are used in this application:

* [RoboGuice 2](https://code.google.com/p/roboguice/) (dependency injection framework for Android based on Google Guice)
* [Robolectric](http://pivotal.github.com/robolectric/) (advanced unit testing framework for Android)
* [Robotium](http://code.google.com/p/robotium/) (automatic black-box testing framework for Android, like Selenium on Android)

Some instructions for using RoboGuice:
* http://code.google.com/p/roboguice/wiki/ProvidedInjections
* http://code.google.com/p/roboguice/source/browse/astroboy/src/test/java/org/roboguice/astroboy/controller/Astroboy2Test.java
* http://code.google.com/p/roboguice/wiki/UpgradingTo20
* http://code.google.com/p/roboguice/wiki/SimpleExample
* http://www.blog.project13.pl/wp-content/uploads/2011/12/presentation.html#slide35 (injection scopes)
* http://www.blog.project13.pl/wp-content/uploads/2011/12/presentation.html#slide58 (testing)


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


Maven instructions
-------

Run the application in emulator
(does not seem to save preferences so can be used for initial installation testing):
```
mvn -Dmaven.test.skip=true clean package android:redeploy android:run
```


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
```
mvn deploy:deploy-file \
-DgroupId=com.jhlabs \
-DartifactId=javaproj-noawt \
-Dversion=1.0.6 \
-Dpackaging=jar \
-Dfile=/path/in/your/computer/javaproj-1.0.6-noawt.jar \
-DrepositoryId=testbed2repo \
-Durl=file:///path/in/your/computer/to/the/clonedrepo/helsinki-testbed2-android-repo/repo
```


Adding Android library projects as Maven dependencies
-------

In addition to adding jar dependencies, you can also easily add Android library projects as project dependencies.
Just do the following:

1. Checkout the source of your Android library project. Do not compile the project
(or make sure that there are no compiled classes by running `ant clean`).
2. Make a zip file from the project sources. In addition to the src directory,
also include the res, gen and other source folders to the zip file.
Do not include any compiled classes.
3. Rename the zip file to `something.apklib`.
4. Install the file `something.apklib` into the testbed2 maven repository like jar files described above.
Just change the packaging type to `apklib` (`-Dpackaging=apklib`).

After that, you can use the apklib as a dependency in pom.xml:

```
<dependency>
  <groupId>something</groupId>
  <artifactId>something</artifactId>
  <version>1.0</version>
  <type>apklib</type>
</dependency>
```

More information about maven and Android library projects:

* http://stackoverflow.com/questions/9931039/adding-an-eclipse-android-library-project-and-building-via-maven
* http://code.google.com/p/maven-android-plugin/wiki/ApkLib


Testing guidelines
-------

Test at least these special cases:
* Downloading images while changing screen orientation (Ctrl+F11).
Should not pause download.
* Only one map image selected.
* No network connection (disable network from computer running emulator).
Should show error dialog.
* Parsing error (change HTML_TIMESTAMP_PREFIX to some invalid value).
Should show error dialog.
* Cancelling download in ParsingActivity and AnimationActivity.
Should show toast.
* Continuing download when download cancelled.
Should not download all images again.
* Try to kill app with DDMS when it is in the background.
Should not fail when resuming.
* Use very fast animation (0.1 sec).
* Play/pause button icon when application goes to background,
returning from preferences and using the slider.
* Both English and Finnish languages (change language
from the Emulator's "Custom locale")
* Default settings (remove the app and reset all saved settings)

Checklist for publishing a new release
-------

1. Change the version code and version name from `AndroidManifest.xml`
2. Update `CHANGELOG.md`
3. Update `whats_new_text` from `res/values/strings.xml` (both English and Finnish)
4. Sign the app bu running `mvn clean install` using the release profile
5. Tag the code: `git tag -a 2.0.x -m 'Tagged 2.0.x'; git push --tags`
6. Upload the file `target/testbedViewer-aligned.apk` to Google Play and activate it
7. Update "Recent Changes" in Google Play (both English and Finnish)

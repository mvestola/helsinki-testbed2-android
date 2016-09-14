Development guides
=============

This document contains some instructions about developing this application.

Required software
-------

You need to have the following software installed:

* Gradle
* Android SDK (API level 23)
* Git

Preferred IDE is Android Studio and preferred operating system is Ubuntu/Kubuntu.


Android frameworks used
-------

The following Android frameworks are used in this application:

* [RoboGuice](https://code.google.com/p/roboguice/) (dependency injection framework for Android based on Google Guice)
* [Robolectric](http://pivotal.github.com/robolectric/) (advanced unit testing framework for Android)
* [Robotium](http://code.google.com/p/robotium/) (automatic black-box testing framework for Android, like Selenium on Android)
* [AndroidAnnotations](http://androidannotations.org/) (simplify Android development with annotations)
* [Project Lombok](http://projectlombok.org/features/index.html) (simplify Java development with annotations generating e.g. getters and setters)

Some instructions for using RoboGuice:
* https://github.com/robolectric/robolectric/wiki
* https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric


Project installation and configuration
-------

TODO: fix this instructions

1. Checkout the sources from GitHub:
`git clone git@github.com:mvestola/helsinki-testbed2-android.git`
2. To the root directory (where the pom.xml is), add file local.properties
and specify where you have installed your Android SDK to "sdk.dir" property
(see file src/main/config/default.properties for example).
This file is required by the Robolectric framework.
3. Also add local.properties file to src/main/config/local.properties. Just make a copy of
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

TODO: fix this

1. Change the version code and version name from `build.gradle`
2. Update `CHANGELOG.md`
3. Update `whats_new_text` from `res/values/strings.xml` (both English and Finnish)
4. Sign the app by running `mvn clean install` using the release profile
5. Tag the code: `git tag -a 2.0.x -m 'Tagged 2.0.x'; git push --tags`
6. Upload the file `target/testbedViewer-aligned.apk` to [Google Play](https://play.google.com/apps/publish/v2) and activate it
7. Update "Recent Changes" in Google Play (both English and Finnish)

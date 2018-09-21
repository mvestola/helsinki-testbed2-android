Development guides
=============

This document contains some instructions about developing this application.

Required software
-------

You need to have the following software installed:

* Gradle
* Android SDK (API level 27)
* Git
* Android Studio

Preferred operating system is Ubuntu.


Android frameworks used
-------

The following Android frameworks / libraries are used in this application:

* [RoboGuice](https://github.com/roboguice/roboguice/) (dependency injection framework for Android based on Google Guice)
* [Robolectric](http://robolectric.org/) (advanced unit testing framework for Android)
* [Robotium](http://code.google.com/p/robotium/) (automatic black-box testing framework for Android, like Selenium on Android)
* [AndroidAnnotations](http://androidannotations.org/) (simplify Android development with annotations)
* [Project Lombok](http://projectlombok.org/features/index.html) (simplify Java development with annotations generating e.g. getters and setters)
* [WeatherIconView](https://github.com/pwittchen/WeatherIconView) (show weather icons using a font)
* [FlexboxLayout](https://github.com/google/flexbox-layout) (flexible CSS style flexbox layout for Android)
* [Java Map Projection Library in Android](http://augusttown.blogspot.fi/2010/03/using-java-map-projection-library-in.html) (Used for map projection calculation, JAR file in own testbd repo)

Some instructions for using RoboGuice:
* https://github.com/robolectric/robolectric/wiki
* https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric


Project installation and configuration
-------

1. Checkout the sources from GitHub:
`git clone git@github.com:mvestola/helsinki-testbed2-android.git`
2. Open the project with Android Studio and run gradle command `build` to build the project. 

You should also set the Android SDK to environmental variable "ANDROID_HOME"

To sign a release version of the application, you need to have a file `~/.gradle/gradle.properties` with the following content:
```
RELEASE_STORE_FILE=/your/store/file/location
RELEASE_STORE_PASSWORD=your_store_password
RELEASE_KEY_ALIAS=your_alias
RELEASE_KEY_PASSWORD=your_key_password
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

Testing guidelines
-------

Test at least these special cases:
* Downloading images while changing screen orientation (Ctrl+F11).
Should not pause download.
* Slow network connection
* Emulator with oldest supported API version and small screen
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
* Try out with large screen device such as tablet

Checklist for publishing a new release
-------

1. Change the version code and version name from `build.gradle`
2. Update `CHANGELOG.md`
3. Update `whats_new_text` from `res/values/strings.xml` (both English and Finnish)
4. Create a signed apk package by running `Build...Build APK` from the Android studio using the build variant `release` (bottom left corner in Android Studio)
5. Tag the code: `git tag -a 2.0.x -m 'Tagged 2.0.x'; git push --tags`
6. Upload the file `build/outputs/apk/testbed-release.apk` to [Google Play](https://play.google.com/apps/publish/) and activate it
7. Update "Recent Changes" in Google Play (both English and Finnish)

Future refactoring plans
------------------------

When API level 9 support can be dropped, can do the following refactorings:
* Use com.android.support:preference-v7:24.2.1 and built in MultiSelectListPreference (remove ListPreferenceMultiSelect)
* Use better color picker preference: https://github.com/martin-stone/hsv-alpha-color-picker-android
* Use better seekbar preference: https://github.com/MrBIMC/MaterialSeekBarPreference (this does not support allowed values?)

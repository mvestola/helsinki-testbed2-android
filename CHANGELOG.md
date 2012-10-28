Helsinki Testbed Viewer 2.0
=============

Version 2.0.10 (coming soon...)
-------

* Added GPS as an alternate location provider (for tablets)
* User can save his/her current location as a fixed position
* Automatically starts loading pictures when the application is opened
* Added reset zoom to context menu
* Improved layout to support Android tablets
* Added advertisements which can be disabled from the preferences (TODO)


Version 2.0.9 (2012-10-23)
-------

* Added pinch and double-tap zooming
* Now uses scalable SVG images as map markers. User can change the color and
the size of the markers from preferences.
* Shows the name of the municipality when long pressing the circle representing a municipality.
* Major code refactorings. Now uses RoboGuice 2 for dependency injection
and Robotium for automatic black-box testing.


Version 2.0.8 (2012-10-07)
-------

* Shows user's current location on the map (see settings)
* Shows selected municipalities/cities on the map (see settings). The municipalities are
represented as black dots (coordinates are from
[Wikipedia](http://fi.wikipedia.org/wiki/Luettelo_Suomen_kuntien_koordinaateista))
* Added "What's new" dialog for informing about new features


Version 2.0.7 (2012-09-26)
-------

* Fixed crash bug which occurred when resuming the application after long time of inactivity
* More user-friendly error messages and translated them to Finnish


Version 2.0.6 (2012-09-24)
-------

* Fixed NullPointerException which occurred sometimes after resuming to the application
* Refreshing the animation view after a short time does not download all images again but only new images.


Version 2.0.5 (2012-09-22)
-------

* Added slider control to the animation view
* Added sliders to the preference screen


Version 2.0.4 (2012-09-21)
-------

* Orientation change does not stop download anymore
* Context menu accessible also in download and map view
* Added refresh button to context menu
* Fixed possible OutOfMemory errors
* Version number visible in about box


Version 2.0.3 (2012-09-17)
-------

* Now immediately loads and shows the latest weather map image and continues downloading
the rest map images in the background.
* Added option to enable/disable animation auto-start


Version 2.0.2 (2012-09-16)
-------

* More detailed error messages


Version 2.0.1 (2012-09-16)
-------

* Translated to Finnish
* Sources published to GitHub

Version 2.0.0 (2012-09-15)
-------

* Fixed download error which was caused by missing Content-length when
downloading the actual image files from the servers.


Helsinki Testbed Weather View
=============

Below are the changelogs from the original uncontinued application "Helsinki Testbed Weather View".

Version 1.0.6 (2010-08-09)
-------

* Try to fix OOM-error which occurs rarely on real devices

Version 1.0.5
-------

* Timestamp GMT offset error fixed

Version 1.0.4
-------

* Android 1.5 compatible
* Added info dialog and copyright notes about Helsinki Testbed

Version 1.0.3
-------

* Map location is saved and restored to/from persistent storage

Version 1.0.2
-------

* Stop animation while not visible

Version 1.0.1
-------

* Animation scale issues fixed
* Animation control buttons as images

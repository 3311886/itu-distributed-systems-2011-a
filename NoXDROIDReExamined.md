NOxDROID Reflections and rationalizations

# Introduction #

Different aspects of NOxDROID touched upon in the literature of the course.

## Tolerance for ignorance (p. 56 Krumm 2010) ##
Nox service should try to start a new IOIOConectionThread when connection fails.

Skyhook and the phone's location service continue if no location is found.

The status of network-, location- and ioio connection is sent to the main gui (subscribe/publish using Messenger)

Storing data on the phone with timestamps ensures data validity throughout the track

##  ##
Old tracks should be wiped from the SQLite database, eg when db size exceeds some specified threshold or when they are older than some date.

## Implementation issues ##
Open source thirdparty frameworks:
IOIO Wrapper (YTAI): Communication between board and Android app

KMLFramework (Eivind Bøhn): Implements the XML schema for KML in a Java OO model. Encapsulates adding properties to KML documents (eg. PlaceMark).

Google Appengine (Google): Provides standards for exposing http get/post requests through Java Servlets.

Google Android (Google): API System design comprising custom services extending Service from google API. Enables plugging in more sensors as comunication is based on events (publish/subscribe model) and Messenger objects across Services (essentially parallel threads)

JQuery, javascript framework. Encapsulates and enhances many the javascript language and supports Ajax calls and dataconcumption (XML, Json etc.)

GoogleMaps API (Google): Visualization of map data. Pluggable with KML data thus in compatible with the chosen KML Framework as datatype for geotagging and visualizing sensor data.

## Evaluation ##
Using existing community based datasharing sites like Pachube and Geocommons could be a way to provide access to NOxDROID data for a wider audience/user participation
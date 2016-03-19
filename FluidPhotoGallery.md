# Introduction #

Android application that can show images and by certain gestures send an image to the FluidPhotoBrowser.

The application can also receive image(s) from the FluidPhotoBrowser.

This is the essential activities:


**Image gallery overview - [GalleryActivity.java](https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/FluidPhotoGallery/trunk/src/dk/itu/spct/GalleryActivity.java)**

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/android_fluid_gallery_1.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/android_fluid_gallery_1.png)

[Based upon this tutorial, Hello gallery](http://developer.android.com/resources/tutorials/views/hello-gallery.html)

**Image item view - gesture scroll down sends image to server / table top - [ImageTcpSendActivity.java](https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/FluidPhotoGallery/trunk/src/dk/itu/spct/ImageTcpSendActivity.java)**

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/android_fluid_gallery_2.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/android_fluid_gallery_2.png)

**Receive an image from table top [TCPImageServerActivity.java](https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/FluidPhotoGallery/trunk/src/dk/itu/spct/TCPImageServerActivity.java)**

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/android_fluid_gallery_3.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/android_fluid_gallery_3.png)


All done in separate activities - proof of example should be done with Async tasks etc... - code is really raw due to the very limited time for the implementation.

There are many other actives e.g.: send text message etc.:

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/android_fluid_gallery_0.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/android_fluid_gallery_0.png)


# Where is the code #


==== TTW (through the web) ====  http://code.google.com/p/itu-distributed-systems-2011-a/source/browse/pervasive-computing-ad-hoc/#pervasive-computing-ad-hoc%2FFluidPhotoGallery%2Ftrunk


#### Svn checkout in terminal ####


svn co https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/FluidPhotoGallery/trunk FluidPhotoGallery

then import FluidPhotoGallery to the eclipse android sdk.


## Helper TCP for debugging/testing ##

TTW:

http://code.google.com/p/itu-distributed-systems-2011-a/source/browse/pervasive-computing-ad-hoc/#pervasive-computing-ad-hoc%2Fpervasivecomp2011%2Ftrunk

Svn (its a full eclipse project but minimal - so import it as prefered):
https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/pervasivecomp2011/trunk



#### Setup ####

There is unfortunately some ip-address setup hassle involved and the application is 'raw'.


#### Protocol in use ####

TCP for send/receive images. Selected for simplicity.

Bluetooth could have been used and has the pro that a wifi-network connection is not need.

Other interesting protocols for use:

  * NCF
  * Websockets (requires webservice setup on both table top and android application)
  * more ?
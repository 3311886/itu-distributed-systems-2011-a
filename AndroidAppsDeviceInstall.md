# How to install android application on a 'real' android device #

Have:

  * device: huawei ideos

Do:

  * Connect device and computer with usb
  * To setup at device, do following on your phone
    1. go applications / development
    1. allow usb debuging on

  * then through adb (on computer / terminal):

  1. list devices
> > adb devices
  1. install application
> > adb -s <some id> install <path to>/YourApp/bin/YourApp.apk
    1. .g. adb -s 404D8E1812B3 install /Users/stinebierre/Documents/Studie/ITU/2.semester
> > /Mobile\_og\_distribuerede\_systemer/Workspace/ButtonTest/bin/ButtonTest.apk
  1. reinstall device with option -r
> > adb -s <some id> install -r <path to>/YourApp/bin/YourApp.apk


Document eventually how to do it through eclipse with screen shots and so if you need it (at sbie)


## Uninstall ##


```
    
    adb -s <some id> uninstall <package name space e.g. dk.itu.noxdroid>
    
```
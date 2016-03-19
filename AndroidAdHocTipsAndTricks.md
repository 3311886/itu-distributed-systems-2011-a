# adb #




## uninstall app ##


with adb do:

```

    adb -s <devices> uninstall <package name dk.itu.spvc.androidlocationsimple>

```

  * Its important its the package name and not the application name e.g.: AndroidLocationSimple.
  * or delete from devices directly with adb shell :

```

    adb -s <devices> shell
    # become root - requires your device to be rooted
    su
    cd /data/app
    ls
    dk.itu.spvc.android-2.apk
    dk.itu.spvc.android.test-2.apk
    com.nolanlawson.logcat-1.apk
    dk.itu.noxdroid-1.apk
    # just do rm
    rm dk.itu.noxdroid-1.apk
    
```


## pull /push file from / to device ##

```


				
    // Assume the gesture file exists on your Android device
    adb -s <device id> pull /sdcard/gestures ~/test
    // Now copy it back
    adb -s <device id> push ~/test/gesture /sdcard/gestures2 

```


## no devices shown ##

```

    # no devices are shown do even if its connected or emulator started
    adb devices
    <no devices shown>
    # the kill adb server and restart
    adb kill-server
    adb start-server
    * daemon not running. starting it now on port 5037 *
    * daemon started successfully *

```













## activities strings.xml ##


Now and then we need a string from strings.xml - then do

```
    String url = getString(R.string.default_url_post);
```

and not

```
    String url = R.string.default_url_post;
```

which will not type check since R.string.default\_url\_post is represented as an int in the R.java.












# log (cat) #



## write to log ##


within a java class

```
    
    private static final String TAG = <activity name>.class.getName(); 			
    // e.g.:
    private static final String TAG = AndroidHttpClientPostActivity.class.getName(); 			
    // then Log.d / Log.i / Log.e etc...
    Log.d(TAG, "onCreate called");
    
```












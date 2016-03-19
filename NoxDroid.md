# NoxDroid Android App #

Code: https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/NoxDroid/trunk


## NoxDroid on the move ##

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/noxdroid_onthemove_1_cropped.jpg](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/noxdroid_onthemove_1_cropped.jpg)


## Test / debugging ##

todo

### Debug the ioio / noxdroid device ###

Read NOxDROID\_README


### Post to App engine ###

Read NoxDroidCloudEngine

todo: some minor description


### Get raw data from the phone / huawei ideos ###

Requires phone to be rooted and requires a SD Card on the phone.


```

    adb -s <device id/name> shell 

    # tip - ls is your friend 
    ls /data/data/dk.itu.noxdroid/databases
    # should not be empty then continue
    cat /data/data/dk.itu.noxdroid/databases/noxdroid.db > /sdcard/noxdroid.db
    
    # exit twice
    exit
    exit

    # now you can pull database down from sdcard
    adb -s <device id/name> pull /sdcard/noxdroid.db /tmp

```




## Various ##

### How to make a app release ###

```
    
    add NoxDroid/dist/NoxDroid_1_4.apk
    add NoxDroid/dist/NoxDroid_release.apk
    
    # svn tag 
    svn cp https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/NoxDroid/trunk https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/NoxDroid/tags/version_1_4 -m 'version 1.4 fixture'
    
```




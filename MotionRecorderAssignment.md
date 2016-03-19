# App engine - ActivityRecorderCloud #

Code: https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/ActivityRecorderCloud/trunk


## Main url(s) ##

  * http://spct-e2011-lab-motionrecorder.appspot.com/activitynodes

- lists all activity nodes in ascending order - newest recording first - click on an uuid and view one recording block - either in html or arrf

http://spct-e2011-lab-motionrecorder.appspot.com/activity_arff?activityName=81f686d8-b5fb-45a5-969a-e44bbd671267
http://spct-e2011-lab-motionrecorder.appspot.com/activityrecording.jsp?activityName=81f686d8-b5fb-45a5-969a-e44bbd671267

- why is there a form at the end of activityrecording.jsp ? its based roughly on the app engine tutorial so its a left over and a simple way to test the app with out the android app.


# Android app code #


https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/MotionRecorderAndroid/trunk


## screen shots ##
![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-1.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-1.png)

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-2.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-2.png)

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-3.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-3.png)

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-4.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-4.png)

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-5.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/MotionRecorderAndroid-5.png)





## motion recording stops on display sleep ##

on some devices e.g. the huwai ideos (android 2.2)  apparently stop its accelerometer when display is put to sleep - read more:

  * http://stackoverflow.com/questions/2143102/accelerometer-stops-delivering-samples-when-the-screen-is-off-on-droid-nexus-one
  * http://code.google.com/p/android/issues/detail?id=3708

this behavior doesn't appear on the htc desire (android 2.2)


## get database from Android device ##


```
    
    adb -s 404D8E1812B3 shell
    # copy db to sd card - at least required on the huwai devices
    cat /data/data/dk.itu.spct.motionrecorderandroid/databases/motions.db > /sdcard/motions.db
    # exit device - from computer do pull 
    adb -s 404D8E1812B3 pull /sdcard/motions.db /tmp

```


# rm database #


```
    
    adb -s 404D8E1812B3 shell
    su
    rm /data/data/dk.itu.spct.motionrecorderandroid/databases/motions.db

```


# some sqlite query examples #

```
    
    sqlite3 /tmp/motions.db 
    sqlite> select * from motions;
    # pic an uid (e.g.: a9db7906-c692-4629-8a0e-d9d169f6889f) and lookup recording for one motion sequence
    sqlite> select motions.motion_type, motions.uuid, accelerometer.x, accelerometer.y, accelerometer.z, accelerometer.time_stamp from accelerometer, motions where motions.uuid="c7b80dc9-8733-4a7e-81c6-6a0555a82878" and (accelerometer.time_stamp > motions.time_stamp_start and accelerometer.time_stamp < motions.time_stamp_end);
    
```

## add/subtract of 4/5 seconds from start/end time ##

```

    sqlite> select motions.motion_type, motions.uuid, accelerometer.x, accelerometer.y, accelerometer.z, accelerometer.time_stamp from accelerometer, motions where motions.uuid="a9db7906-c692-4629-8a0e-d9d169f6889f" and (accelerometer.time_stamp > datetime(motions.time_stamp_start, '+4 seconds') and accelerometer.time_stamp < datetime(motions.time_stamp_end, '-5 seconds'));

```


## verify a recording ##

```

    // raw
    sqlite> select count(motions.motion_type) from accelerometer, motions where motions.uuid="69666f1b-2c43-4562-a46e-1bdb498dbd96" and (accelerometer.time_stamp > motions.time_stamp_start and accelerometer.time_stamp < motions.time_stamp_end);
    
    // time adjusted - this number should be equal to the recording on the app engine
    sqlite> select count(motions.motion_type) from accelerometer, motions where motions.uuid="69666f1b-2c43-4562-a46e-1bdb498dbd96" and (accelerometer.time_stamp > datetime(motions.time_stamp_start, '+4 seconds') and accelerometer.time_stamp < datetime(motions.time_stamp_end, '-5 seconds'));

    
```



## from unix time to sqlite time ##

```
    sqlite> select datetime(1092941466, 'unixepoch', 'localtime');
    
    // current unix time
    sqlite>  select strftime('%s','now');
    
    // from time stampe stored in db to a unix time stamp
    sqlite> select strftime('%s',time_stamp_end) from motions where uuid="c7b80dc9-8733-4a7e-81c6-6a0555a82878";
        
```


more on sqlite and time (functions) http://www.sqlite.org/cvstrac/wiki?p=DateAndTimeFunctions









# Raw project description #

## MotionRecorderAndroid ##

We have a simple model for a motion

```
--------------
| motion |   |
--------------
| id (uuid)  |
| start time |
| end time   |
--------------

---------------------------
| accelerometer recording |
---------------------------
| time stamp              |
| x                       |
| y                       |
| z                       |
---------------------------
```

When the user starts a recording of a motion it starts an standalone Android service. A service within Android is not bound to the activity (UI) and runs in the background until the user explicitly stops the service through an activity (UI).

Each motion has an unique id and get a time-stamp on start and stop. All accelerometer movements along the x/y/z axis has as well a time-stamp.

For that reason we can get a sequences of accelerometer recordings for each motion recorded.

In the first place we store all data on the relational database, Sqlite3 which is shipped with Android and famous for handling lots of writes.

In the end of a motion recording the user has the possibility to post the motion recording to the Google App Engine (for now just referenced as App Engine). This is handled by a simple http post request.


### Drawback(s) ###
Currently each recording is stored in the Sqlite database on the fly. We where curious how many writes the database was able to do so we stock to this solution.

Each motion is posted separately to the App Engine which should of course have been in one request/post e.g. as JSON.


## ActivityRecorderCloud (App Engine) ##

The ActivityRecorderCloud uses an App Engine to store and present a motion.

We have some servlets to handle that

- One servlet that handles the date sent from the Android application (AddRecordingServlet)
- Multiple serlvets that handles the presentation of a single motion recording (ActivityARFFServlet, ActivityCSVServlet)

and we have a simple JSP view that list all recording (activityrecording.jsp)


The ActivityRecorderCloud is based on a very simple date model:

```
-------------------------
| motion recording node |
-------------------------
| id (uuid)             |
| username              |
| time stamp            |
| x                     |
| y                     |
| z                     |
| start time            |
| end time              |
-------------------------
```

Under the hood we are simply using the low level object oriented datastore of the App Engine which is further more based upon the high replication storage and for that reason are prepared to scale very well.




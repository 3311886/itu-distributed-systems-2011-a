# Introduction #

Hello app for various android services approaches:

```
    
`-- dk
    `-- itu
        `-- spvc
            `-- android
                |-- Main.java
                |-- apidemoexample
                |   |-- LocalService.java
                |   `-- LocalServiceActivities.java
                |-- locationservice
                |   |-- LocationService.java
                |   `-- LocationServiceDB.java
                `-- standaloneservice
                    `-- StandAloneLocalService.java

    
```

  * apidemoexample is from the demo api example code (has example of Controller/Binding feature)
  * locationservice
    * LocationService - has a location listener attached - for trying out basic location take also a look here in AndroidLocationSimple
  * standaloneservice - simple service boiler plate can be started/stopped etc..

## Screen shoot ##

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/HelloService.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/HelloService.png)

Remember to press the menu button to get access to options menu (Start location service etc...)

## Svn location ##

```
   
    svn co https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/HelloService/trunk HelloService

    svn co https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/HelloServiceTest/trunk HelloServiceTest    
    
    
```

Then import both into Eclipse as existing android projects.

Browse code TTW here:

  * http://code.google.com/p/itu-distributed-systems-2011-a/source/browse/pervasive-computing-ad-hoc/HelloService
  * http://code.google.com/p/itu-distributed-systems-2011-a/source/browse/pervasive-computing-ad-hoc/HelloServiceTest








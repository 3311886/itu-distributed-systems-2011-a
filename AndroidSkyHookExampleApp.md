# How to install Skyhook example application #

Code based on the example within the Android Skyhook SDK:

http://www.skyhookwireless.com/developers/sdk.php


svn co (in terminal):

```
cd <eclipse workspace>
svn co https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/WpsApiTest/trunk WpsApiTest
```

import to eclipse:

  * Go to eclipse
  * Select import (from file-menu) and choose General-existing project. Browse to the above location (the new project that you added through terminal).

install on device:

take a look at AndroidAppsDeviceInstall

at device (android)

  * select WpsApiTest
  * and remember to set username and realm under setting!

## screenshots ##
![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/skyhook_example_app_front_page.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/skyhook_example_app_front_page.png)
![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/skyhook_example_app_setting.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/skyhook_example_app_setting.png)

## Java doc ##

To get java doc on the Skyhook api change path in .classpath (or through eclipse)

```
<classpathentry kind="lib" path="libs/wpsapi.jar">
	<attributes>
		<attribute name="javadoc_location" value="file:/Users/pelle/itu/eclipse_workspace_android/WpsApiTest/documentation"/>
	</attributes>
</classpathentry>
```

We tried with
```
<classpathentry kind="lib" path="libs/wpsapi.jar">
	<attributes>
		<attribute name="javadoc_location" value="file:documentation"/>
	</attributes>
</classpathentry>
```

...but that didn't work.
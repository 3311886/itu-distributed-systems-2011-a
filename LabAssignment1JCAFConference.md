#The Alien RFID reader Java library


# How to use and setup the JCAF conference system #


# export project as jar #

save it to as etc. conference.jar

$ 

&lt;eclipse &gt;

/framework

then edit the ContextService/ContextService.bat in your favorite editor
$ emcs ContextService

and add conference.jar to the jar...

note: all this could be done in a nicer way with ant's there is an example in the JCAF source code xxx (todo insert url)


# run the service #

go to the framework folder

$ cd 

&lt;eclipse &gt;

/framework

then start service:
$ ./ContextService testcustom




# if rfid monitor starts not to work etc #

ensure basic rfid is running - read LabRFIDAlienNotes

todo: document the rest




# eclipse additional install #


## GWT ##

tilføj til som update site
  * http://dl.google.com/eclipse/inst/d2wbpro/latest/3.6


http://code.google.com/webtoolkit/download.html

download GWT SDK
http://www.eclipse.org/swt/
download .jar


org.eclipse.swt




gwt (google) = swt ? (eclipse viderudvikling af swing)


new osx go for swt-3.7.1-cocoa-macosx


older macs need 64 bit of



> /System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Users/pelle/itu/eclipse\_workspace/test\_swt/bin:/Users/pelle/itu/eclipse\_workspace/test\_swt/libs/swt-debug\_x86\_64.jar:/Users/pelle/itu/eclipse\_workspace/test\_swt/libs/swt\_x86\_64.jar HelloWorld

raised:

::

> WARNING: Display must be created on main thread due to Cocoa restrictions.


set parameter arguments either in the eclipse execute as java ...

-XstartOnFirstThread

/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Users/pelle/itu/eclipse\_workspace/test\_swt/bin:/Users/pelle/itu/eclipse\_workspace/test\_swt/libs/swt-debug\_x86\_64.jar:/Users/pelle/itu/eclipse\_workspace/test\_swt/libs/swt\_x86\_64.jar -XstartOnFirstThread HelloWorld




ConferenceDisplaySWT


**WARNING: Display must be created on main thread due to Cocoa restrictions.
org.eclipse.swt.SWTException: Invalid thread access
> at org.eclipse.swt.SWT.error(SWT.java:4282)
> at org.eclipse.swt.SWT.error(SWT.java:4197)
> at org.eclipse.swt.SWT.error(SWT.java:4168)
> at org.eclipse.swt.widgets.Display.error(Display.java:1065)
> at org.eclipse.swt.widgets.Display.createDisplay(Display.java:822)
> at org.eclipse.swt.widgets.Display.create(Display.java:805)
> at org.eclipse.swt.graphics.Device.**

&lt;init&gt;

(Device.java:130)
> at org.eclipse.swt.widgets.Display.

&lt;init&gt;

(Display.java:696)
> at org.eclipse.swt.widgets.Display.

&lt;init&gt;

(Display.java:687)
> at org.eclipse.swt.widgets.Display.getDefault(Display.java:1383)
> at assignment1.conference.gui.ConferenceDisplaySWT.open(ConferenceDisplaySWT.java:30)
> at assignment1.conference.gui.ConferenceDisplaySWT.

&lt;init&gt;

(ConferenceDisplaySWT.java:10)
> at assignment1.conference.gui.ConferenceDisplaySWT.main(ConferenceDisplaySWT.java:19)


add argument:

-XstartOnFirstThread

to: <long path and lots of libs etc...>:framework/jcaf.v15.jar -XstartOnFirstThread assignment1.conference.gui.ConferenceDisplaySWT

/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home/bin/java -Dfile.encoding=UTF-8 -classpath /Users/pelle/itu/eclipse\_workspace/pervasive\_computing\_course/bin:/Users/pelle/Applications/eclipse361/plugins/org.junit\_4.8.1.v4\_8\_1\_v20100427-1100/junit.jar:/Users/pelle/Applications/eclipse361/plugins/org.hamcrest.core\_1.1.0.v20090501071000.jar:/Users/pelle/itu/eclipse\_workspace/pervasive\_computing\_course/lib/swt-debug\_x86\_64.jar:/Users/pelle/itu/eclipse\_workspace/pervasive\_computing\_course/lib/swt\_x86\_64.jar:/Users/pelle/itu/eclipse\_workspace/pervasive\_computing\_course/lib/ebwsclient.jar:/Users/pelle/itu/eclipse\_workspace/pervasive\_computing\_course/lib/jetty-util-8.0.0.M0.jar:/Users/pelle/itu/eclipse\_workspace/pervasive\_computing\_course/lib/AlienRFID.jar:/Users/pelle/itu/eclipse\_workspace/pervasive\_computing\_course/framework/jcaf.v15.jar -XstartOnFirstThread assignment1.conference.gui.ConferenceDisplaySWT


















# How to get JCAF java doc shown #

JCAF unfortunately doesn't shipped with java doc

either point java doc to the docs folder of the downloaded JCAF source code or point to the java doc exported to:

http://code.google.com/p/itu-distributed-systems-2011-a/source/browse/pervasive-computing/#pervasive-computing%2Ftrunk%2Fjcaf_javadoc

e.g.:

```
/path-to/jcaf_javadoc
```

then in Java Build Path in Eclipse go to libs and add java doc for the JCAF

![https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/eclipse_get_set_java_doc_on_a_jar_lib.png](https://itu-distributed-systems-2011-a.googlecode.com/svn/wiki/images/eclipse_get_set_java_doc_on_a_jar_lib.png)

or open in your favorite editor:

```
$ emacs <path-to-eclipse-project>/.classpath
```

and the set

```
<classpathentry kind="lib" path="framework/jcaf.v15.jar">
	<attributes>
		<attribute name="javadoc_location" value="file:/Users/pelle/itu/eclipse_workspace/pervasive_computing_course/pervasive-computing/jcaf_javadoc/"/>
	</attributes>
</classpathentry>
```


























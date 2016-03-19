# Introduction #


Send/receive images as bits is required when send/receive images to applications not implementing the java image object ().


There is currently 2 files

- a client ImageTcpClient.java - http://code.google.com/p/itu-distributed-systems-2011-a/source/browse/pervasive-computing-ad-hoc/pervasivecomp2011/trunk/dk/itu/socketprogramming/ImageTcpClient.java

- a server ImageTcpServer.java - http://code.google.com/p/itu-distributed-systems-2011-a/source/browse/pervasive-computing-ad-hoc/pervasivecomp2011/trunk/dk/itu/socketprogramming/ImageTcpServer.java


# Do #

Start server

Run client

Server store received images 'here' <eclipse project path>/dk/itu/socketprogramming/output



# Problem #

For now the byte array / buffer length are manually


```
    
    byte buffer[] = new byte[1024 * 15];
    
```

niece to have : figure out how to this is done right - perhaps

```
    
		 InputStream input = new FileInputStream(image);
		 input.read(b, off, len) // how to do ?
    
```

or send the length as first message sent....


# Where is the code #


TTW:

http://code.google.com/p/itu-distributed-systems-2011-a/source/browse/pervasive-computing-ad-hoc/#pervasive-computing-ad-hoc%2Fpervasivecomp2011%2Ftrunk

Svn (its a full eclipse project but minimal - so import it as prefered):
https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/pervasivecomp2011/trunk
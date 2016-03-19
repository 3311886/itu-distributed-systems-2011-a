#The Alien RFID reader Java library


# How to use the Alien RFID Reader Java library #

Pre-request:

The Alien RFID Reader is already set up.

Momently accessible here (since the data is also posted on the blog we rely on its ok to post it here...):


Start:

**Download the library and add it to the Java project in eclipse**


```

    i'm code :)

```



## print out full RFID data ##

```

for (int i=0; i<tagList.length; i++) {
  Tag tag = tagList[i];
  System.out.println("ID:" + tag.getTagID() +
                     ", Discovered:" + tag.getDiscoverTime() +
                     ", Last Seen:" + tag.getRenewTime() +
                     ", Antenna:" + tag.getAntenna() +
                     ", Reads:" + tag.getRenewCount()
                     );
}
    
```


Tag(s) found:
ID:E200 9037 8904 0121 1620 7040, Discovered:1317193202963, Last Seen:1317193202963, Antenna:0, Reads:1
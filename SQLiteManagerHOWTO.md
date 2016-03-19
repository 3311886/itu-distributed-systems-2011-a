  1. onnect to the phone via ADB (USB or ADB Wireless)
  1. n the console on the computer go to the local directory you want to save the database to.
  1. rite adb pull /[the\_dir\_to\_noxdroid.db\_on\_your\_phone]/noxdroid.db ./[or empty to keep noxdroid.db](rename.md)

Install [Firefox SQLiteManager](https://addons.mozilla.org/en-US/firefox/addon/sqlite-manager/)

Open Firefox SQLiteManager and connect to the downloaded noxdroid database

### Getting NOX from the nox table ###
```
select time_stamp, nox from nox where time_stamp >= (select time_stamp_start from tracks where _id = 'id of the track') and time_stamp  <= (select time_stamp_end from tracks where _id = 'id of the track')
```

Click the **Action** button and choose "Save result to CSV file"

### Getting locations from the locations table ###
```
select time_stamp,location_provider, latitude, longitude from locations where time_stamp >= (select time_stamp_start from tracks where _id = 'id of the track') and time_stamp  <= (select time_stamp_end from tracks where _id = 'id of the track') order by time_stamp asc
```
Click the **Action** button and choose "Save result to CSV file"

### Getting locations above some threshold for nox ###
```
select latitude || '-' || longitude , latitude, longitude from locations where time_stamp >= '2011-12-11 12:21:39' and time_stamp <= '2011-12-11 12:23:21'
```


Open the saved CSV files and remove quotation marks " by search replace.
Add column headers, eg.

_time\_stamp, nox_

_12-12-2011 12:00:00, .65433_
# App engine - NoxDroidCloudEngine #

Code: https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/NoxDroidCloudEngine/trunk

Some general App Engine tricks: AppEngineAdHocTipsAndTricks

## Main url(s) ##

  * http://noxdroidcloudengine.appspot.com
  * http://noxdroidcloudengine.appspot.com/noxdroids_listing (list noxdroid / tracks / location and nox)
  * http://noxdroidcloudengine.appspot.com/add_track_form.html (adds a track to the datastore - when added just return a fresh from - this will of course happen from the NoxDroid Android app) - logic : [AddTrackServlet.java](https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/NoxDroidCloudEngine/trunk/src/dk/itu/noxdroidcloudengine/tracks/AddTrackServlet.java) - todo: add support for nox json etc...


Listings of tracks (recordings) is coming up etc... ('todo')


### Data model ###

'todo'

#### Unit tests ####

  * [https://itu-distributed-systems-2011-a.googlecode.com/svn/pervasive-computing-ad-hoc/NoxDroidCloudEngine/trunk Various example(s) of using the data storage ](.md)



## Queries ##

### Low level storage ###

#### Java ####

In a servlet you can do:

```

    String ancestorParentKeyName = req.getParameter("ancestor_parent_key_name");
    Key parentAncestorKey = KeyFactory.createKey("NoxDroid", ancestorParentKeyName);
    // prints: NoxDroid()

    // Get ancestorKeyName from request
    // and create the key for the query
    String ancestorKeyName = req.getParameter("ancestor_key_name");
    Key ancestorKey = KeyFactory.createKey(parentAncestorKey, "Track", ancestorKeyName);
    System.out.println("ancestorKey: " + ancestorKey);
    System.out.println("ancestorKey.getParent(): " + ancestorKey.getParent());    	


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query q = new Query("Location");

    if (setProviderFiler)
    q.addFilter("provider", Query.FilterOperator.EQUAL, providerSetting);

    q.setAncestor(ancestorKey);
    //        q.addSort("start_time", Query.SortDirection.DESCENDING);

    PreparedQuery pq = datastore.prepare(q);

```

when introspecting this in the debugger e.g. "PreparedQuery pq" - we get a GQL query looking like

```
    
    SELECT * FROM Location WHERE __ancestor__ is NoxDroid("test_user_name")/Track("eeb445dc-d2eb-494f-a4af-78c20b5d181c")
    
```

but its apparently not possible to paste this into the GQL query form at the app engine - weird - look further down under GQL...

any way to iterate through the result and eventually print to the request... do:


```
    
    QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
    for (Entity entity : results) {

    	// eventually get child from entity:
    	entity.getKey().getChild("Location", id)
        
        // get property from entity
        entity.getProperty("latitude")
    
```


#### GQL ####

Remember to use single quotes in the "By GQL:" field e.g.:

```

```


In general don't do:

```
    select * 
```

unless you need all the entity properties do:

```
    select __key__
```

this is more cpu friendly and less cost expensive on the read side.



some ancestor examples:

```

    // 'works':
    // get all tracks / locations / noxs of test_user_id including test_user_name
    SELECT __key__ WHERE ANCESTOR IS KEY('NoxDroid', 'test_user_name')
    
    // lets step further into the model
    // works - get locations / noxs of track eeb445dc-d2eb-494f-a4af-78c20b5d181c including track
    SELECT __key__ WHERE ANCESTOR IS KEY('NoxDroid', 'test_user_name', 'Track', '8c3adc99-3e51-4922-a3c9-d127117bb764')


    // doesn't work
    SELECT __key__ WHERE ANCESTOR IS NoxDroid('test_user_name')
    // NoxDroid('test_user_name') is the key name when looking it up in java/servlet code
    // also doesn't work (this one is copy pasted from the java print out GQL)
    SELECT __key__ FROM Nox WHERE __ancestor__ is KEY('NoxDroid', 'test_user_name', 'Track', '8c3adc99-3e51-4922-a3c9-d127117bb764')
    
    // this is equivalent to a key printed out in a entity with java 
    // <Entity [NoxDroid("test_user_name")/Track("8c3adc99-3e51-4922-a3c9-d127117bb764")]:
    // 	start_time = 2011-12-04 09:10:04
    //     >

```

so its probably not possible in GL to query on both model (e.g. Nox) and ancestor though this approach is used in the java based query language... weird.

naive geo try outs:

```
    // not working
    SELECT * FROM Location where geopoint is Geopt(lat=55.6599197388, lon=12.5911903381)
    SELECT * FROM Location where geo_point = Geopt(55.6599197388,12.5911903381)
```

date time:

```
    
    SELECT * FROM Location where time_stamp > DATETIME('2011-12-05 22:16:22')
    // time_stamp is stored as string - long story short
    
    // time_stamp_date stored as date type - but both these queries give identical result
    SELECT * FROM Location where time_stamp_date > DATETIME('2011-12-05 22:16:22')    
        
```
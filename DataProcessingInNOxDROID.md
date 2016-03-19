# Introduction #

Analyzing sensor data in Weka

Preprocessing of location and nox data
  * Cleaning GPS and nox data

Processing of data
  * Aligning nox and location data


## Analyzing sensor data in Weka ##
Load the data from either appengine or directly from the phone see [SQLiteManagerHOWTO](SQLiteManagerHOWTO.md)

Open WEKA and load the file, see [DataProcessingInWeka](DataProcessingInWeka.md)

Look at the mean, stddev, min and max.

## Preprocessing of location and nox data ##
Here is some pseudo code for the algorithm which should process the data for a single track

```

generateTrackKML(T) :
  G : set of locationdata of some track T
  if |G| == 0 return
  g0 = G[0]
  for g1 in G[1:] : 
     t1 = g0.time_stamp
     if (t1 == null)
        g0 = g1
        continue
     t2 = g1.time_stamp
     if (t2 == null) 
        continue
     N = set of nox data where time of n, nt :  t1 <= nt <= t2
     if |N| == 0 : 
        continue
     

     Insert |N|-2 gps points between g0 and g1 to geotag each nox reading
     for n in N : 
        if n is first : create Placemark
        if level(n) !=  level(n-1) || n is last point 
           add coordinates to placemark
           if index of n is less than |N|-1 
               update noxlevel
     

  

  

CreateSingleTrackKML(N, G)
   N : set of noxdata of some track T
   G : set of locationdata of some track T
   K : empty set of KML datapoints 

   for (gx,gy) in G : 
     N = set of nox between gx_t and gy_t (_t is timestamp of gx)
     L = ContractNOX(N)
     E.add(gx)
     E = L.size-1 of extrapolated GPS points between gx and gy 
     for i..L.size : 
        KML.add((L[i].timestamp,L[i].timestamp, E[i]))
   return K

ContractNOX(N)
   M : emptyset for noxdata
   for n in N : 
      c <- contracted point n0, n1, ni where |nh-ng| < some delta 
      M.add(c) 
   return M
```

This should be done when the data is uploaded and saved in a table:

timestamp, latitude, longitude, geopoint, nox

Several interesting things to do when we want to show multiple tracks.
First of all we need to do the dataprocessing in real time. The pollution level should be visualized according to selected timespan.

  * Show tracks by a user. Ability to select/unselect individual tracks.
  * Show pollution on the map with timespan selection

Should all KML datapoints be calculated at once or can you pass the map's quadrant as arguments.

```
Save KMLPoint as an Entity extending GeoModel. 
Divide "Copenhagen" into geocells
G = {g1, g2,...,gi}, where g1 consists of KMLPoints k (with limit) where k_timestamp >= datetime_start && k_timestamp <= datetime_end and k is in geocell gc

for each g1, g2,...,gi : 
   Refine resolution, [45,-90], [45, 90], [-45,-90],[-45,90] proximity searches for {
   Lg = # Green(KMLPoints)
   Ly = # Yellow(KMLPoints)
   Lr = # Red(KMLPoints)
}
   If Lg, Ly, Lr are close increase the resolution of the geocell.
   Else Get the dominant KMLPoints in the geocell -> Color the geocell accordingly by drawing a polygon consisting of a coordinates furtherst from the center.
```

```

generateTrackKML(T) :
  G : set of locationdata of some track T
  if |G| == 0 return
  g0 = G[0]
  for g1 in G[1:] : 
     t1 = g0.time_stamp
     t2 = g1.time_stamp
     N = set of nox data where time of n, nt :  t1 <= nt <= t2

     noxvalue = null
     List pm_list // List of placemarks
     Placemark pm
     List c // List of gps coordinates
     I : set of size |N|-1 of interpolated gps points between g0 and g1 
     for i in I : 
        if i is first : 
           pm = new Placemark
           nox = N[i.index]
           c.add(i.gpspoint)
        elif nox.noxlevel !=  noxvalue : 
           c.add(i.gpspoint)
           pm.add(c)
           if index of i.index < |I|-1 
              noxvalue = nox.noxlevel
              c = new List
              c.add(i.gpspoint)
              pm_list.add(pm)
              pm = new Placemark
        else : 
           c.add(i.gpspoint)
           pm.add(c)
           if i is last :
              pm_list.add(pm)
      g0 = g1
   return pm_list
```

http://code.google.com/p/javageomodel/
http://code.google.com/apis/maps/articles/geospatial.html
http://code.google.com/p/geomodel/source/browse/trunk/demos/pubschools
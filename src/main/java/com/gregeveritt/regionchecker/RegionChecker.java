/**
 * The following has been ported to Java from C# and modified to work with the LatLng type
 * privided by Google Maps. Original code from Matt R on Stack Overflow
 * http://stackoverflow.com/questions/18486284/android-geofencing-polygon
 *
 * Copyright 2014 Gregory R. Everitt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Gregory R. Everitt, Matthew Robbins
 * @version 0.5
 */
package com.gregeveritt.regionchecker;

import com.google.android.gms.maps.model.LatLng;

public class RegionChecker {
    public static boolean PointIsInRegion(double x, double y, LatLng[] region)
    {
        int crossings = 0;

        LatLng point = new LatLng (x, y);
        int count = region.length;
        // for each edge
        for (int i=0; i < count; i++) 
        {
           LatLng a = region [i];
            int j = i + 1;
            if (j >= count) 
            {
                j = 0;
            }
            LatLng b = region [j];
            if (RayCrossesSegment(point, a, b)) 
            {
                crossings++;
            }
        }
        // odd number of crossings?
        return (crossings % 2 == 1);
    }

    private static boolean RayCrossesSegment(LatLng point, LatLng a, LatLng b)
    {
        double px = point.longitude;
        double py = point.latitude;
        double ax = a.longitude;
        double ay = a.latitude;
        double bx = b.longitude;
        double by = b.latitude;
        if (ay > by)
        {
            ax = b.longitude;
            ay = b.latitude;
            bx = a.longitude;
            by = a.latitude;
        }
            // alter longitude to cater to 180 degree crossings
        if (px < 0) { px += 360; }
        if (ax < 0) { ax += 360; }
        if (bx < 0) { bx += 360; }

        if (py == ay || py == by) py += 0.00000001;
        if ((py > by || py < ay) || (px > Math.max(ax, bx))) return false;
        if (px < Math.min(ax, bx)) return true;

        double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Double.MAX_VALUE;
        double blue = (ax != px) ? ((py - ay) / (px - ax)) : Double.MAX_VALUE;
        return (blue >= red);
    }
}

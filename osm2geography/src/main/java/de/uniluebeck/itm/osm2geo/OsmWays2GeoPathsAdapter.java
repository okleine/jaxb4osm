/**
 * Copyright (c) 2014, Oliver Kleine, Institute of Telematics, University of Luebeck
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *  - Redistributions of source messageCode must retain the above copyright notice, this list of conditions and the following
 *    disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *  - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 *    products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uniluebeck.itm.osm2geo;


import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.Point;
import de.uniluebeck.itm.jaxb4osm.tools.OsmUnmarshaller;
import de.uniluebeck.itm.jaxb4osm.tools.WayElementFilter;
import de.uniluebeck.itm.jaxb4osm.elements.NodeElement;
import de.uniluebeck.itm.jaxb4osm.elements.OsmElement;
import de.uniluebeck.itm.jaxb4osm.elements.WayElement;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by olli on 13.06.14.
 */
public class OsmWays2GeoPathsAdapter {

    private OsmElement osmElement;

    public OsmWays2GeoPathsAdapter(File osmFile, WayElementFilter filter) throws Exception{
        FileInputStream fileInputStream = new FileInputStream(osmFile);
        this.osmElement = OsmUnmarshaller.unmarshal(fileInputStream, filter, true);
    }


    public OsmWays2GeoPathsAdapter(OsmElement osmElement){
        this.osmElement = osmElement;
    }


    private static Point toPoint(NodeElement nodeElement){
        return new Point(
                new DegreeCoordinate(nodeElement.getLatitude()), new DegreeCoordinate(nodeElement.getLongitude())
        );
    }

    /**
     * Returns a {@link java.util.Map} containing the polygons (resp. a {@link java.util.List}s containing the
     * respective polygons corners) as values and strings as IDs. The IDs have the form "<prefix>-<postfix>". The
     * prefix is taken from the ID attribute of the according <way> element from the OSM file whereas the postfix is
     * an ascending number (counter) per prefix.
     *
     * @param taper <code>true</code> if the returned polygons should have tapered ends (like an arrow) or
     *              <code>false</code> for flat ends
     *
     * @return a {@link java.util.Map} containing the polygons (resp. a {@link java.util.List}s containing the
     * respective polygons corners) as values and strings as IDs.
     */
    public Multimap<String, List<Point>> createGeoPolygons(boolean taper, boolean splitWays){

        Map<String, GeoPath> geoPaths = this.createGeoPaths(splitWays);

        return this.createGeoPolygons(geoPaths, taper);
    }


    /**
     * Returns a {@link java.util.Map} containing the polygons (resp. a {@link java.util.List}s containing the
     * respective polygons corners) as values and strings as IDs. The IDs have the form "<prefix>-<postfix>". The
     * prefix is taken from the ID attribute of the according <way> element from the OSM file whereas the postfix is
     * an ascending number (counter) per prefix.
     *
     * @param geoPaths the {@link de.uniluebeck.itm.osm2geo.GeoPath}s to create the polygons for
     * @param taper <code>true</code> if the returned polygons should have tapered ends (like an arrow) or
     *              <code>false</code> for flat ends
     *
     * @return a {@link java.util.Map} containing the polygons (resp. a {@link java.util.List}s containing the
     * respective polygons corners) as values and strings as IDs.
     */
    public Multimap<String, List<Point>> createGeoPolygons(Map<String, GeoPath> geoPaths, boolean taper){

        LinkedHashMultimap<String, List<Point>> result = LinkedHashMultimap.create();

        for(Map.Entry<String, GeoPath> entry : geoPaths.entrySet()){
            List<List<Point>> polygons = entry.getValue().getPolygonCorners(taper);
            for(int i = 0; i < polygons.size(); i++){
                result.put(entry.getKey() + "-" + i, polygons.get(i));
            }
        }

        return result;
    }


    /**
     * Creates a {@link java.util.Map} with IDs based on {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement#getID()}
     * as key and a {@link de.uniluebeck.itm.osm2geo.GeoPath} as value.
     *
     * @param splitWays <code>true</code> if each {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement} is to be
     *                  split at crossings or <code>false</code> otherwise
     *
     * @return a {@link java.util.Map} with IDs based on {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement#getID()}
     * as key and a {@link de.uniluebeck.itm.osm2geo.GeoPath} as value
     */
    public Map<String, GeoPath> createGeoPaths(boolean splitWays){
        Map<String, GeoPath> result = new HashMap<>();

        for(WayElement wayElement : this.osmElement.getWayElements()){
            List<Point> points = new ArrayList<>();
            for(int i = 0; i < wayElement.getNdElements().size(); i++){
                long nodeID = wayElement.getNdElements().get(i).getReference();
                points.add(toPoint(this.osmElement.getNodeElement(nodeID)));

                if((splitWays && osmElement.getReferencingWayIDs(nodeID).size() > 1) ||
                        nodeID == wayElement.getLastNdElement().getReference()){

                    result.put(
                            wayElement.getID() + "-" + i,
                            new GeoPath(points, wayElement.getTagValue("name"), wayElement.isOneWay())
                    );

                    points = new ArrayList<>();
                    points.add(toPoint(this.osmElement.getNodeElement(nodeID)));
                }
            }
        }

        return result;
    }
}

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
package de.uniluebeck.itm.osm2slse;

import com.google.common.collect.Multimap;
import com.grum.geocalc.Point;
import de.uniluebeck.itm.jaxb4osm.tools.WayElementFilter;
import de.uniluebeck.itm.jaxb4osm.elements.OsmElement;
import de.uniluebeck.itm.osm2geo.OsmWays2GeoPathsAdapter;
import de.uniluebeck.itm.xsd.slse.jaxb.*;
import de.uniluebeck.itm.xsd.slse.jaxb.SemanticEntity;
import de.uniluebeck.itm.xsd.slse.tools.SemanticEntityMarshaller;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by olli on 13.06.14.
 */
public class OsmWays2SlseAdapter extends OsmWays2GeoPathsAdapter{

    private static final StringBuilder formatString = new StringBuilder();
    static{
        formatString.append("PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n")
                    .append("PREFIX geof: <http://www.opengis.net/def/geosparql/function/>\n\n")
                    .append("SELECT ?what\n")
                    .append("WHERE {\n")
                    .append("\t?what geo:hasGeometry ?geometry .\n")
                    .append("\t?geometry geo:asWKT ?wkt .\n\n")
                    .append("\tFILTER(geof:within(?wkt, \"POLYGON((\n")
                    .append("\t\t%s\n")
                    .append("\t))\"^^geo:wktLiteral))\n")
                    .append("}");
    }


    public OsmWays2SlseAdapter(File osmFile, WayElementFilter filter) throws Exception {
        super(osmFile, filter);
    }

    public OsmWays2SlseAdapter(OsmElement osmElement) {
        super(osmElement);
    }


    public Map<String, String> getSparqlQueries() throws Exception{
        Map<String, String> result = new HashMap<String, String>();

        Multimap<String, List<Point>> polygons = createGeoPolygons(false, true);
        for(String key : polygons.keySet()){
            List<List<Point>> polygonsForKey = new ArrayList<List<Point>>(polygons.get(key));
            for(int i = 0; i < polygonsForKey.size(); i++){
                StringBuilder polygonString = new StringBuilder();

                for (Point point : polygonsForKey.get(i)) {
                    polygonString.append(point.getLatitude()).append(" ").append(point.getLongitude()).append(", ");
                }

//                //Add first point also as last point to close the shape
//                Point point = polygonsForKey.get(i).get(0);
//                polygonString.append(point.getLatitude()).append(" ").append(point.getLongitude());

                result.put(key + "-" + i, String.format(formatString.toString(), polygonString.toString()));
            }
        }

        return result;
    }


    public void writeXMLToFile(Map<String, String> sparqlQueries, File xmlFile) throws Exception {
        SemanticEntityList slseList = new SemanticEntityList();

        for(Map.Entry<String, String> entry : sparqlQueries.entrySet()){
            SemanticEntity slse = new SemanticEntity();
            slse.setUriPath(new SemanticEntity.UriPath(entry.getKey()));
            slse.setSparqlQuery(new SemanticEntity.SparqlQuery(entry.getValue()));
            slseList.getEntities().add(slse);
        }

        SemanticEntityMarshaller.marshal(slseList, new FileOutputStream(xmlFile));
    }


    public static void main(String[] args) throws Exception {

        final String MAP_NAME = "dankwartsgrube";
//        final String MAP_NAME = "luebeck-altstadt";

        long start = System.currentTimeMillis();

        //Unmarshall OSM file
        URL osmFileUrl = OsmWays2SlseAdapter.class.getResource("/maps/" + MAP_NAME + "/map.osm");
        File osmFile = new File(osmFileUrl.toURI());

        WayElementFilter wayFilter = WayElementFilter.STREETS;
        OsmWays2SlseAdapter adapter = new OsmWays2SlseAdapter(osmFile, wayFilter);

        Map<String, String> sparqlQueries = adapter.getSparqlQueries();

        File xmlFile = File.createTempFile("slse-list", ".xml");
        adapter.writeXMLToFile(sparqlQueries, xmlFile);


        System.out.println("Created " + sparqlQueries.size() + " SPARQL queries in file " + xmlFile.getAbsolutePath());
        System.out.println("Time passed: " + (System.currentTimeMillis() - start));

        Desktop.getDesktop().browse(xmlFile.toURI());

//        for(WayElement way : adapter.ways.values()){
//            System.out.println(way);
//        }

    }

}

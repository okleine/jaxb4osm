package de.uniluebeck.itm.osm2googlemaps;

import com.google.common.collect.Multimap;
import com.grum.geocalc.Point;
import de.uniluebeck.itm.jaxb4osm.WayElementFilter;
import de.uniluebeck.itm.jaxb4osm.elements.OsmElement;
import de.uniluebeck.itm.osm2geo.OsmWays2GeoPathsAdapter;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.List;

/**
 *
 * @author Oliver Kleine
 */
public class OsmWays2GoogleMapsPolygonsAdapter extends OsmWays2GeoPathsAdapter {

    private static final String htmlFormatString = "" +
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>" +
            "<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\n" +
            "<meta charset=\"utf-8\">\n" +
            "<title>%s</title>\n" +
            "<style> html, body, #map-canvas {height: 100%%; margin: 0px; padding: 0px}</style>\n" +
            "<script src=\"https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false\"></script>\n" +
            "<script src=\"%s\"></script>\n" +
            "<script>google.maps.event.addDomListener(window, 'load', initialize);</script>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"map-canvas\"></div>\n" +
            "</body>\n" +
            "</html>";


    /**
     * Creates a new instance of {@link de.uniluebeck.itm.osm2googlemaps.OsmWays2GoogleMapsPolygonsAdapter}
     *
     * @param osmFile the OSM file to be de-serialized
     * @param filter the {@link de.uniluebeck.itm.jaxb4osm.WayElementFilter}
     *
     * @throws Exception if some error occurred
     */
    public OsmWays2GoogleMapsPolygonsAdapter(File osmFile, WayElementFilter filter) throws Exception{
        super(osmFile, filter);
    }


    /**
     * Creates a new instance of {@link de.uniluebeck.itm.osm2googlemaps.OsmWays2GoogleMapsPolygonsAdapter}
     * @param osmElement the {@link de.uniluebeck.itm.jaxb4osm.elements.OsmElement} to get the data from
     */
    public OsmWays2GoogleMapsPolygonsAdapter(OsmElement osmElement){
        super(osmElement);
    }


    /**
     * Writes the JavaScript data into the given file (which must already exist!)
     *
     * @param jsFile the file to write the JavaScript data in
     * @param zoomLevel the initial zoom level
     *
     * @throws Exception if some error occurred
     */
    public void createJSGMFile(File jsFile, int zoomLevel) throws Exception {

        Multimap<String, List<Point>> polygons = this.createGeoPolygons(true, true);

        Point center = polygons.get(polygons.keySet().iterator().next()).iterator().next().get(0);

        BufferedWriter writer = new BufferedWriter(new FileWriter(jsFile));
        writer.write("function initialize() {\n" +
                "    var mapOptions = {\n" +
                "        zoom: " + zoomLevel + ",\n" +
                "        center: new google.maps.LatLng("+center.getLatitude() + "," + center.getLongitude()+"),\n" +
                "        mapTypeId: google.maps.MapTypeId.ROADMAP\n" +
                "    };\n" +
                "\n" +
                "    var map = new google.maps.Map(document.getElementById('map-canvas'),\n" +
                "        mapOptions);\n\n");

        int[] colorValues = new int[]{0x3CB371, 0xFF4500};
        for(String pathID : polygons.keySet()){
            int index = 0;

            for(List<Point> polygon : polygons.get(pathID)){
                writer.write("\n\n" + toGoogleMapsPolygonString(
                        pathID + "-" + index, polygon, Integer.toHexString(colorValues[index++ % 2]))
                );
            }
        }

        writer.append("\n\n}");
        writer.flush();
        writer.close();
    }


    public static void openPolygonFileInBrower(String title, File jsFile) throws IOException {

        String htmlString = String.format(htmlFormatString, title, jsFile.toURI());
        File tmpHtmlFile = File.createTempFile("polygons", ".html");
        tmpHtmlFile.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new FileWriter(tmpHtmlFile));
        writer.write(htmlString);
        writer.flush();
        writer.close();

        Desktop.getDesktop().browse(tmpHtmlFile.toURI());
    }

    private static String toGoogleMapsPolygonString(String polygonID, List<Point> points, String color){

        final String polygonName = "polygon" + polygonID.replace("#", "").replace("-","_");
        StringBuilder result = new StringBuilder("var " + polygonName  + "Coordinates" + " = [");

        for (Point coordinate : points) {
            result.append("\n\t\tnew google.maps.LatLng(")
                  .append(coordinate.getLatitude())
                  .append(",")
                  .append(coordinate.getLongitude())
                  .append("),");
        }

        result.append("\n\t];")
                .append("\nvar ").append(polygonName).append(" = new google.maps.Polygon({")
                .append("paths:  ").append(polygonName).append("Coordinates,\n")

                //Color stuff...
                .append("    strokeColor: '").append(color).append("',\n").append("    strokeOpacity: 0.8,\n")
                .append("    strokeWeight: 2,\n").append("    fillColor: '").append(color).append("',\n")
                .append("    fillOpacity: 0.35\n")
                .append("  });\n")

                .append(polygonName)
                .append(".setMap(map);");

        return result.toString();
    }



    public static void main(String[] args) throws Exception{
//        final String MAP_NAME = "dankwartsgrube";
        final String MAP_NAME = "luebeck-altstadt";

        long start = System.currentTimeMillis();

        //Unmarshall OSM file
        URL osmFileUrl = OsmWays2GoogleMapsPolygonsAdapter.class.getResource("/maps/" + MAP_NAME + "/map.osm");
        File osmFile = new File(osmFileUrl.toURI());

        WayElementFilter wayFilter = WayElementFilter.STREETS;
        OsmWays2GoogleMapsPolygonsAdapter adapter = new OsmWays2GoogleMapsPolygonsAdapter(osmFile, wayFilter);

        //Write JavaScript file
        File jsFile = File.createTempFile("map", ".osm.js");
        jsFile.deleteOnExit();
        adapter.createJSGMFile(jsFile, 18);

        openPolygonFileInBrower(MAP_NAME, jsFile);

        System.out.println("Time passed: " + (System.currentTimeMillis() - start));
        System.out.print("Press ENTER to exit... (Deletes temporarily created files!)");
        System.in.read();
    }
}

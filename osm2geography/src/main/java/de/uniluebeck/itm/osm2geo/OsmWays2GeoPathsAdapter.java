package de.uniluebeck.itm.osm2geo;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.grum.geocalc.DegreeCoordinate;
import com.grum.geocalc.Point;
import de.uniluebeck.itm.jaxb4osm.OsmUnmarshaller;
import de.uniluebeck.itm.jaxb4osm.WayElementFilter;
import de.uniluebeck.itm.jaxb4osm.elements.NodeElement;
import de.uniluebeck.itm.jaxb4osm.elements.OsmElement;
import de.uniluebeck.itm.jaxb4osm.elements.WayElement;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by olli on 13.06.14.
 */
public class OsmWays2GeoPathsAdapter {

    private Map<Long, NodeElement> nodes;
    private Map<Long, WayElement> ways;


    public OsmWays2GeoPathsAdapter(File osmFile, WayElementFilter filter) throws Exception{
        OsmUnmarshaller osmUnmarshaller = OsmUnmarshaller.getInstance();
        OsmElement osmElement = osmUnmarshaller.deserialize(osmFile, filter, true);

        this.nodes = osmElement.getNodeElements();
        this.ways = osmElement.getWayElements();
    }


    public OsmWays2GeoPathsAdapter(OsmElement osmElement){
        this.nodes = osmElement.getNodeElements();
        this.ways = osmElement.getWayElements();
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
     *
     * @throws Exception if some error occurred
     */
    public Multimap<String, List<Point>> createGeoPolygons(boolean taper, boolean splitWays) throws Exception{

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
     *
     * @throws Exception if some error occurred
     */
    public Multimap<String, List<Point>> createGeoPolygons(Map<String, GeoPath> geoPaths, boolean taper){

        Multimap<String, List<Point>> result = LinkedHashMultimap.create();

        for(Map.Entry<String, GeoPath> entry : geoPaths.entrySet()){
            List<List<Point>> polygons = entry.getValue().getPolygonCorners(taper);
            for(int i = 0; i < polygons.size(); i++){
                result.put(entry.getKey() + "-" + i, polygons.get(i));
            }
        }

        return result;
    }


    /**
     *
     * @param splitWays
     * @return
     */
    public Map<String, GeoPath> createGeoPaths(boolean splitWays){
        Map<String, GeoPath> result = new HashMap<String, GeoPath>();

        for(WayElement wayElement : this.ways.values()){
            List<Point> points = new ArrayList<Point>();
            for(int i = 0; i < wayElement.getReferencedNodeIDs().size(); i++){
                long nodeID = wayElement.getReferencedNodeIDs().get(i);
                points.add(toPoint(this.nodes.get(nodeID)));

                if((splitWays && this.nodes.get(nodeID).getReferencingWays().size() > 1) ||
                        nodeID == wayElement.getLastNodeID()){

                    result.put(
                            wayElement.getID() + "-" + i,
                            new GeoPath(points, wayElement.getTagValue("name"), wayElement.isOneWay())
                    );

                    points = points.subList(points.size() - 1, points.size());
                }
            }
        }

        return result;
    }
}

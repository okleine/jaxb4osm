package de.uniluebeck.itm.jaxb4osm.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

/**
 * JAXB compliant class for the node element in OSM files (currently, only the attributes <code>id</code>,
 * <code>latitude</code>, and <code>longitude</code> are supported.
 *
 * @author Oliver Kleine
 */
@XmlJavaTypeAdapter(NodeElement.OsmNodeElementAdapter.class)
public class NodeElement extends AbstractAdaptedLevel2Element {

    private static Logger log = LoggerFactory.getLogger(NodeElement.class.getName());

    private double latitude;
    private double longitude;

    private Set<Long> referencingWays;

    private NodeElement(PlainNodeElement plainNodeElement){
        super(plainNodeElement);

        this.latitude = plainNodeElement.getLatitude();
        this.longitude = plainNodeElement.getLongitude();

        this.referencingWays = new HashSet<Long>();
        log.debug("Instance of {} created!", this.getClass().getName());
    }


    /**
     * Returns the value of the attribute "latitude" (e.g. <code>50.1234</code> if the node element was like
     * <code><node ... latitude="50.1234" ...></code>).
     *
     * @return the value of the attribute "latitude"
     */
    public double getLatitude(){
        return this.latitude;
    }

    /**
     * Returns the value of the attribute "longitude" (e.g. <code>10.987</code> if the node element was like
     * <code><node ... longitude="10.987" ...></code>).
     *
     * @return the value of the attribute "latitude"
     */
    public double getLongitude(){
        return this.longitude;
    }


    public Set<Long> getReferencingWays(){
        return this.referencingWays;
    }


    void addRefereningWay(Long wayID){
        this.referencingWays.add(wayID);
    }


    /**
     * Returns a {@link String} representation of this {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement}.
     *
     * @return a {@link String} representation of this {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement}.
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("osm:node (ID: ").append(this.getID()).append(", Latitude: ").append(this.latitude)
              .append(", Longitude: ").append(this.longitude).append(", Tags: [");

        Iterator<Map.Entry<String, String>> tagIterator = this.getTags().entrySet().iterator();
        while(tagIterator.hasNext()){
            Map.Entry<String, String> entry = tagIterator.next();
            result.append("k=\"").append(entry.getKey()).append("\", v=\"").append(entry.getValue()).append("\"");

            if(tagIterator.hasNext()){
                result.append(" | ");
            }
        }

        result.append("])");

        return result.toString();
    }


    public static class PlainNodeElement extends AbstractPlainLevel2Element {

        @XmlAttribute(name = "lat")
        private double latitude;

        @XmlAttribute(name = "lon")
        private double longitude;

        private PlainNodeElement(){}

        private PlainNodeElement(NodeElement nodeElement){
            super(nodeElement);
        }

        private double getLatitude(){
            return this.latitude;
        }

        private double getLongitude(){
            return this.longitude;
        }
    }


    public static class OsmNodeElementAdapter extends XmlAdapter<PlainNodeElement, NodeElement>{

        public OsmNodeElementAdapter(){
            log.debug("Instance of {} created!", this.getClass().getName());
        }

        @Override
        public NodeElement unmarshal(PlainNodeElement plainNodeElement) throws Exception {
            return new NodeElement(plainNodeElement);
        }

        @Override
        public PlainNodeElement marshal(NodeElement nodeElement) throws Exception {
            return new PlainNodeElement(nodeElement);
        }
    }
}

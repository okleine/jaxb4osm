package de.uniluebeck.itm.jaxb4osm.elements;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.*;

/**
 * JAXB compliant class for the way element in OSM files (from OpenStreetMap)
 *
 * @author Oliver Kleine
 */
@XmlJavaTypeAdapter(WayElement.WayElementAdapter.class)
public class WayElement extends AbstractAdaptedLevel2Element {

    private static Logger log = LoggerFactory.getLogger(WayElement.class.getName());

    private static final Multimap<String, String> ONEWAY_CRITERIA = HashMultimap.create();
    static{
        ONEWAY_CRITERIA.putAll("highway", Arrays.asList("motorway", "motorway_link", "trunk_link", "primary_link"));
        ONEWAY_CRITERIA.put("junction", "yes");
    }

    private List<Long> referencedNodeIDs;

    /**
     * Creates a new instance of {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement}.
     */
    private WayElement(PlainWayElement plainWayElement){
        super(plainWayElement);
        this.referencedNodeIDs = new ArrayList<Long>();

        for(NdElement ndElement : plainWayElement.getNdElements()){
            this.referencedNodeIDs.add(ndElement.getReferencedNodeID());
        }
    }

    public long getFirstNodeID(){
        return this.referencedNodeIDs.get(0);
    }


    public long getLastNodeID(){
        return this.referencedNodeIDs.get(this.referencedNodeIDs.size() - 1);
    }

    public boolean isOneWay(){
        String tagValue = this.getTagValue("oneway");
        if("no".equals(tagValue) || "0".equals(tagValue) || "false".equals(tagValue))
            return false;

        if("yes".equals(tagValue) || "1".equals(tagValue) || "-1".equals(tagValue) || "true".equals(tagValue))
            return true;

        for(String key : ONEWAY_CRITERIA.keySet()){
            String value = this.getTagValue(key);
            if(ONEWAY_CRITERIA.get(key).contains(value))
                return true;
        }

        return false;
    }

    /**
     * Returns a {@link java.util.List} containing the IDs of the referenced nodes in
     * order of the appearance of the corresponding elements (<code><nd ... /></code>) in the OSM file.
     *
     * @return a {@link java.util.LinkedList} containing the IDs of the referenced nodes in
     * order of the appearance of the corresponding elements (<code><nd ... /></code>) in the OSM file.
     */
    public List<Long> getReferencedNodeIDs() {
        return this.referencedNodeIDs;
    }


    /**
     * Returns a {@link String} representation of this {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement}.
     *
     * @return a {@link String} representation of this {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement}.
     */
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("osm:way (ID: ").append(this.getID()).append(", referenced nodes: [");

        Iterator<Long> iterator = this.getReferencedNodeIDs().iterator();
        while(iterator.hasNext()){
            result.append(iterator.next());

            if(iterator.hasNext())
                result.append(", ");
        }
        result.append("], Tags: [");

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


    private static class NdElement{

        @XmlAttribute(name = "ref")
        private long referencedNodeID;

        private long getReferencedNodeID(){
            return this.referencedNodeID;
        }
    }


    public static class PlainWayElement extends AbstractPlainLevel2Element{

        @XmlElement(name="nd", type = NdElement.class)
        private List<NdElement> ndElements;

        //for JAXB unmarshalling
        private PlainWayElement(){}

        //for JAXB marshalling
        private PlainWayElement(WayElement wayElement){
            super(wayElement);
            this.ndElements = new ArrayList<NdElement>();
        }

        private List<NdElement> getNdElements(){
            return this.ndElements;
        }
    }


    public static class WayElementAdapter extends XmlAdapter<PlainWayElement, WayElement>{

        @Override
        public WayElement unmarshal(PlainWayElement plainWayElement) throws Exception {
            return new WayElement(plainWayElement);

        }

        @Override
        public PlainWayElement marshal(WayElement wayElement) throws Exception {
            return new PlainWayElement(wayElement);
        }
    }

}
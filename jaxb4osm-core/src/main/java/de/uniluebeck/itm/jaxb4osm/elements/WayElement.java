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
        this.referencedNodeIDs = new ArrayList<>();

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

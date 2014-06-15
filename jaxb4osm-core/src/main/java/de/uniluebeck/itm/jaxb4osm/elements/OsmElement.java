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

import de.uniluebeck.itm.jaxb4osm.tools.WayElementFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;

/**
 * JAXB compliant class for the osm (root) element in OSM files (Open Street Map)
 *
 * @author Oliver Kleine
 */

public class OsmElement {
    
    public static final String ATT_VERSION = "version";
    public static final String ATT_GENERATOR = "generator";
    public static final String ATT_COPYRIGHT = "copyright";
    public static final String ATT_ATTRIBUTION = "attribution";
    public static final String ATT_LICENSE = "license";
    
    
    public static final String ELEM_BOUNDS = "bounds";
    public static final String ELEM_NODE = "node";
    public static final String ELEM_WAY = "way";

    private static Logger log = LoggerFactory.getLogger(OsmElement.class.getName());

    private Map<String, String> attributes;
    
    private BoundsElement boundsElement;
    private Map<Long, NodeElement> nodeElements;
    private Map<Long, WayElement> wayElements;

    private OsmElement(BoundsElement boundsElement){
        this(new HashMap<String, String>(), boundsElement);
    }

    private OsmElement(Map<String, String> attributes, BoundsElement boundsElement){
        this.attributes = attributes;
        this.boundsElement = boundsElement;
        this.nodeElements = new HashMap<>();
        this.wayElements = new HashMap<>();
    }

//    public OsmElement(Map<String, String> attributes, BoundsElement boundsElement, Collection<NodeElement> nodeElements,
//                      Collection<WayElement> wayElements){
//
//        this(attributes, boundsElement);
//
//        for(NodeElement nodeElement : nodeElements){
//            this.nodeElements.put(nodeElement.getID(), nodeElement);
//        }
//
//        for(WayElement wayElement : wayElements){
//
//        }
//    }



    public void addAttribute(String attributeName, String attributeValue){
        this.attributes.put(attributeName, attributeValue);
    }
    
    public void addNodeElements(Collection<NodeElement> nodeElements){
        for(NodeElement nodeElement : nodeElements){
            this.nodeElements.put(nodeElement.getID(), nodeElement);
        }
    }

    public void addWayElements(Collection<WayElement> wayElements){
        for(WayElement wayElement : wayElements){
            this.wayElements.put(wayElement.getID(), wayElement);
        }
    }

    private void addWayElement(WayElement wayElement){
        this.wayElements.put(wayElement.getID(), wayElement);
    }

    public Map<String, String> getAttributes(){
        return this.attributes;
    }    
    
    public String getAttributeValue(String attributeName){
        return this.attributes.get(attributeName);
    }
    
    /**
     * Returns a {@link java.util.Map} with the values of the nodes ID attributes as keys and the
     * {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement}s as values
     *
     * @return a {@link java.util.Map} with the values of the nodes ID attributes as keys and the
     * {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement}s as values
     */
    public Map<Long, NodeElement> getNodeElements() {
        return this.nodeElements;
    }


    /**
     * Returns the {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement} that has the given ID or
     * <code>null</code> if no such node was found.
     *
     * @param nodeID the ID to lookup the corresponding node for
     *
     * @return the {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement} that has the given ID or
     * <code>null</code> if no such node was found.
     */
    public NodeElement getNodeElement(long nodeID){
        return this.nodeElements.get(nodeID);
    }

    /**
     * Returns a {@link java.util.Map} with the values of the nodes ID attributes as keys and the
     * {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement}s as values
     *
     * @return a {@link java.util.Map} with the values of the nodes ID attributes as keys and the
     * {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement}s as values
     */
    public Map<Long, WayElement> getWayElements() {
        return this.wayElements;
    }


    /**
     * Returns the {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement} that has the given ID or
     * <code>null</code> if no such way was found.
     *
     * @return the {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement} that has the given ID or
     * <code>null</code> if no such way was found.
     */
    public WayElement getWayElement(long wayID){
        return this.wayElements.get(wayID);
    }

    public BoundsElement getBoundsElement() {
        return boundsElement;
    }


    /**
     * This class is for internal use only and is public due to the restrictions (or bug?) of the
     * {@link javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter} not to be applicable on XML root elements.
     */
    @XmlRootElement
    public static class PlainOsmElement{
       
        @XmlAttribute(name = ATT_VERSION)
        private String version;

        @XmlAttribute(name = ATT_GENERATOR)
        private String generator;

        @XmlAttribute(name = ATT_COPYRIGHT)
        private String copyright;

        @XmlAttribute(name = ATT_ATTRIBUTION)
        private String attribution;

        @XmlAttribute(name = ATT_LICENSE)
        private String license;
                
        @XmlElement(name = ELEM_BOUNDS)
        private BoundsElement boundsElement;

        @XmlElement(name = ELEM_NODE)
        private List<NodeElement> nodeElements;

        @XmlElement(name = ELEM_WAY)
        private List<WayElement> wayElements;


        private PlainOsmElement(){
            this.nodeElements = new ArrayList<>();
            this.wayElements = new ArrayList<>();
        }

        private void setBoundsElement(BoundsElement boundsElement){
            this.boundsElement = boundsElement;
        }

        private BoundsElement getBoundsElement(){
            return this.boundsElement;
        }

        private void addNodeElements(Collection<NodeElement> nodeElements){
            this.nodeElements.addAll(nodeElements);
        }

        private List<NodeElement> getNodeElements(){
            return this.nodeElements;
        }

        private void addWayElements(Collection<WayElement> wayElements){
            this.wayElements.addAll(wayElements);
        }

        private List<WayElement> getWayElements(){
            return this.wayElements;
        }

        private String getVersion() {
            return version;
        }

        private void setVersion(String version) {
            this.version = version;
        }

        private String getGenerator() {
            return generator;
        }

        private void setGenerator(String generator){
            this.generator = generator;
        }

        private String getCopyright() {
            return copyright;
        }

        private void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        private String getAttribution() {
            return attribution;
        }

        private void setAttribution(String attribution) {
            this.attribution = attribution;
        }

        private String getLicense() {
            return license;
        }

        private void setLicense(String license) {
            this.license = license;
        }
    }


    public static class OsmElementAdapter extends XmlAdapter<PlainOsmElement, OsmElement>{

        public OsmElement unmarshal(PlainOsmElement plainOsmElement, WayElementFilter wayElementFilter){
            OsmElement result = new OsmElement(plainOsmElement.getBoundsElement());

            result.addAttribute(ATT_VERSION, plainOsmElement.getVersion());
            result.addAttribute(ATT_GENERATOR, plainOsmElement.getGenerator());
            result.addAttribute(ATT_COPYRIGHT, plainOsmElement.getCopyright());
            result.addAttribute(ATT_ATTRIBUTION, plainOsmElement.getAttribution());
            result.addAttribute(ATT_LICENSE, plainOsmElement.getLicense());

            //Add <node> elements
            result.addNodeElements(plainOsmElement.getNodeElements());

            //Add <way> elements and update referencees
            Map<Long, NodeElement> nodeElements = result.getNodeElements();
            for(WayElement wayElement : plainOsmElement.getWayElements()){
                for(Long nodeReference : wayElement.getReferencedNodeIDs()){
                    if(wayElementFilter.matchesCriteria(wayElement)){
                        nodeElements.get(nodeReference).addRefereningWay(wayElement.getID());
                        result.addWayElement(wayElement);
                    }
                }
            }

            return result;
        }

        @Override
        public OsmElement unmarshal(PlainOsmElement plainOsmElement) throws Exception {
            return unmarshal(plainOsmElement, WayElementFilter.ANY_WAY) ;
        }


        @Override
        public PlainOsmElement marshal(OsmElement osmElement) throws Exception {
            PlainOsmElement result = new PlainOsmElement();

            String attributeValue = osmElement.getAttributeValue(ATT_VERSION);
            if(attributeValue != null)
                result.setVersion(attributeValue);

            attributeValue = osmElement.getAttributeValue(ATT_GENERATOR);
            if(attributeValue != null)
                result.setGenerator(attributeValue);

            attributeValue = osmElement.getAttributeValue(ATT_COPYRIGHT);
            if(attributeValue != null)
                result.setCopyright(attributeValue);

            attributeValue = osmElement.getAttributeValue(ATT_ATTRIBUTION);
            if(attributeValue != null)
                result.setAttribution(attributeValue);

            attributeValue = osmElement.getAttributeValue(ATT_LICENSE);
            if(attributeValue != null)
                result.setLicense(attributeValue);

            result.setBoundsElement(osmElement.getBoundsElement());
            result.addNodeElements(osmElement.getNodeElements().values());
            result.addWayElements(osmElement.getWayElements().values());
            return result;
        }
    }
}

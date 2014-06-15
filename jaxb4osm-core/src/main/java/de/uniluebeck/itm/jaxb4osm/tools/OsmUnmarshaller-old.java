///**
// * Copyright (c) 2014, Oliver Kleine, Institute of Telematics, University of Luebeck
// * All rights reserved
// *
// * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
// * following conditions are met:
// *
// *  - Redistributions of source messageCode must retain the above copyright notice, this list of conditions and the following
// *    disclaimer.
// *
// *  - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
// *    following disclaimer in the documentation and/or other materials provided with the distribution.
// *
// *  - Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
// *    products derived from this software without specific prior written permission.
// *
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
// * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
// * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//package de.uniluebeck.itm.jaxb4osm.tools;
//
//import de.uniluebeck.itm.jaxb4osm.elements.NodeElement;
//import de.uniluebeck.itm.jaxb4osm.elements.OsmElement;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.namespace.QName;
//import javax.xml.stream.XMLEventReader;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.events.StartElement;
//import javax.xml.stream.events.XMLEvent;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
///**
// * An {@link OsmUnmarshaller} de-serializes a given OSM file (Open Street Map),
// * e.g. reads <node> and <way> elements into {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement}s and
// * {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement}s.
// *
// * @author Oliver Kleine
// */
//public class OsmUnmarshaller {
//
//    private static Logger log = LoggerFactory.getLogger(OsmUnmarshaller.class.getName());
//
//    public static final QName QNAME_OSM = new QName("osm");
//
//    private Unmarshaller unmarshaller;
//
//    private WayElementFilter wayElementFilter;
//
//    private static OsmUnmarshaller instance;
//    static {
//        try {
//            instance = new OsmUnmarshaller();
//        } catch (Exception e) {
//            log.error("This should never happen.", e);
//        }
//    }
//
//
//    public static OsmUnmarshaller getInstance(){
//        return OsmUnmarshaller.instance;
//    }
//
//    /**
//     * Creates a new instance of {@link OsmUnmarshaller}
//     *
//     * @throws Exception if some unexpected error occurred
//     */
//    private OsmUnmarshaller() throws Exception {
//
//        JAXBContext context = JAXBContext.newInstance(OsmElement.PlainOsmElement.class);
//        this.unmarshaller = context.createUnmarshaller();
//    }
//
//
//    public WayElementFilter getWayElementFilter(){
//        return this.wayElementFilter;
//    }
//
//
//    public synchronized OsmElement unmarshal(File osmFile) throws Exception {
//        return unmarshal(osmFile, WayElementFilter.ANY_WAY, false);
//    }
//    /**
//     * Deserializes the given OSM file into one {@link de.uniluebeck.itm.jaxb4osm.elements.OsmElement} instance.
//     *
//     * @param osmFile the {@link java.io.File} to be de-serialized
//     * @param filter the {@link WayElementFilter} to be applied
//     * @param removeUnreferencedNodes <code>true</code> if the {@link java.util.Map} returned by
//     *                                {@link de.uniluebeck.itm.jaxb4osm.elements.OsmElement#getNodeElements()}
//     *                                is supposed to contain only the
//     *                                {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement}s that are referenced at
//     *                                least by one {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement} contained in
//     *                                the {@link java.util.Map} returned by
//     *                                {@link de.uniluebeck.itm.jaxb4osm.elements.OsmElement#getWayElements()}.
//     *
//     * @throws Exception if some unexpected error occurred
//     */
//    public synchronized OsmElement unmarshal(File osmFile, WayElementFilter filter, boolean removeUnreferencedNodes)
//            throws Exception{
//
//        this.wayElementFilter = filter;
//
//        InputStream inputStream = new FileInputStream(osmFile);
//
//        //create xml event reader for input stream
//        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
//        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);
//
//        //loop through the xml stream
//        XMLEvent xmlEvent = xmlEventReader.peek();
//        while(!(xmlEvent.isStartElement() && QNAME_OSM.equals(((StartElement) xmlEvent).getName()))){
//            //ignore next event
//            xmlEventReader.next();
//
//            //peek next event (but do not read it from stream!)
//            xmlEvent = xmlEventReader.peek();
//        }
//
//        OsmElement.PlainOsmElement plainOsmElement =
//                unmarshaller.unmarshal(xmlEventReader, OsmElement.PlainOsmElement.class).getValue();
//
//        OsmElement osmElement = new OsmElement.OsmElementAdapter().unmarshal(plainOsmElement);
//
//        if(removeUnreferencedNodes){
//            Set<Long> unreferencedNodes = new HashSet<Long>();
//            for(Map.Entry<Long, NodeElement> entry : osmElement.getNodeElements().entrySet()){
//                if(entry.getValue().getReferencingWays().size() == 0){
//                    unreferencedNodes.add(entry.getKey());
//                }
//            }
//
//            for(long nodeID : unreferencedNodes){
//                osmElement.getNodeElements().remove(nodeID);
//            }
//
//        }
//
//        return osmElement;
//    }
//
//
//    public static void main(String[] args) throws Exception {
//
//        long start = System.currentTimeMillis();
//
//        String pathToOsmFile = args[0];
//        OsmUnmarshaller osmUnmarshaller = OsmUnmarshaller.getInstance();
//        OsmElement osmElement  = osmUnmarshaller.unmarshal(new File(pathToOsmFile), WayElementFilter.STREETS, true);
//
//        System.out.println("Found " + osmElement.getNodeElements().size() + " nodes.");
//
//        System.out.println("Found " + osmElement.getWayElements().size() + " streets.");
//
//        int crossings = 0;
//        for(NodeElement nodeElement : osmElement.getNodeElements().values())
//            if(nodeElement.getReferencingWays().size() > 1) crossings++;
//
//        System.out.println("Found " + crossings + " crossings");
//
//        System.out.println("Time passed: " + (System.currentTimeMillis() - start));
//
//        Thread.sleep(100);
//
//    }
//
//}

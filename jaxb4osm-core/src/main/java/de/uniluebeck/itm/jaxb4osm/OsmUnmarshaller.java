package de.uniluebeck.itm.jaxb4osm;

import de.uniluebeck.itm.jaxb4osm.elements.NodeElement;
import de.uniluebeck.itm.jaxb4osm.elements.OsmElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An {@link de.uniluebeck.itm.jaxb4osm.OsmUnmarshaller} de-serializes a given OSM file (Open Street Map),
 * e.g. reads <node> and <way> elements into {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement}s and
 * {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement}s.
 *
 * @author Oliver Kleine
 */
public class OsmUnmarshaller {

    private static Logger log = LoggerFactory.getLogger(OsmUnmarshaller.class.getName());

    public static final QName QNAME_OSM = new QName("osm");

    private Unmarshaller unmarshaller;

    private WayElementFilter wayElementFilter;

    private static OsmUnmarshaller instance;
    static {
        try {
            instance = new OsmUnmarshaller();
        } catch (Exception e) {
            log.error("This should never happen.", e);
        }
    }


    public static OsmUnmarshaller getInstance(){
        return OsmUnmarshaller.instance;
    }

    /**
     * Creates a new instance of {@link de.uniluebeck.itm.jaxb4osm.OsmUnmarshaller}
     *
     * @throws Exception if some unexpected error occurred
     */
    private OsmUnmarshaller() throws Exception {
//        this.nodeElementsMap = new HashMap<Long, NodeElement>();
//        this.wayElementsMap = new HashMap<Long, WayElement>();

        JAXBContext context = JAXBContext.newInstance(OsmElement.PlainOsmElement.class);
        this.unmarshaller = context.createUnmarshaller();
    }


    public WayElementFilter getWayElementFilter(){
        return this.wayElementFilter;
    }


    public synchronized OsmElement deserialize(File osmFile) throws Exception {
        return deserialize(osmFile, WayElementFilter.ANY_WAY, false);
    }
    /**
     * Deserializes the given OSM file into one {@link de.uniluebeck.itm.jaxb4osm.elements.OsmElement} instance.
     *
     * @param osmFile the {@link java.io.File} to be de-serialized
     * @param filter the {@link WayElementFilter} to be applied
     * @param removeUnreferencedNodes <code>true</code> if the {@link java.util.Map} returned by
     *                                {@link de.uniluebeck.itm.jaxb4osm.elements.OsmElement#getNodeElements()}
     *                                is supposed to contain only the
     *                                {@link de.uniluebeck.itm.jaxb4osm.elements.NodeElement}s that are referenced at
     *                                least by one {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement} contained in
     *                                the {@link java.util.Map} returned by
     *                                {@link de.uniluebeck.itm.jaxb4osm.elements.OsmElement#getWayElements()}.
     *
     * @throws Exception if some unexpected error occurred
     */
    public synchronized OsmElement deserialize(File osmFile, WayElementFilter filter, boolean removeUnreferencedNodes)
            throws Exception{

        this.wayElementFilter = filter;

        InputStream inputStream = new FileInputStream(osmFile);

        //create xml event reader for input stream
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

        //loop through the xml stream
        XMLEvent xmlEvent = xmlEventReader.peek();
        while(!(xmlEvent.isStartElement() && QNAME_OSM.equals(((StartElement) xmlEvent).getName()))){
            //ignore next event
            xmlEventReader.next();

            //peek next event (but do not read it from stream!)
            xmlEvent = xmlEventReader.peek();
        }

        OsmElement.PlainOsmElement plainOsmElement =
                unmarshaller.unmarshal(xmlEventReader, OsmElement.PlainOsmElement.class).getValue();

        OsmElement osmElement = new OsmElement.OsmElementAdapter().unmarshal(plainOsmElement);

        if(removeUnreferencedNodes){
            Set<Long> unreferencedNodes = new HashSet<Long>();
            for(Map.Entry<Long, NodeElement> entry : osmElement.getNodeElements().entrySet()){
                if(entry.getValue().getReferencingWays().size() == 0){
                    unreferencedNodes.add(entry.getKey());
                }
            }

            for(long nodeID : unreferencedNodes){
                osmElement.getNodeElements().remove(nodeID);
            }

        }

        return osmElement;
    }


    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        final String MAP_NAME = "luebeck-altstadt";
//        final String MAP_NAME = "bremen";
        final String OSM_FILE = "maps/" + MAP_NAME + "/map.osm";

        OsmUnmarshaller osmUnmarshaller = OsmUnmarshaller.getInstance();
        OsmElement osmElement  = osmUnmarshaller.deserialize(new File(OSM_FILE), WayElementFilter.STREETS, true);

        System.out.println("Found " + osmElement.getNodeElements().size() + " nodes.");

        System.out.println("Found " + osmElement.getWayElements().size() + " streets.");

        int crossings = 0;
        for(NodeElement nodeElement : osmElement.getNodeElements().values())
            if(nodeElement.getReferencingWays().size() > 1) crossings++;

        System.out.println("Found " + crossings + " crossings");

        System.out.println("Time passed: " + (System.currentTimeMillis() - start));

        Thread.sleep(100);

    }

}

package de.uniluebeck.itm.jaxb4osm.tools;

import de.uniluebeck.itm.jaxb4osm.elements.NodeElement;
import de.uniluebeck.itm.jaxb4osm.elements.OsmElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by olli on 15.06.14.
 */
public class OsmUnmarshaller {

    private static Logger log = LoggerFactory.getLogger(OsmUnmarshaller.class.getName());

    private static Unmarshaller unmarshaller;
    static{
        try{
            JAXBContext context = JAXBContext.newInstance(OsmElement.PlainOsmElement.class);
            unmarshaller = context.createUnmarshaller();
        }
        catch (Exception ex){
            log.error("This should never happen!", ex);
        }
    }

    /**
     * Shortcurt for <code>unmarshal(osmFile, WayElementFilter.ANY_WAY, false)</code>
     *
     * @param osmFile the OSM file to be unmarshalled
     *
     * @return the unmarshalled {@link de.uniluebeck.itm.jaxb4osm.elements.OsmElement}
     *
     * @throws Exception if some error occurred
     */
    public static OsmElement unmarshal(File osmFile) throws Exception {
        return unmarshal(osmFile, WayElementFilter.ANY_WAY);
    }

    /**
     * Shortcurt for <code>unmarshal(osmFile, filter, false)</code>
     *
     * @param osmFile the OSM file to be unmarshalled
     *
     * @return the unmarshalled {@link de.uniluebeck.itm.jaxb4osm.elements.OsmElement}
     *
     * @throws Exception if some error occurred
     */
    public static OsmElement unmarshal(File osmFile, WayElementFilter filter) throws Exception {
        return unmarshal(osmFile, filter, false);
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
    public static OsmElement unmarshal(File osmFile, WayElementFilter filter, boolean removeUnreferencedNodes)
            throws Exception{

        InputStream inputStream = new FileInputStream(osmFile);

        //create xml event reader for input stream
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

        //Do the un-marshalling
        OsmElement.PlainOsmElement plainOsmElement =
                unmarshaller.unmarshal(xmlEventReader, OsmElement.PlainOsmElement.class).getValue();

        OsmElement osmElement = new OsmElement.OsmElementAdapter().unmarshal(plainOsmElement, filter);

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

//        createUnmarshaller();
        String pathToOsmFile = args[0];
        OsmElement osmElement  = OsmUnmarshaller.unmarshal(new File(pathToOsmFile), WayElementFilter.STREETS, true);

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

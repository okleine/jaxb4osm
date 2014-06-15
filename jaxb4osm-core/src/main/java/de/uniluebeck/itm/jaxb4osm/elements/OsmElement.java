package de.uniluebeck.itm.jaxb4osm.elements;

import de.uniluebeck.itm.jaxb4osm.OsmUnmarshaller;
import de.uniluebeck.itm.jaxb4osm.WayElementFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JAXB compliant class for the osm (root) element in OSM files (Open Street Map)
 *
 * @author Oliver Kleine
 */

public class OsmElement {

    public static final String ELEM_BOUNDS = "bounds";
    public static final String ELEM_NODE = "node";
    public static final String ELEM_WAY = "way";

    private static Logger log = LoggerFactory.getLogger(OsmElement.class.getName());

    private BoundsElement boundsElement;
    private Map<Long, NodeElement> nodeElements;
    private Map<Long, WayElement> wayElements;

    private OsmElement(BoundsElement boundsElement){
        this.boundsElement = boundsElement;
        this.nodeElements = new HashMap<Long, NodeElement>();
        this.wayElements = new HashMap<Long, WayElement>();
    }


    private void addNodeElements(Collection<NodeElement> nodeElements){
        for(NodeElement nodeElement : nodeElements){
            this.nodeElements.put(nodeElement.getID(), nodeElement);
        }
    }


    private void addWayElement(WayElement wayElement){
        this.wayElements.put(wayElement.getID(), wayElement);
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
    public static class PlainOsmElement{

        @XmlElement(name = ELEM_BOUNDS)
        private BoundsElement boundsElement;

        @XmlElement(name = ELEM_NODE)
        private List<NodeElement> nodeElements;

        @XmlElement(name = ELEM_WAY)
        private List<WayElement> wayElements;


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


    }


    public static class OsmElementAdapter extends XmlAdapter<PlainOsmElement, OsmElement>{

        private WayElementFilter wayElementFilter = OsmUnmarshaller.getInstance().getWayElementFilter();

        @Override
        public OsmElement unmarshal(PlainOsmElement plainOsmElement) throws Exception {
            OsmElement result = new OsmElement(plainOsmElement.getBoundsElement());

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
        public PlainOsmElement marshal(OsmElement osmElement) throws Exception {
            PlainOsmElement result = new PlainOsmElement();
            result.setBoundsElement(osmElement.getBoundsElement());
            result.addNodeElements(osmElement.getNodeElements().values());
            result.addWayElements(osmElement.getWayElements().values());
            return result;
        }
    }
}

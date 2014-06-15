package de.uniluebeck.itm.jaxb4osm;


import de.uniluebeck.itm.jaxb4osm.elements.WayElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link de.uniluebeck.itm.jaxb4osm.WayElementFilter} is used to check whether a given
 * {@link de.uniluebeck.itm.jaxb4osm.WayElementFilter} is to be further processed or ignored for the
 * (un-)marshalling process.
 *
 * @author Oliver Kleine
 */
public abstract class WayElementFilter {

    /**
     * An {@link de.uniluebeck.itm.jaxb4osm.WayElementFilter} to filter for streets, i.e. roads that are allowed
     * to be used by cars
     */
    public static final WayElementFilter STREETS = new WayElementFilter() {

        private Set<String> highwayValues = new HashSet<String>(Arrays.asList(
                "motorway", "trunk", "primary", "secondary", "tertiary", "unclassified", "residential", "service",
                "motorway_link", "trunk_link", "primary_link", "secondary_link", "tertiary_link", "living_street"
        ));

        @Override
        public boolean matchesCriteria(WayElement wayElement) {
            return highwayValues.contains(wayElement.getTagValue("highway"));
        }
    };


    public static final WayElementFilter LUEBECK_WALLSTR = new WayElementFilter() {
        @Override
        public boolean matchesCriteria(WayElement wayElement) {
            if(!WayElementFilter.STREETS.matchesCriteria(wayElement))
                return false;

            return "Wallstraße".equals(wayElement.getTagValue("name"));
        }
    };


    public static final WayElementFilter LUEBECK_GROSSE_BURGSTR = new WayElementFilter() {
        @Override
        public boolean matchesCriteria(WayElement wayElement) {
            if(!WayElementFilter.STREETS.matchesCriteria(wayElement))
                return false;

            return "Große Burgstraße".equals(wayElement.getTagValue("name"));
        }
    };


    public static final WayElementFilter TEST = new WayElementFilter() {
        @Override
        public boolean matchesCriteria(WayElement wayElement) {
            return wayElement.getID() == 191533107;
        }
    };


    /**
     * An {@link de.uniluebeck.itm.jaxb4osm.WayElementFilter} to filter out nothing, i.e. the de-serialized
     * data contains everything that was contained in the OSM file.
     */
    public static final WayElementFilter ANY_WAY = new WayElementFilter() {
        @Override
        public boolean matchesCriteria(WayElement wayElement) {
            return true;
        }
    };


    /**
     * This method is called during the (un-)marshalling process to check whether the given
     * {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement} is to be further processed or ignored.
     *
     * @param wayElement the {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement} to check for the criteria
     *                   defined by filter
     *
     * @return <code>true</code> if the {@link de.uniluebeck.itm.jaxb4osm.elements.WayElement} is to be further
     * processed or <code>false</code> otherwise.
     */
    public abstract boolean matchesCriteria(WayElement wayElement);

}



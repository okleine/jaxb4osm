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
package de.uniluebeck.itm.jaxb4osm.tools;


import de.uniluebeck.itm.jaxb4osm.elements.WayElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link WayElementFilter} is used to check whether a given
 * {@link WayElementFilter} is to be further processed or ignored for the
 * (un-)marshalling process.
 *
 * @author Oliver Kleine
 */
public abstract class WayElementFilter {

    /**
     * An {@link WayElementFilter} to filter for streets, i.e. roads that are allowed
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
     * An {@link WayElementFilter} to filter out nothing, i.e. the de-serialized
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



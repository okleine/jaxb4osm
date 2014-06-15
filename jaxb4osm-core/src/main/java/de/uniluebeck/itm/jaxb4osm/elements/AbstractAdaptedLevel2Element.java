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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for unmarshalled XML elements from OSM files (node, way, relation) after unmarshalling resp.
 * before marshalling by the appropriate {@link javax.xml.bind.annotation.adapters.XmlAdapter}.
 *
 * In simple terms: Classes extending {@link de.uniluebeck.itm.jaxb4osm.elements.AbstractAdaptedLevel2Element} are
 * the classes that represent layer 2 elements from OSM files to JAVA applications.
 *
 * @author Oliver Kleine
 */
public abstract class AbstractAdaptedLevel2Element extends AbstractLevel2Element{

    private static Logger log = LoggerFactory.getLogger(AbstractAdaptedLevel2Element.class.getName());

    private Map<String, String> tags;

    protected AbstractAdaptedLevel2Element(AbstractPlainLevel2Element abstractPlainLevel2Element){
        super(abstractPlainLevel2Element);
        this.tags = new HashMap<String, String>();

        for(TagElement tagElement : abstractPlainLevel2Element.getTagElements()){
            if(this.tags.put(tagElement.getKey(), tagElement.getValue()) != null){
                log.warn("Node with ID {} contains multiple tags with key {}!", abstractPlainLevel2Element.getID(),
                    tagElement.getKey());
            }
        }
    }


    /**
     * Returns a {@link java.util.Map} containing the tags of the respective parent element. All <code><tag></code>
     * elements from the original OSM file (<code><tag k="key" v="value"/></code>) are contained in this map with
     * the value of the "k" attribute as key and the value of the "v" attribute as the value.
     *
     * @return a {@link java.util.Map} containing the tags of the respective parent element.
     */
    public Map<String, String> getTags(){
        return this.tags;
    }


    /**
     * Returns the value of the "v" attribute of the tag with attribute <code>k="tagKey"</code> or <code>null</code>
     * if the parent element did not contain a tag child with such a "k" attribute.
     *
     * @param tagKey the value of the "k" attribute
     *
     * @return the value of the "v" attribute of the tag with attribute <code>k="tagKey"</code> or <code>null</code>
     * if the parent element did not contain a tag child with such a "k" attribute.
     */
    public String getTagValue(String tagKey){
        return this.tags.get(tagKey);
    }
}

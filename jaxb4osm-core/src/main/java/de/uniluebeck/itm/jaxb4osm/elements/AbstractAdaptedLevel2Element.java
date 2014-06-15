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

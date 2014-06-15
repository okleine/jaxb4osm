package de.uniluebeck.itm.jaxb4osm.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * JAXB compliant class for the tag element in OSM files (from OpenStreetMap)
 *
 * @author Oliver Kleine
 */
public class TagElement {

    private static Logger log = LoggerFactory.getLogger(TagElement.class.getName());

    @XmlAttribute(name = "k")
    private String key;


    @XmlAttribute(name = "v")
    private String value;


    private TagElement(){}


    TagElement(String key, String value){
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the value of the attribute "k" (e.g. <code>xyz</code> if the tag element was like
     * <code><tag k="xyz" ...></code>).
     *
     * @return the value of the attribute "k"
     */
    public String getKey(){
        return this.key;
    }

    /**
     * Returns the value of the attribute "value" (e.g. <code>xyz</code> if the tag element was like
     * <code><tag ... v="xyz" ...></code>).
     *
     * @return the value of the attribute "v"
     */
    public String getValue(){
        return this.value;
    }


    @Override
    public int hashCode(){
        return this.key.hashCode() + this.value.hashCode();
    }

    /**
     * Returns a {@link String} representation of this {@link de.uniluebeck.itm.jaxb4osm.elements.TagElement}.
     *
     * @return a {@link String} representation of this {@link de.uniluebeck.itm.jaxb4osm.elements.TagElement}.
     */
    @Override
    public String toString(){
        return "osm:tag (key=" + this.key + ", value=" + this.value + ")";
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof TagElement))
            return false;

        TagElement other = (TagElement) object;
        return this.getKey().equals(other.getKey()) && this.getValue().equals(other.getValue());
    }
}

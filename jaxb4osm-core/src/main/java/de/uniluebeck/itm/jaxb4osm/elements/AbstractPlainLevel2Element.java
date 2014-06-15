package de.uniluebeck.itm.jaxb4osm.elements;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for unmarshalled XML elements from OSM files (node, way, relation) before being processed by
 * the appropriate {@link javax.xml.bind.annotation.adapters.XmlAdapter}.
 *
 * @author Oliver Kleine
 */
public abstract class AbstractPlainLevel2Element extends AbstractLevel2Element{

    @XmlElement(name = "tag", type=TagElement.class)
    private List<TagElement> tagElements;

    AbstractPlainLevel2Element(){
        this.tagElements = new ArrayList<TagElement>();
    }

    AbstractPlainLevel2Element(AbstractLevel2Element abstractLevel2Element){
        super(abstractLevel2Element);
        this.tagElements = new ArrayList<TagElement>();
    }

    void addTagElement(String key, String value){
        this.tagElements.add(new TagElement(key, value));
    }

    List<TagElement> getTagElements(){
        return this.tagElements;
    }
}

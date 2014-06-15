package de.uniluebeck.itm.jaxb4osm.elements;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by olli on 15.06.14.
 */
public class NdElement{

    @XmlAttribute(name = "ref")
    private long reference;

    NdElement(){}

    public NdElement(long reference){
        this.reference = reference;
    }

    public long getReference(){
        return this.reference;
    }
}


package de.uniluebeck.itm.jaxb4osm.elements;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by olli on 15.06.14.
 */
@XmlTransient
public abstract class AbstractOsmElement{

    protected static final String ATT_VERSION = "version";
    protected static final String ATT_GENERATOR = "generator";
    protected static final String ATT_COPYRIGHT = "copyright";
    protected static final String ATT_ATTRIBUTION = "attribution";
    protected static final String ATT_LICENSE = "license";

    protected static final String ELEM_BOUNDS = "bounds";
    protected static final String ELEM_NODE = "node";
    protected static final String ELEM_WAY = "way";

    protected static final String PROP_VERSION = "version";
    protected static final String PROP_GENERATOR = "generator";
    protected static final String PROP_COPYRIGHT = "copyright";
    protected static final String PROP_ATTRIBUTION = "attribution";
    protected static final String PROP_LICENSE = "license";

    protected static final String PROP_BOUNDS = "boundsElement";
    protected static final String PROP_NODE = "nodeElements";
    protected static final String PROP_WAY = "wayElements";
    

    private String version;
    private String generator;
    private String copyright;
    private String attribution;
    private String license;
    private BoundsElement boundsElement;

    protected AbstractOsmElement(){}
    
    protected AbstractOsmElement(AbstractOsmElement abstractOsmElement){
        this.setVersion(abstractOsmElement.getVersion());
        this.setGenerator(abstractOsmElement.getGenerator());
        this.setCopyright(abstractOsmElement.getCopyright());
        this.setAttribution(abstractOsmElement.getAttribution());
        this.setLicense(abstractOsmElement.getLicense());
    }
    
    
    @XmlAttribute(name = ATT_VERSION)
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlAttribute(name = ATT_GENERATOR)
    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator){
        this.generator = generator;
    }

    @XmlAttribute(name = ATT_COPYRIGHT)
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @XmlAttribute(name = ATT_ATTRIBUTION)
    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    @XmlAttribute(name = ATT_LICENSE)
    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setBoundsElement(BoundsElement boundsElement){
        this.boundsElement = boundsElement;
    }

    @XmlElement(name = ELEM_BOUNDS)
    public BoundsElement getBoundsElement() {
        return boundsElement;
    }

}

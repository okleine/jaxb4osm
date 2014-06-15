package de.uniluebeck.itm.jaxb4osm.elements;


import javax.xml.bind.annotation.XmlAttribute;
import java.util.Date;

/**
 * Abstract base class for base class for unmarshalled XML elements from OSM files (node, way, relation).
 *
 * @author Oliver Kleine
 */
public abstract class AbstractLevel2Element {

    @XmlAttribute(name = "id")
    private long ID;

    @XmlAttribute(name = "version")
    private int version;

    @XmlAttribute(name = "changeset")
    private int changeset;

    @XmlAttribute(name = "visible")
    private boolean visible;

    @XmlAttribute(name = "timestamp")
    private Date timestamp;

    @XmlAttribute(name = "user")
    private String user;

    @XmlAttribute(name = "uid")
    private String userID;


    protected AbstractLevel2Element(){}


    protected AbstractLevel2Element(AbstractLevel2Element other){
        this.ID = other.getID();
        this.version = other.getVersion();
        this.changeset = other.getChangeset();
        this.visible = other.isVisible();
        this.timestamp = other.getTimestamp();
        this.user = other.getUser();
        this.userID = other.getUserID();
    }

    public long getID() {
        return ID;
    }

    public int getVersion() {
        return version;
    }

    public int getChangeset() {
        return changeset;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isVisible() {
        return visible;
    }
}

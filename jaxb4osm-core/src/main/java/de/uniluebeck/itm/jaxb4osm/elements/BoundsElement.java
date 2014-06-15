package de.uniluebeck.itm.jaxb4osm.elements;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by olli on 13.06.14.
 */
public class BoundsElement {

    public static final String ATT_MIN_LATITUDE = "minLat";
    public static final String ATT_MAX_LATITUDE = "maxLat";
    public static final String ATT_MIN_LONGITUDE = "minLon";
    public static final String ATT_MAX_LONGITUDE = "maxLon";


    @XmlAttribute(name = ATT_MIN_LATITUDE)
    private double minLatitude;

    @XmlAttribute(name = ATT_MAX_LATITUDE)
    private double maxLatitude;

    @XmlAttribute(name = ATT_MIN_LONGITUDE)
    private double minLongitude;

    @XmlAttribute(name = ATT_MAX_LONGITUDE)
    private double maxLongitude;

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }
}

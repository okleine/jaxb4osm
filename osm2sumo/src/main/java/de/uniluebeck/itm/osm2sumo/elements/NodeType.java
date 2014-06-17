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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.16 at 12:03:39 PM CEST 
//


package de.uniluebeck.itm.osm2sumo.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nodeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="x" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="y" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="z" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="traffic_light"/>
 *             &lt;enumeration value="right_before_left"/>
 *             &lt;enumeration value="priority"/>
 *             &lt;enumeration value="dead_end"/>
 *             &lt;enumeration value="unregulated"/>
 *             &lt;enumeration value="traffic_light_unregulated"/>
 *             &lt;enumeration value="allway_stop"/>
 *             &lt;enumeration value="priority_stop"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="tl" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tlType" type="{}tlTypeType" />
 *       &lt;attribute name="controlledInner" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="shape" type="{}shapeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodeType")
public class NodeType {

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "x")
    protected Float x;
    @XmlAttribute(name = "y")
    protected Float y;
    @XmlAttribute(name = "z")
    protected Float z;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "tl")
    protected String tl;
    @XmlAttribute(name = "tlType")
    protected TlTypeType tlType;
    @XmlAttribute(name = "controlledInner")
    protected String controlledInner;
    @XmlAttribute(name = "shape")
    protected String shape;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the x property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setX(Float value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setY(Float value) {
        this.y = value;
    }

    /**
     * Gets the value of the z property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getZ() {
        return z;
    }

    /**
     * Sets the value of the z property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setZ(Float value) {
        this.z = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the tl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTl() {
        return tl;
    }

    /**
     * Sets the value of the tl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTl(String value) {
        this.tl = value;
    }

    /**
     * Gets the value of the tlType property.
     * 
     * @return
     *     possible object is
     *     {@link TlTypeType }
     *     
     */
    public TlTypeType getTlType() {
        return tlType;
    }

    /**
     * Sets the value of the tlType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TlTypeType }
     *     
     */
    public void setTlType(TlTypeType value) {
        this.tlType = value;
    }

    /**
     * Gets the value of the controlledInner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControlledInner() {
        return controlledInner;
    }

    /**
     * Sets the value of the controlledInner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControlledInner(String value) {
        this.controlledInner = value;
    }

    /**
     * Gets the value of the shape property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShape() {
        return shape;
    }

    /**
     * Sets the value of the shape property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShape(String value) {
        this.shape = value;
    }

}
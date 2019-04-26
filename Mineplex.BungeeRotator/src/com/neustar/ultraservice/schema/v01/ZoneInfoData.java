
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZoneInfoData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZoneInfoData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="guid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="zoneid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="infoname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="infovalue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="modified" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZoneInfoData")
public class ZoneInfoData {

    @XmlAttribute(name = "guid", required = true)
    protected String guid;
    @XmlAttribute(name = "zoneid", required = true)
    protected String zoneid;
    @XmlAttribute(name = "infoname")
    protected String infoname;
    @XmlAttribute(name = "infovalue")
    protected String infovalue;
    @XmlAttribute(name = "modified")
    protected String modified;

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
    }

    /**
     * Gets the value of the zoneid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneid() {
        return zoneid;
    }

    /**
     * Sets the value of the zoneid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneid(String value) {
        this.zoneid = value;
    }

    /**
     * Gets the value of the infoname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfoname() {
        return infoname;
    }

    /**
     * Sets the value of the infoname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfoname(String value) {
        this.infoname = value;
    }

    /**
     * Gets the value of the infovalue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfovalue() {
        return infovalue;
    }

    /**
     * Sets the value of the infovalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfovalue(String value) {
        this.infovalue = value;
    }

    /**
     * Gets the value of the modified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModified() {
        return modified;
    }

    /**
     * Sets the value of the modified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModified(String value) {
        this.modified = value;
    }

}

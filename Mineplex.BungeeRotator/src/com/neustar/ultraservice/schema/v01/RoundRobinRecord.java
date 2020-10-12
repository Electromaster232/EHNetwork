
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoundRobinRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RoundRobinRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="lbPoolID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="info1Value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ZoneName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TTL" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoundRobinRecord")
public class RoundRobinRecord {

    @XmlAttribute(name = "lbPoolID", required = true)
    protected String lbPoolID;
    @XmlAttribute(name = "info1Value", required = true)
    protected String info1Value;
    @XmlAttribute(name = "ZoneName", required = true)
    protected String zoneName;
    @XmlAttribute(name = "Type", required = true)
    protected String type;
    @XmlAttribute(name = "TTL")
    protected String ttl;

    /**
     * Gets the value of the lbPoolID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLbPoolID() {
        return lbPoolID;
    }

    /**
     * Sets the value of the lbPoolID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLbPoolID(String value) {
        this.lbPoolID = value;
    }

    /**
     * Gets the value of the info1Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo1Value() {
        return info1Value;
    }

    /**
     * Sets the value of the info1Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo1Value(String value) {
        this.info1Value = value;
    }

    /**
     * Gets the value of the zoneName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * Sets the value of the zoneName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneName(String value) {
        this.zoneName = value;
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
     * Gets the value of the ttl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTTL() {
        return ttl;
    }

    /**
     * Sets the value of the ttl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTTL(String value) {
        this.ttl = value;
    }

}

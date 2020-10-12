
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateRoundRobinRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateRoundRobinRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="rrGuid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lbPoolID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="info1Value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TTL" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateRoundRobinRecord")
public class UpdateRoundRobinRecord {

    @XmlAttribute(name = "rrGuid", required = true)
    protected String rrGuid;
    @XmlAttribute(name = "lbPoolID", required = true)
    protected String lbPoolID;
    @XmlAttribute(name = "info1Value", required = true)
    protected String info1Value;
    @XmlAttribute(name = "TTL")
    protected String ttl;

    /**
     * Gets the value of the rrGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRrGuid() {
        return rrGuid;
    }

    /**
     * Sets the value of the rrGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRrGuid(String value) {
        this.rrGuid = value;
    }

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

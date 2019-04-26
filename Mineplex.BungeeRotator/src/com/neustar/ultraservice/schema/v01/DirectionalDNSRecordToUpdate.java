
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirectionalDNSRecordToUpdate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectionalDNSRecordToUpdate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InfoValues" type="{http://schema.ultraservice.neustar.com/v01/}InfoValues"/>
 *       &lt;/sequence>
 *       &lt;attribute name="TTL" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectionalDNSRecordToUpdate", propOrder = {
    "infoValues"
})
public class DirectionalDNSRecordToUpdate {

    @XmlElement(name = "InfoValues", required = true)
    protected InfoValues infoValues;
    @XmlAttribute(name = "TTL")
    protected String ttl;

    /**
     * Gets the value of the infoValues property.
     * 
     * @return
     *     possible object is
     *     {@link InfoValues }
     *     
     */
    public InfoValues getInfoValues() {
        return infoValues;
    }

    /**
     * Sets the value of the infoValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoValues }
     *     
     */
    public void setInfoValues(InfoValues value) {
        this.infoValues = value;
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

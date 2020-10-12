
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UnsuspendZone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UnsuspendZone">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="unsuspendZoneRequest" type="{http://schema.ultraservice.neustar.com/v01/}UnsuspendZoneRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnsuspendZone", propOrder = {
    "unsuspendZoneRequest"
})
public class UnsuspendZone {

    @XmlElement(required = true)
    protected UnsuspendZoneRequest unsuspendZoneRequest;

    /**
     * Gets the value of the unsuspendZoneRequest property.
     * 
     * @return
     *     possible object is
     *     {@link UnsuspendZoneRequest }
     *     
     */
    public UnsuspendZoneRequest getUnsuspendZoneRequest() {
        return unsuspendZoneRequest;
    }

    /**
     * Sets the value of the unsuspendZoneRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnsuspendZoneRequest }
     *     
     */
    public void setUnsuspendZoneRequest(UnsuspendZoneRequest value) {
        this.unsuspendZoneRequest = value;
    }

}

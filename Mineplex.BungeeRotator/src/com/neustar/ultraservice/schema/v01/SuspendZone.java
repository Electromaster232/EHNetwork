
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SuspendZone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SuspendZone">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="suspendZoneRequest" type="{http://schema.ultraservice.neustar.com/v01/}SuspendZoneRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SuspendZone", propOrder = {
    "suspendZoneRequest"
})
public class SuspendZone {

    @XmlElement(required = true)
    protected SuspendZoneRequest suspendZoneRequest;

    /**
     * Gets the value of the suspendZoneRequest property.
     * 
     * @return
     *     possible object is
     *     {@link SuspendZoneRequest }
     *     
     */
    public SuspendZoneRequest getSuspendZoneRequest() {
        return suspendZoneRequest;
    }

    /**
     * Sets the value of the suspendZoneRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuspendZoneRequest }
     *     
     */
    public void setSuspendZoneRequest(SuspendZoneRequest value) {
        this.suspendZoneRequest = value;
    }

}

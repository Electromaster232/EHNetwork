
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeolocationGroupData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeolocationGroupData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GroupData" type="{http://schema.ultraservice.neustar.com/v01/}GroupData"/>
 *         &lt;element name="GeolocationGroupDetails" type="{http://schema.ultraservice.neustar.com/v01/}GeolocationGroupDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeolocationGroupData", propOrder = {
    "groupData",
    "geolocationGroupDetails"
})
public class GeolocationGroupData {

    @XmlElement(name = "GroupData", required = true)
    protected GroupData groupData;
    @XmlElement(name = "GeolocationGroupDetails", required = true, nillable = true)
    protected GeolocationGroupDetails geolocationGroupDetails;

    /**
     * Gets the value of the groupData property.
     * 
     * @return
     *     possible object is
     *     {@link GroupData }
     *     
     */
    public GroupData getGroupData() {
        return groupData;
    }

    /**
     * Sets the value of the groupData property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupData }
     *     
     */
    public void setGroupData(GroupData value) {
        this.groupData = value;
    }

    /**
     * Gets the value of the geolocationGroupDetails property.
     * 
     * @return
     *     possible object is
     *     {@link GeolocationGroupDetails }
     *     
     */
    public GeolocationGroupDetails getGeolocationGroupDetails() {
        return geolocationGroupDetails;
    }

    /**
     * Sets the value of the geolocationGroupDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeolocationGroupDetails }
     *     
     */
    public void setGeolocationGroupDetails(GeolocationGroupDetails value) {
        this.geolocationGroupDetails = value;
    }

}

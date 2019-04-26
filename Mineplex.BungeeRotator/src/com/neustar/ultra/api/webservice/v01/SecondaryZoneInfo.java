
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for secondaryZoneInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="secondaryZoneInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="notificationEmailAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primaryNameServers" type="{http://webservice.api.ultra.neustar.com/v01/}primaryNameServers" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "secondaryZoneInfo", propOrder = {
    "notificationEmailAddress",
    "primaryNameServers"
})
public class SecondaryZoneInfo {

    protected String notificationEmailAddress;
    protected PrimaryNameServers primaryNameServers;

    /**
     * Gets the value of the notificationEmailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationEmailAddress() {
        return notificationEmailAddress;
    }

    /**
     * Sets the value of the notificationEmailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationEmailAddress(String value) {
        this.notificationEmailAddress = value;
    }

    /**
     * Gets the value of the primaryNameServers property.
     * 
     * @return
     *     possible object is
     *     {@link PrimaryNameServers }
     *     
     */
    public PrimaryNameServers getPrimaryNameServers() {
        return primaryNameServers;
    }

    /**
     * Sets the value of the primaryNameServers property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaryNameServers }
     *     
     */
    public void setPrimaryNameServers(PrimaryNameServers value) {
        this.primaryNameServers = value;
    }

}


package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for zone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="zone">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aliasZoneInfo" type="{http://webservice.api.ultra.neustar.com/v01/}aliasZoneInfo" minOccurs="0"/>
 *         &lt;element name="primaryZoneInfo" type="{http://webservice.api.ultra.neustar.com/v01/}primaryZoneInfo" minOccurs="0"/>
 *         &lt;element name="secondaryZoneInfo" type="{http://webservice.api.ultra.neustar.com/v01/}secondaryZoneInfo" minOccurs="0"/>
 *         &lt;element name="zoneProperties" type="{http://webservice.api.ultra.neustar.com/v01/}zoneProperties" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "zone", propOrder = {
    "aliasZoneInfo",
    "primaryZoneInfo",
    "secondaryZoneInfo",
    "zoneProperties"
})
@XmlSeeAlso({
    InternalZone.class
})
public class Zone {

    protected AliasZoneInfo aliasZoneInfo;
    protected PrimaryZoneInfo primaryZoneInfo;
    protected SecondaryZoneInfo secondaryZoneInfo;
    protected ZoneProperties zoneProperties;

    /**
     * Gets the value of the aliasZoneInfo property.
     * 
     * @return
     *     possible object is
     *     {@link AliasZoneInfo }
     *     
     */
    public AliasZoneInfo getAliasZoneInfo() {
        return aliasZoneInfo;
    }

    /**
     * Sets the value of the aliasZoneInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link AliasZoneInfo }
     *     
     */
    public void setAliasZoneInfo(AliasZoneInfo value) {
        this.aliasZoneInfo = value;
    }

    /**
     * Gets the value of the primaryZoneInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PrimaryZoneInfo }
     *     
     */
    public PrimaryZoneInfo getPrimaryZoneInfo() {
        return primaryZoneInfo;
    }

    /**
     * Sets the value of the primaryZoneInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaryZoneInfo }
     *     
     */
    public void setPrimaryZoneInfo(PrimaryZoneInfo value) {
        this.primaryZoneInfo = value;
    }

    /**
     * Gets the value of the secondaryZoneInfo property.
     * 
     * @return
     *     possible object is
     *     {@link SecondaryZoneInfo }
     *     
     */
    public SecondaryZoneInfo getSecondaryZoneInfo() {
        return secondaryZoneInfo;
    }

    /**
     * Sets the value of the secondaryZoneInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecondaryZoneInfo }
     *     
     */
    public void setSecondaryZoneInfo(SecondaryZoneInfo value) {
        this.secondaryZoneInfo = value;
    }

    /**
     * Gets the value of the zoneProperties property.
     * 
     * @return
     *     possible object is
     *     {@link ZoneProperties }
     *     
     */
    public ZoneProperties getZoneProperties() {
        return zoneProperties;
    }

    /**
     * Sets the value of the zoneProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZoneProperties }
     *     
     */
    public void setZoneProperties(ZoneProperties value) {
        this.zoneProperties = value;
    }

}

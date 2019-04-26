
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirectionalRecordData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectionalRecordData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DirectionalRecordConfiguration" type="{http://schema.ultraservice.neustar.com/v01/}DirectionalDNSRecord"/>
 *         &lt;element name="GeolocationGroupData" type="{http://schema.ultraservice.neustar.com/v01/}GeolocationGroupData"/>
 *         &lt;element name="SourceIPGroupData" type="{http://schema.ultraservice.neustar.com/v01/}SourceIPGroupData"/>
 *       &lt;/sequence>
 *       &lt;attribute name="existingResourceRecordId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="createAllNonConfiguredGrp" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectionalRecordData", propOrder = {
    "directionalRecordConfiguration",
    "geolocationGroupData",
    "sourceIPGroupData"
})
public class DirectionalRecordData {

    @XmlElement(name = "DirectionalRecordConfiguration", required = true, nillable = true)
    protected DirectionalDNSRecord directionalRecordConfiguration;
    @XmlElement(name = "GeolocationGroupData", required = true, nillable = true)
    protected GeolocationGroupData geolocationGroupData;
    @XmlElement(name = "SourceIPGroupData", required = true, nillable = true)
    protected SourceIPGroupData sourceIPGroupData;
    @XmlAttribute(name = "existingResourceRecordId")
    protected String existingResourceRecordId;
    @XmlAttribute(name = "createAllNonConfiguredGrp", required = true)
    protected boolean createAllNonConfiguredGrp;

    /**
     * Gets the value of the directionalRecordConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link DirectionalDNSRecord }
     *     
     */
    public DirectionalDNSRecord getDirectionalRecordConfiguration() {
        return directionalRecordConfiguration;
    }

    /**
     * Sets the value of the directionalRecordConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirectionalDNSRecord }
     *     
     */
    public void setDirectionalRecordConfiguration(DirectionalDNSRecord value) {
        this.directionalRecordConfiguration = value;
    }

    /**
     * Gets the value of the geolocationGroupData property.
     * 
     * @return
     *     possible object is
     *     {@link GeolocationGroupData }
     *     
     */
    public GeolocationGroupData getGeolocationGroupData() {
        return geolocationGroupData;
    }

    /**
     * Sets the value of the geolocationGroupData property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeolocationGroupData }
     *     
     */
    public void setGeolocationGroupData(GeolocationGroupData value) {
        this.geolocationGroupData = value;
    }

    /**
     * Gets the value of the sourceIPGroupData property.
     * 
     * @return
     *     possible object is
     *     {@link SourceIPGroupData }
     *     
     */
    public SourceIPGroupData getSourceIPGroupData() {
        return sourceIPGroupData;
    }

    /**
     * Sets the value of the sourceIPGroupData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceIPGroupData }
     *     
     */
    public void setSourceIPGroupData(SourceIPGroupData value) {
        this.sourceIPGroupData = value;
    }

    /**
     * Gets the value of the existingResourceRecordId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExistingResourceRecordId() {
        return existingResourceRecordId;
    }

    /**
     * Sets the value of the existingResourceRecordId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExistingResourceRecordId(String value) {
        this.existingResourceRecordId = value;
    }

    /**
     * Gets the value of the createAllNonConfiguredGrp property.
     * 
     */
    public boolean isCreateAllNonConfiguredGrp() {
        return createAllNonConfiguredGrp;
    }

    /**
     * Sets the value of the createAllNonConfiguredGrp property.
     * 
     */
    public void setCreateAllNonConfiguredGrp(boolean value) {
        this.createAllNonConfiguredGrp = value;
    }

}

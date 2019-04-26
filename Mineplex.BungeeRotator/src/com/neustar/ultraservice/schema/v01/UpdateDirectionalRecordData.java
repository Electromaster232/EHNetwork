
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateDirectionalRecordData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateDirectionalRecordData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DirectionalRecordConfiguration" type="{http://schema.ultraservice.neustar.com/v01/}DirectionalDNSRecordToUpdate"/>
 *         &lt;element name="GeolocationGroupDetails" type="{http://schema.ultraservice.neustar.com/v01/}UpdateGeolocationGroupDetails"/>
 *         &lt;element name="SourceIPGroupDetails" type="{http://schema.ultraservice.neustar.com/v01/}UpdateSourceIPGroupDetails"/>
 *         &lt;element name="forceOverlapTransfer" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="directionalPoolRecordId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateDirectionalRecordData", propOrder = {
    "directionalRecordConfiguration",
    "geolocationGroupDetails",
    "sourceIPGroupDetails",
    "forceOverlapTransfer"
})
public class UpdateDirectionalRecordData {

    @XmlElement(name = "DirectionalRecordConfiguration", required = true, nillable = true)
    protected DirectionalDNSRecordToUpdate directionalRecordConfiguration;
    @XmlElement(name = "GeolocationGroupDetails", required = true, nillable = true)
    protected UpdateGeolocationGroupDetails geolocationGroupDetails;
    @XmlElement(name = "SourceIPGroupDetails", required = true, nillable = true)
    protected UpdateSourceIPGroupDetails sourceIPGroupDetails;
    protected Boolean forceOverlapTransfer;
    @XmlAttribute(name = "directionalPoolRecordId", required = true)
    protected String directionalPoolRecordId;

    /**
     * Gets the value of the directionalRecordConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link DirectionalDNSRecordToUpdate }
     *     
     */
    public DirectionalDNSRecordToUpdate getDirectionalRecordConfiguration() {
        return directionalRecordConfiguration;
    }

    /**
     * Sets the value of the directionalRecordConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirectionalDNSRecordToUpdate }
     *     
     */
    public void setDirectionalRecordConfiguration(DirectionalDNSRecordToUpdate value) {
        this.directionalRecordConfiguration = value;
    }

    /**
     * Gets the value of the geolocationGroupDetails property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateGeolocationGroupDetails }
     *     
     */
    public UpdateGeolocationGroupDetails getGeolocationGroupDetails() {
        return geolocationGroupDetails;
    }

    /**
     * Sets the value of the geolocationGroupDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateGeolocationGroupDetails }
     *     
     */
    public void setGeolocationGroupDetails(UpdateGeolocationGroupDetails value) {
        this.geolocationGroupDetails = value;
    }

    /**
     * Gets the value of the sourceIPGroupDetails property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateSourceIPGroupDetails }
     *     
     */
    public UpdateSourceIPGroupDetails getSourceIPGroupDetails() {
        return sourceIPGroupDetails;
    }

    /**
     * Sets the value of the sourceIPGroupDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateSourceIPGroupDetails }
     *     
     */
    public void setSourceIPGroupDetails(UpdateSourceIPGroupDetails value) {
        this.sourceIPGroupDetails = value;
    }

    /**
     * Gets the value of the forceOverlapTransfer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isForceOverlapTransfer() {
        return forceOverlapTransfer;
    }

    /**
     * Sets the value of the forceOverlapTransfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setForceOverlapTransfer(Boolean value) {
        this.forceOverlapTransfer = value;
    }

    /**
     * Gets the value of the directionalPoolRecordId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirectionalPoolRecordId() {
        return directionalPoolRecordId;
    }

    /**
     * Sets the value of the directionalPoolRecordId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirectionalPoolRecordId(String value) {
        this.directionalPoolRecordId = value;
    }

}

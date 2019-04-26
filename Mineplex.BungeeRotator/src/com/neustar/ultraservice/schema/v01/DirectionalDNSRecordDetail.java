
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirectionalDNSRecordDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectionalDNSRecordDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DirectionalDNSRecord" type="{http://schema.ultraservice.neustar.com/v01/}DirectionalDNSRecord"/>
 *       &lt;/sequence>
 *       &lt;attribute name="GeolocationGroupName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="GeolocationGroupId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SourceIPGroupName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SourceIPGroupId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="GeolocationDescription" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SourceIPDescription" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TerritoriesCount" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DirPoolRecordId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="GroupName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="GroupId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ZoneName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectionalDNSRecordDetail", propOrder = {
    "directionalDNSRecord"
})
public class DirectionalDNSRecordDetail {

    @XmlElement(name = "DirectionalDNSRecord", required = true)
    protected DirectionalDNSRecord directionalDNSRecord;
    @XmlAttribute(name = "GeolocationGroupName")
    protected String geolocationGroupName;
    @XmlAttribute(name = "GeolocationGroupId")
    protected String geolocationGroupId;
    @XmlAttribute(name = "SourceIPGroupName")
    protected String sourceIPGroupName;
    @XmlAttribute(name = "SourceIPGroupId")
    protected String sourceIPGroupId;
    @XmlAttribute(name = "GeolocationDescription")
    protected String geolocationDescription;
    @XmlAttribute(name = "SourceIPDescription")
    protected String sourceIPDescription;
    @XmlAttribute(name = "TerritoriesCount")
    protected String territoriesCount;
    @XmlAttribute(name = "DirPoolRecordId", required = true)
    protected String dirPoolRecordId;
    @XmlAttribute(name = "GroupName")
    protected String groupName;
    @XmlAttribute(name = "GroupId")
    protected String groupId;
    @XmlAttribute(name = "Description")
    protected String description;
    @XmlAttribute(name = "ZoneName")
    protected String zoneName;
    @XmlAttribute(name = "DName")
    protected String dName;

    /**
     * Gets the value of the directionalDNSRecord property.
     * 
     * @return
     *     possible object is
     *     {@link DirectionalDNSRecord }
     *     
     */
    public DirectionalDNSRecord getDirectionalDNSRecord() {
        return directionalDNSRecord;
    }

    /**
     * Sets the value of the directionalDNSRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirectionalDNSRecord }
     *     
     */
    public void setDirectionalDNSRecord(DirectionalDNSRecord value) {
        this.directionalDNSRecord = value;
    }

    /**
     * Gets the value of the geolocationGroupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeolocationGroupName() {
        return geolocationGroupName;
    }

    /**
     * Sets the value of the geolocationGroupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeolocationGroupName(String value) {
        this.geolocationGroupName = value;
    }

    /**
     * Gets the value of the geolocationGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeolocationGroupId() {
        return geolocationGroupId;
    }

    /**
     * Sets the value of the geolocationGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeolocationGroupId(String value) {
        this.geolocationGroupId = value;
    }

    /**
     * Gets the value of the sourceIPGroupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceIPGroupName() {
        return sourceIPGroupName;
    }

    /**
     * Sets the value of the sourceIPGroupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceIPGroupName(String value) {
        this.sourceIPGroupName = value;
    }

    /**
     * Gets the value of the sourceIPGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceIPGroupId() {
        return sourceIPGroupId;
    }

    /**
     * Sets the value of the sourceIPGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceIPGroupId(String value) {
        this.sourceIPGroupId = value;
    }

    /**
     * Gets the value of the geolocationDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeolocationDescription() {
        return geolocationDescription;
    }

    /**
     * Sets the value of the geolocationDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeolocationDescription(String value) {
        this.geolocationDescription = value;
    }

    /**
     * Gets the value of the sourceIPDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceIPDescription() {
        return sourceIPDescription;
    }

    /**
     * Sets the value of the sourceIPDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceIPDescription(String value) {
        this.sourceIPDescription = value;
    }

    /**
     * Gets the value of the territoriesCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerritoriesCount() {
        return territoriesCount;
    }

    /**
     * Sets the value of the territoriesCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerritoriesCount(String value) {
        this.territoriesCount = value;
    }

    /**
     * Gets the value of the dirPoolRecordId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirPoolRecordId() {
        return dirPoolRecordId;
    }

    /**
     * Sets the value of the dirPoolRecordId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirPoolRecordId(String value) {
        this.dirPoolRecordId = value;
    }

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the groupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Sets the value of the groupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupId(String value) {
        this.groupId = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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
     * Gets the value of the dName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDName() {
        return dName;
    }

    /**
     * Sets the value of the dName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDName(String value) {
        this.dName = value;
    }

}

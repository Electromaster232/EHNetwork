
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SimpleFailoverPoolAdd complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SimpleFailoverPoolAdd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="zoneName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hostName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="liveRecord" type="{http://schema.ultraservice.neustar.com/v01/}FailoverRecord"/>
 *         &lt;element name="backupRecord" type="{http://schema.ultraservice.neustar.com/v01/}FailoverRecord"/>
 *         &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="monitor" type="{http://schema.ultraservice.neustar.com/v01/}FailoverMonitor"/>
 *         &lt;element name="regionThreshold" type="{http://schema.ultraservice.neustar.com/v01/}simpleFailoverPoolRegionThreshold"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleFailoverPoolAdd", propOrder = {
    "zoneName",
    "hostName",
    "description",
    "liveRecord",
    "backupRecord",
    "ttl",
    "monitor",
    "regionThreshold"
})
public class SimpleFailoverPoolAdd {

    @XmlElement(required = true)
    protected String zoneName;
    @XmlElement(required = true)
    protected String hostName;
    protected String description;
    @XmlElement(required = true)
    protected FailoverRecord liveRecord;
    @XmlElement(required = true)
    protected FailoverRecord backupRecord;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long ttl;
    @XmlElement(required = true)
    protected FailoverMonitor monitor;
    @XmlElement(required = true)
    protected SimpleFailoverPoolRegionThreshold regionThreshold;

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
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
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
     * Gets the value of the liveRecord property.
     * 
     * @return
     *     possible object is
     *     {@link FailoverRecord }
     *     
     */
    public FailoverRecord getLiveRecord() {
        return liveRecord;
    }

    /**
     * Sets the value of the liveRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link FailoverRecord }
     *     
     */
    public void setLiveRecord(FailoverRecord value) {
        this.liveRecord = value;
    }

    /**
     * Gets the value of the backupRecord property.
     * 
     * @return
     *     possible object is
     *     {@link FailoverRecord }
     *     
     */
    public FailoverRecord getBackupRecord() {
        return backupRecord;
    }

    /**
     * Sets the value of the backupRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link FailoverRecord }
     *     
     */
    public void setBackupRecord(FailoverRecord value) {
        this.backupRecord = value;
    }

    /**
     * Gets the value of the ttl property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTtl() {
        return ttl;
    }

    /**
     * Sets the value of the ttl property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTtl(Long value) {
        this.ttl = value;
    }

    /**
     * Gets the value of the monitor property.
     * 
     * @return
     *     possible object is
     *     {@link FailoverMonitor }
     *     
     */
    public FailoverMonitor getMonitor() {
        return monitor;
    }

    /**
     * Sets the value of the monitor property.
     * 
     * @param value
     *     allowed object is
     *     {@link FailoverMonitor }
     *     
     */
    public void setMonitor(FailoverMonitor value) {
        this.monitor = value;
    }

    /**
     * Gets the value of the regionThreshold property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleFailoverPoolRegionThreshold }
     *     
     */
    public SimpleFailoverPoolRegionThreshold getRegionThreshold() {
        return regionThreshold;
    }

    /**
     * Sets the value of the regionThreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleFailoverPoolRegionThreshold }
     *     
     */
    public void setRegionThreshold(SimpleFailoverPoolRegionThreshold value) {
        this.regionThreshold = value;
    }

}

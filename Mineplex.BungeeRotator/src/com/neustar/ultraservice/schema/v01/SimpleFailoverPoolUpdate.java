
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SimpleFailoverPoolUpdate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SimpleFailoverPoolUpdate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="liveRecord" type="{http://schema.ultraservice.neustar.com/v01/}FailoverRecordUpdate" minOccurs="0"/>
 *         &lt;element name="backupRecord" type="{http://schema.ultraservice.neustar.com/v01/}FailoverRecordUpdate" minOccurs="0"/>
 *         &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="monitor" type="{http://schema.ultraservice.neustar.com/v01/}FailoverMonitorUpdate" minOccurs="0"/>
 *         &lt;element name="regionThreshold" type="{http://schema.ultraservice.neustar.com/v01/}simpleFailoverPoolRegionThreshold" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleFailoverPoolUpdate", propOrder = {
    "description",
    "liveRecord",
    "backupRecord",
    "ttl",
    "monitor",
    "regionThreshold"
})
public class SimpleFailoverPoolUpdate {

    protected String description;
    protected FailoverRecordUpdate liveRecord;
    protected FailoverRecordUpdate backupRecord;
    protected Long ttl;
    protected FailoverMonitorUpdate monitor;
    protected SimpleFailoverPoolRegionThreshold regionThreshold;

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
     *     {@link FailoverRecordUpdate }
     *     
     */
    public FailoverRecordUpdate getLiveRecord() {
        return liveRecord;
    }

    /**
     * Sets the value of the liveRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link FailoverRecordUpdate }
     *     
     */
    public void setLiveRecord(FailoverRecordUpdate value) {
        this.liveRecord = value;
    }

    /**
     * Gets the value of the backupRecord property.
     * 
     * @return
     *     possible object is
     *     {@link FailoverRecordUpdate }
     *     
     */
    public FailoverRecordUpdate getBackupRecord() {
        return backupRecord;
    }

    /**
     * Sets the value of the backupRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link FailoverRecordUpdate }
     *     
     */
    public void setBackupRecord(FailoverRecordUpdate value) {
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
     *     {@link FailoverMonitorUpdate }
     *     
     */
    public FailoverMonitorUpdate getMonitor() {
        return monitor;
    }

    /**
     * Sets the value of the monitor property.
     * 
     * @param value
     *     allowed object is
     *     {@link FailoverMonitorUpdate }
     *     
     */
    public void setMonitor(FailoverMonitorUpdate value) {
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


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MonitoredRDPoolAdd complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MonitoredRDPoolAdd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="monitoredRDPoolKey" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolKey"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="poolRecords" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolRecordsAdd"/>
 *         &lt;element name="allFailRecord" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolRecordAdd" minOccurs="0"/>
 *         &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="monitor" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolMonitor"/>
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
@XmlType(name = "MonitoredRDPoolAdd", propOrder = {
    "monitoredRDPoolKey",
    "description",
    "poolRecords",
    "allFailRecord",
    "ttl",
    "monitor",
    "regionThreshold"
})
public class MonitoredRDPoolAdd {

    @XmlElement(required = true)
    protected MonitoredRDPoolKey monitoredRDPoolKey;
    protected String description;
    @XmlElement(required = true)
    protected MonitoredRDPoolRecordsAdd poolRecords;
    protected MonitoredRDPoolRecordAdd allFailRecord;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long ttl;
    @XmlElement(required = true)
    protected MonitoredRDPoolMonitor monitor;
    @XmlElement(required = true)
    protected SimpleFailoverPoolRegionThreshold regionThreshold;

    /**
     * Gets the value of the monitoredRDPoolKey property.
     * 
     * @return
     *     possible object is
     *     {@link MonitoredRDPoolKey }
     *     
     */
    public MonitoredRDPoolKey getMonitoredRDPoolKey() {
        return monitoredRDPoolKey;
    }

    /**
     * Sets the value of the monitoredRDPoolKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoredRDPoolKey }
     *     
     */
    public void setMonitoredRDPoolKey(MonitoredRDPoolKey value) {
        this.monitoredRDPoolKey = value;
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
     * Gets the value of the poolRecords property.
     * 
     * @return
     *     possible object is
     *     {@link MonitoredRDPoolRecordsAdd }
     *     
     */
    public MonitoredRDPoolRecordsAdd getPoolRecords() {
        return poolRecords;
    }

    /**
     * Sets the value of the poolRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoredRDPoolRecordsAdd }
     *     
     */
    public void setPoolRecords(MonitoredRDPoolRecordsAdd value) {
        this.poolRecords = value;
    }

    /**
     * Gets the value of the allFailRecord property.
     * 
     * @return
     *     possible object is
     *     {@link MonitoredRDPoolRecordAdd }
     *     
     */
    public MonitoredRDPoolRecordAdd getAllFailRecord() {
        return allFailRecord;
    }

    /**
     * Sets the value of the allFailRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoredRDPoolRecordAdd }
     *     
     */
    public void setAllFailRecord(MonitoredRDPoolRecordAdd value) {
        this.allFailRecord = value;
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
     *     {@link MonitoredRDPoolMonitor }
     *     
     */
    public MonitoredRDPoolMonitor getMonitor() {
        return monitor;
    }

    /**
     * Sets the value of the monitor property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoredRDPoolMonitor }
     *     
     */
    public void setMonitor(MonitoredRDPoolMonitor value) {
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

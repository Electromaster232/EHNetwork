
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MonitoredRDPoolUpdate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MonitoredRDPoolUpdate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="monitoredRDPoolKey" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolKey"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="poolRecords" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolRecords" minOccurs="0"/>
 *         &lt;element name="allFailRecord" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolAllFailRecordUpdate" minOccurs="0"/>
 *         &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="monitor" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolMonitorUpdate" minOccurs="0"/>
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
@XmlType(name = "MonitoredRDPoolUpdate", propOrder = {
    "monitoredRDPoolKey",
    "description",
    "poolRecords",
    "allFailRecord",
    "ttl",
    "monitor",
    "regionThreshold"
})
public class MonitoredRDPoolUpdate {

    @XmlElement(required = true)
    protected MonitoredRDPoolKey monitoredRDPoolKey;
    protected String description;
    protected MonitoredRDPoolRecords poolRecords;
    protected MonitoredRDPoolAllFailRecordUpdate allFailRecord;
    @XmlElementRef(name = "ttl", namespace = "http://schema.ultraservice.neustar.com/v01/", type = JAXBElement.class)
    protected JAXBElement<Long> ttl;
    protected MonitoredRDPoolMonitorUpdate monitor;
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
     *     {@link MonitoredRDPoolRecords }
     *     
     */
    public MonitoredRDPoolRecords getPoolRecords() {
        return poolRecords;
    }

    /**
     * Sets the value of the poolRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoredRDPoolRecords }
     *     
     */
    public void setPoolRecords(MonitoredRDPoolRecords value) {
        this.poolRecords = value;
    }

    /**
     * Gets the value of the allFailRecord property.
     * 
     * @return
     *     possible object is
     *     {@link MonitoredRDPoolAllFailRecordUpdate }
     *     
     */
    public MonitoredRDPoolAllFailRecordUpdate getAllFailRecord() {
        return allFailRecord;
    }

    /**
     * Sets the value of the allFailRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoredRDPoolAllFailRecordUpdate }
     *     
     */
    public void setAllFailRecord(MonitoredRDPoolAllFailRecordUpdate value) {
        this.allFailRecord = value;
    }

    /**
     * Gets the value of the ttl property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getTtl() {
        return ttl;
    }

    /**
     * Sets the value of the ttl property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setTtl(JAXBElement<Long> value) {
        this.ttl = value;
    }

    /**
     * Gets the value of the monitor property.
     * 
     * @return
     *     possible object is
     *     {@link MonitoredRDPoolMonitorUpdate }
     *     
     */
    public MonitoredRDPoolMonitorUpdate getMonitor() {
        return monitor;
    }

    /**
     * Sets the value of the monitor property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoredRDPoolMonitorUpdate }
     *     
     */
    public void setMonitor(MonitoredRDPoolMonitorUpdate value) {
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

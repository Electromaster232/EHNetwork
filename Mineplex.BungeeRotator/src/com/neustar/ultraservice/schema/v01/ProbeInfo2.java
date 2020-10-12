
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for probeInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="probeInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="allFailRecord" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolRecordAdd" minOccurs="0"/>
 *         &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
@XmlType(name = "probeInfo", propOrder = {
    "description",
    "allFailRecord",
    "ttl",
    "monitor",
    "regionThreshold"
})
public class ProbeInfo2 {

    protected String description;
    protected MonitoredRDPoolRecordAdd allFailRecord;
    @XmlElementRef(name = "ttl", namespace = "http://schema.ultraservice.neustar.com/v01/", type = JAXBElement.class)
    protected JAXBElement<Long> ttl;
    @XmlElement(required = true)
    protected MonitoredRDPoolMonitor monitor;
    @XmlElement(required = true)
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

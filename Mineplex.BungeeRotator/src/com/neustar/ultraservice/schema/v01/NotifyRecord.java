
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NotifyRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotifyRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="poolRecordId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="probeEvents" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="recordEvents" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="scheduledEvents" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotifyRecord")
public class NotifyRecord {

    @XmlAttribute(name = "poolRecordId", required = true)
    protected String poolRecordId;
    @XmlAttribute(name = "probeEvents", required = true)
    protected String probeEvents;
    @XmlAttribute(name = "recordEvents", required = true)
    protected String recordEvents;
    @XmlAttribute(name = "scheduledEvents", required = true)
    protected String scheduledEvents;

    /**
     * Gets the value of the poolRecordId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolRecordId() {
        return poolRecordId;
    }

    /**
     * Sets the value of the poolRecordId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolRecordId(String value) {
        this.poolRecordId = value;
    }

    /**
     * Gets the value of the probeEvents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeEvents() {
        return probeEvents;
    }

    /**
     * Sets the value of the probeEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeEvents(String value) {
        this.probeEvents = value;
    }

    /**
     * Gets the value of the recordEvents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordEvents() {
        return recordEvents;
    }

    /**
     * Sets the value of the recordEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordEvents(String value) {
        this.recordEvents = value;
    }

    /**
     * Gets the value of the scheduledEvents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduledEvents() {
        return scheduledEvents;
    }

    /**
     * Sets the value of the scheduledEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduledEvents(String value) {
        this.scheduledEvents = value;
    }

}

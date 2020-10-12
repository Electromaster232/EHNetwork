
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NotificationData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NotificationData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="email" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="poolRecordId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="poolNotifyId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="probeEvents" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="recordEvents" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="scheduleEvents" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationData")
public class NotificationData {

    @XmlAttribute(name = "email", required = true)
    protected String email;
    @XmlAttribute(name = "poolRecordId", required = true)
    protected String poolRecordId;
    @XmlAttribute(name = "poolNotifyId", required = true)
    protected String poolNotifyId;
    @XmlAttribute(name = "probeEvents", required = true)
    protected String probeEvents;
    @XmlAttribute(name = "recordEvents", required = true)
    protected String recordEvents;
    @XmlAttribute(name = "scheduleEvents", required = true)
    protected String scheduleEvents;

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

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
     * Gets the value of the poolNotifyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolNotifyId() {
        return poolNotifyId;
    }

    /**
     * Sets the value of the poolNotifyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolNotifyId(String value) {
        this.poolNotifyId = value;
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
     * Gets the value of the scheduleEvents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduleEvents() {
        return scheduleEvents;
    }

    /**
     * Sets the value of the scheduleEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduleEvents(String value) {
        this.scheduleEvents = value;
    }

}

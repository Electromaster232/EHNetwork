
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProbeAlertsData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProbeAlertsData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="IpAddress" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ProbeType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ProbeStatus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="AlertDate" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="FailOverOccured" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PoolDName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProbeAlertsData")
public class ProbeAlertsData {

    @XmlAttribute(name = "IpAddress")
    protected String ipAddress;
    @XmlAttribute(name = "ProbeType")
    protected String probeType;
    @XmlAttribute(name = "ProbeStatus")
    protected String probeStatus;
    @XmlAttribute(name = "AlertDate")
    protected String alertDate;
    @XmlAttribute(name = "FailOverOccured")
    protected String failOverOccured;
    @XmlAttribute(name = "PoolDName")
    protected String poolDName;
    @XmlAttribute(name = "Status")
    protected String status;

    /**
     * Gets the value of the ipAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the value of the ipAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpAddress(String value) {
        this.ipAddress = value;
    }

    /**
     * Gets the value of the probeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeType() {
        return probeType;
    }

    /**
     * Sets the value of the probeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeType(String value) {
        this.probeType = value;
    }

    /**
     * Gets the value of the probeStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeStatus() {
        return probeStatus;
    }

    /**
     * Sets the value of the probeStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeStatus(String value) {
        this.probeStatus = value;
    }

    /**
     * Gets the value of the alertDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlertDate() {
        return alertDate;
    }

    /**
     * Sets the value of the alertDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlertDate(String value) {
        this.alertDate = value;
    }

    /**
     * Gets the value of the failOverOccured property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailOverOccured() {
        return failOverOccured;
    }

    /**
     * Sets the value of the failOverOccured property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailOverOccured(String value) {
        this.failOverOccured = value;
    }

    /**
     * Gets the value of the poolDName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolDName() {
        return poolDName;
    }

    /**
     * Sets the value of the poolDName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolDName(String value) {
        this.poolDName = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

}

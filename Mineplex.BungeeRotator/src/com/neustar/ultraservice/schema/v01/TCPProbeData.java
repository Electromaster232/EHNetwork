
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TCPProbeData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TCPProbeData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="controlIPAddress" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="connectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failAverageConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalAverageConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningAverageConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TCPProbeData")
public class TCPProbeData {

    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "controlIPAddress")
    protected String controlIPAddress;
    @XmlAttribute(name = "connectTime")
    protected String connectTime;
    @XmlAttribute(name = "failConnectTime")
    protected String failConnectTime;
    @XmlAttribute(name = "criticalConnectTime")
    protected String criticalConnectTime;
    @XmlAttribute(name = "warningConnectTime")
    protected String warningConnectTime;
    @XmlAttribute(name = "failAverageConnectTime")
    protected String failAverageConnectTime;
    @XmlAttribute(name = "criticalAverageConnectTime")
    protected String criticalAverageConnectTime;
    @XmlAttribute(name = "warningAverageConnectTime")
    protected String warningAverageConnectTime;

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

    /**
     * Gets the value of the controlIPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControlIPAddress() {
        return controlIPAddress;
    }

    /**
     * Sets the value of the controlIPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControlIPAddress(String value) {
        this.controlIPAddress = value;
    }

    /**
     * Gets the value of the connectTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectTime() {
        return connectTime;
    }

    /**
     * Sets the value of the connectTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectTime(String value) {
        this.connectTime = value;
    }

    /**
     * Gets the value of the failConnectTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailConnectTime() {
        return failConnectTime;
    }

    /**
     * Sets the value of the failConnectTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailConnectTime(String value) {
        this.failConnectTime = value;
    }

    /**
     * Gets the value of the criticalConnectTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalConnectTime() {
        return criticalConnectTime;
    }

    /**
     * Sets the value of the criticalConnectTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalConnectTime(String value) {
        this.criticalConnectTime = value;
    }

    /**
     * Gets the value of the warningConnectTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningConnectTime() {
        return warningConnectTime;
    }

    /**
     * Sets the value of the warningConnectTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningConnectTime(String value) {
        this.warningConnectTime = value;
    }

    /**
     * Gets the value of the failAverageConnectTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailAverageConnectTime() {
        return failAverageConnectTime;
    }

    /**
     * Sets the value of the failAverageConnectTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailAverageConnectTime(String value) {
        this.failAverageConnectTime = value;
    }

    /**
     * Gets the value of the criticalAverageConnectTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalAverageConnectTime() {
        return criticalAverageConnectTime;
    }

    /**
     * Sets the value of the criticalAverageConnectTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalAverageConnectTime(String value) {
        this.criticalAverageConnectTime = value;
    }

    /**
     * Gets the value of the warningAverageConnectTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningAverageConnectTime() {
        return warningAverageConnectTime;
    }

    /**
     * Sets the value of the warningAverageConnectTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningAverageConnectTime(String value) {
        this.warningAverageConnectTime = value;
    }

}

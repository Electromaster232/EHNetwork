
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DNSProbeData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DNSProbeData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tcpOnly" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rrType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nameToQuery" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="response" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="runtime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failResponse" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalResponse" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningResponse" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DNSProbeData")
public class DNSProbeData {

    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "tcpOnly")
    protected String tcpOnly;
    @XmlAttribute(name = "rrType")
    protected String rrType;
    @XmlAttribute(name = "nameToQuery")
    protected String nameToQuery;
    @XmlAttribute(name = "response")
    protected String response;
    @XmlAttribute(name = "runtime")
    protected String runtime;
    @XmlAttribute(name = "failResponse")
    protected String failResponse;
    @XmlAttribute(name = "criticalResponse")
    protected String criticalResponse;
    @XmlAttribute(name = "warningResponse")
    protected String warningResponse;
    @XmlAttribute(name = "failAverageRunTime")
    protected String failAverageRunTime;
    @XmlAttribute(name = "criticalAverageRunTime")
    protected String criticalAverageRunTime;
    @XmlAttribute(name = "warningAverageRunTime")
    protected String warningAverageRunTime;
    @XmlAttribute(name = "failRuntime")
    protected String failRuntime;
    @XmlAttribute(name = "criticalRuntime")
    protected String criticalRuntime;
    @XmlAttribute(name = "warningRuntime")
    protected String warningRuntime;

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
     * Gets the value of the tcpOnly property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTcpOnly() {
        return tcpOnly;
    }

    /**
     * Sets the value of the tcpOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTcpOnly(String value) {
        this.tcpOnly = value;
    }

    /**
     * Gets the value of the rrType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRrType() {
        return rrType;
    }

    /**
     * Sets the value of the rrType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRrType(String value) {
        this.rrType = value;
    }

    /**
     * Gets the value of the nameToQuery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameToQuery() {
        return nameToQuery;
    }

    /**
     * Sets the value of the nameToQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameToQuery(String value) {
        this.nameToQuery = value;
    }

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponse(String value) {
        this.response = value;
    }

    /**
     * Gets the value of the runtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     * Sets the value of the runtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuntime(String value) {
        this.runtime = value;
    }

    /**
     * Gets the value of the failResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailResponse() {
        return failResponse;
    }

    /**
     * Sets the value of the failResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailResponse(String value) {
        this.failResponse = value;
    }

    /**
     * Gets the value of the criticalResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalResponse() {
        return criticalResponse;
    }

    /**
     * Sets the value of the criticalResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalResponse(String value) {
        this.criticalResponse = value;
    }

    /**
     * Gets the value of the warningResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningResponse() {
        return warningResponse;
    }

    /**
     * Sets the value of the warningResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningResponse(String value) {
        this.warningResponse = value;
    }

    /**
     * Gets the value of the failAverageRunTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailAverageRunTime() {
        return failAverageRunTime;
    }

    /**
     * Sets the value of the failAverageRunTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailAverageRunTime(String value) {
        this.failAverageRunTime = value;
    }

    /**
     * Gets the value of the criticalAverageRunTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalAverageRunTime() {
        return criticalAverageRunTime;
    }

    /**
     * Sets the value of the criticalAverageRunTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalAverageRunTime(String value) {
        this.criticalAverageRunTime = value;
    }

    /**
     * Gets the value of the warningAverageRunTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningAverageRunTime() {
        return warningAverageRunTime;
    }

    /**
     * Sets the value of the warningAverageRunTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningAverageRunTime(String value) {
        this.warningAverageRunTime = value;
    }

    /**
     * Gets the value of the failRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailRuntime() {
        return failRuntime;
    }

    /**
     * Sets the value of the failRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailRuntime(String value) {
        this.failRuntime = value;
    }

    /**
     * Gets the value of the criticalRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalRuntime() {
        return criticalRuntime;
    }

    /**
     * Sets the value of the criticalRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalRuntime(String value) {
        this.criticalRuntime = value;
    }

    /**
     * Gets the value of the warningRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningRuntime() {
        return warningRuntime;
    }

    /**
     * Sets the value of the warningRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningRuntime(String value) {
        this.warningRuntime = value;
    }

}

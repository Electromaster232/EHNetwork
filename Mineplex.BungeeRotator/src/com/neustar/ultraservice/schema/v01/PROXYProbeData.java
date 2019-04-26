
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PROXYProbeData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PROXYProbeData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="proxyPort" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="url" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="connectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="runtime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failTotalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalTotalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningTotalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failAverageConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalAverageConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningAverageConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PROXYProbeData")
public class PROXYProbeData {

    @XmlAttribute(name = "proxyPort")
    protected String proxyPort;
    @XmlAttribute(name = "url")
    protected String url;
    @XmlAttribute(name = "totalRuntime")
    protected String totalRuntime;
    @XmlAttribute(name = "connectTime")
    protected String connectTime;
    @XmlAttribute(name = "runtime")
    protected String runtime;
    @XmlAttribute(name = "failTotalRuntime")
    protected String failTotalRuntime;
    @XmlAttribute(name = "criticalTotalRuntime")
    protected String criticalTotalRuntime;
    @XmlAttribute(name = "warningTotalRuntime")
    protected String warningTotalRuntime;
    @XmlAttribute(name = "failConnectTime")
    protected String failConnectTime;
    @XmlAttribute(name = "criticalConnectTime")
    protected String criticalConnectTime;
    @XmlAttribute(name = "warningConnectTime")
    protected String warningConnectTime;
    @XmlAttribute(name = "failRuntime")
    protected String failRuntime;
    @XmlAttribute(name = "criticalRuntime")
    protected String criticalRuntime;
    @XmlAttribute(name = "warningRuntime")
    protected String warningRuntime;
    @XmlAttribute(name = "failAverageConnectTime")
    protected String failAverageConnectTime;
    @XmlAttribute(name = "criticalAverageConnectTime")
    protected String criticalAverageConnectTime;
    @XmlAttribute(name = "warningAverageConnectTime")
    protected String warningAverageConnectTime;
    @XmlAttribute(name = "failAverageRunTime")
    protected String failAverageRunTime;
    @XmlAttribute(name = "criticalAverageRunTime")
    protected String criticalAverageRunTime;
    @XmlAttribute(name = "warningAverageRunTime")
    protected String warningAverageRunTime;

    /**
     * Gets the value of the proxyPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProxyPort() {
        return proxyPort;
    }

    /**
     * Sets the value of the proxyPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProxyPort(String value) {
        this.proxyPort = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the totalRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalRuntime() {
        return totalRuntime;
    }

    /**
     * Sets the value of the totalRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalRuntime(String value) {
        this.totalRuntime = value;
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
     * Gets the value of the failTotalRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailTotalRuntime() {
        return failTotalRuntime;
    }

    /**
     * Sets the value of the failTotalRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailTotalRuntime(String value) {
        this.failTotalRuntime = value;
    }

    /**
     * Gets the value of the criticalTotalRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalTotalRuntime() {
        return criticalTotalRuntime;
    }

    /**
     * Sets the value of the criticalTotalRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalTotalRuntime(String value) {
        this.criticalTotalRuntime = value;
    }

    /**
     * Gets the value of the warningTotalRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningTotalRuntime() {
        return warningTotalRuntime;
    }

    /**
     * Sets the value of the warningTotalRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningTotalRuntime(String value) {
        this.warningTotalRuntime = value;
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

}

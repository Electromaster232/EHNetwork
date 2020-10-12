
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HTTPProbeData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HTTPProbeData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="addTransaction" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="editTransactionStep" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="hostName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="webPage" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="protocol" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="method" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="transmittedData" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="searchString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="connectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="runtime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failTotalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalTotalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningTotalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failSearchString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalSearchString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningSearchString" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlType(name = "HTTPProbeData")
public class HTTPProbeData {

    @XmlAttribute(name = "addTransaction")
    protected String addTransaction;
    @XmlAttribute(name = "editTransactionStep")
    protected String editTransactionStep;
    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "hostName")
    protected String hostName;
    @XmlAttribute(name = "webPage")
    protected String webPage;
    @XmlAttribute(name = "protocol")
    protected String protocol;
    @XmlAttribute(name = "method")
    protected String method;
    @XmlAttribute(name = "transmittedData")
    protected String transmittedData;
    @XmlAttribute(name = "searchString")
    protected String searchString;
    @XmlAttribute(name = "connectTime")
    protected String connectTime;
    @XmlAttribute(name = "runtime")
    protected String runtime;
    @XmlAttribute(name = "totalRuntime")
    protected String totalRuntime;
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
    @XmlAttribute(name = "failSearchString")
    protected String failSearchString;
    @XmlAttribute(name = "criticalSearchString")
    protected String criticalSearchString;
    @XmlAttribute(name = "warningSearchString")
    protected String warningSearchString;
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
     * Gets the value of the addTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddTransaction() {
        return addTransaction;
    }

    /**
     * Sets the value of the addTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddTransaction(String value) {
        this.addTransaction = value;
    }

    /**
     * Gets the value of the editTransactionStep property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEditTransactionStep() {
        return editTransactionStep;
    }

    /**
     * Sets the value of the editTransactionStep property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEditTransactionStep(String value) {
        this.editTransactionStep = value;
    }

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
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the webPage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebPage() {
        return webPage;
    }

    /**
     * Sets the value of the webPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebPage(String value) {
        this.webPage = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocol(String value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

    /**
     * Gets the value of the transmittedData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmittedData() {
        return transmittedData;
    }

    /**
     * Sets the value of the transmittedData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmittedData(String value) {
        this.transmittedData = value;
    }

    /**
     * Gets the value of the searchString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * Sets the value of the searchString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchString(String value) {
        this.searchString = value;
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
     * Gets the value of the failSearchString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailSearchString() {
        return failSearchString;
    }

    /**
     * Sets the value of the failSearchString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailSearchString(String value) {
        this.failSearchString = value;
    }

    /**
     * Gets the value of the criticalSearchString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalSearchString() {
        return criticalSearchString;
    }

    /**
     * Sets the value of the criticalSearchString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalSearchString(String value) {
        this.criticalSearchString = value;
    }

    /**
     * Gets the value of the warningSearchString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningSearchString() {
        return warningSearchString;
    }

    /**
     * Sets the value of the warningSearchString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningSearchString(String value) {
        this.warningSearchString = value;
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

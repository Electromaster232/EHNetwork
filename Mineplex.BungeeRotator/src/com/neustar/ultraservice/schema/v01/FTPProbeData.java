
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FTPProbeData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FTPProbeData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="passiveMode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="userName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pathToFile" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="searchString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="connectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="runtime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failSearchString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalSearchString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningSearchString" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failAverageConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalAverageConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningAverageConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningConnectTime" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlType(name = "FTPProbeData")
public class FTPProbeData {

    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "passiveMode")
    protected String passiveMode;
    @XmlAttribute(name = "userName")
    protected String userName;
    @XmlAttribute(name = "password")
    protected String password;
    @XmlAttribute(name = "pathToFile")
    protected String pathToFile;
    @XmlAttribute(name = "searchString")
    protected String searchString;
    @XmlAttribute(name = "connectTime")
    protected String connectTime;
    @XmlAttribute(name = "runtime")
    protected String runtime;
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
     * Gets the value of the passiveMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassiveMode() {
        return passiveMode;
    }

    /**
     * Sets the value of the passiveMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassiveMode(String value) {
        this.passiveMode = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the pathToFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathToFile() {
        return pathToFile;
    }

    /**
     * Sets the value of the pathToFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathToFile(String value) {
        this.pathToFile = value;
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

}

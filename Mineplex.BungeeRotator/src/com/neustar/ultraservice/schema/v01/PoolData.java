
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="PoolName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LBPoolId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PoolId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PoolType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PoolRecordType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Bleid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PoolStatus" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PoolDName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="FailOver" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Probing" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="MinActiveServers" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="MaxActiveServers" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ResponseMethod" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="MaxResponse" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="configured" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolData")
public class PoolData {

    @XmlAttribute(name = "PoolName", required = true)
    protected String poolName;
    @XmlAttribute(name = "LBPoolId", required = true)
    protected String lbPoolId;
    @XmlAttribute(name = "PoolId", required = true)
    protected String poolId;
    @XmlAttribute(name = "PoolType", required = true)
    protected String poolType;
    @XmlAttribute(name = "PoolRecordType", required = true)
    protected String poolRecordType;
    @XmlAttribute(name = "Bleid", required = true)
    protected String bleid;
    @XmlAttribute(name = "PoolStatus", required = true)
    protected String poolStatus;
    @XmlAttribute(name = "PoolDName", required = true)
    protected String poolDName;
    @XmlAttribute(name = "FailOver", required = true)
    protected String failOver;
    @XmlAttribute(name = "Probing", required = true)
    protected String probing;
    @XmlAttribute(name = "MinActiveServers", required = true)
    protected String minActiveServers;
    @XmlAttribute(name = "MaxActiveServers", required = true)
    protected String maxActiveServers;
    @XmlAttribute(name = "ResponseMethod")
    protected String responseMethod;
    @XmlAttribute(name = "MaxResponse")
    protected String maxResponse;
    @XmlAttribute(name = "description", required = true)
    protected String description;
    @XmlAttribute(name = "configured", required = true)
    protected String configured;

    /**
     * Gets the value of the poolName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * Sets the value of the poolName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolName(String value) {
        this.poolName = value;
    }

    /**
     * Gets the value of the lbPoolId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLBPoolId() {
        return lbPoolId;
    }

    /**
     * Sets the value of the lbPoolId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLBPoolId(String value) {
        this.lbPoolId = value;
    }

    /**
     * Gets the value of the poolId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolId() {
        return poolId;
    }

    /**
     * Sets the value of the poolId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolId(String value) {
        this.poolId = value;
    }

    /**
     * Gets the value of the poolType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolType() {
        return poolType;
    }

    /**
     * Sets the value of the poolType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolType(String value) {
        this.poolType = value;
    }

    /**
     * Gets the value of the poolRecordType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolRecordType() {
        return poolRecordType;
    }

    /**
     * Sets the value of the poolRecordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolRecordType(String value) {
        this.poolRecordType = value;
    }

    /**
     * Gets the value of the bleid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBleid() {
        return bleid;
    }

    /**
     * Sets the value of the bleid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBleid(String value) {
        this.bleid = value;
    }

    /**
     * Gets the value of the poolStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolStatus() {
        return poolStatus;
    }

    /**
     * Sets the value of the poolStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolStatus(String value) {
        this.poolStatus = value;
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
     * Gets the value of the failOver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailOver() {
        return failOver;
    }

    /**
     * Sets the value of the failOver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailOver(String value) {
        this.failOver = value;
    }

    /**
     * Gets the value of the probing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbing() {
        return probing;
    }

    /**
     * Sets the value of the probing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbing(String value) {
        this.probing = value;
    }

    /**
     * Gets the value of the minActiveServers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinActiveServers() {
        return minActiveServers;
    }

    /**
     * Sets the value of the minActiveServers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinActiveServers(String value) {
        this.minActiveServers = value;
    }

    /**
     * Gets the value of the maxActiveServers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxActiveServers() {
        return maxActiveServers;
    }

    /**
     * Sets the value of the maxActiveServers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxActiveServers(String value) {
        this.maxActiveServers = value;
    }

    /**
     * Gets the value of the responseMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseMethod() {
        return responseMethod;
    }

    /**
     * Sets the value of the responseMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseMethod(String value) {
        this.responseMethod = value;
    }

    /**
     * Gets the value of the maxResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxResponse() {
        return maxResponse;
    }

    /**
     * Sets the value of the maxResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxResponse(String value) {
        this.maxResponse = value;
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
     * Gets the value of the configured property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigured() {
        return configured;
    }

    /**
     * Sets the value of the configured property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigured(String value) {
        this.configured = value;
    }

}

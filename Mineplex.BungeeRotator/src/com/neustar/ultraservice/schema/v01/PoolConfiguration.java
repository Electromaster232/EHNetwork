
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="failoverEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="probingEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="responseMethod" type="{http://schema.ultraservice.neustar.com/v01/}ResponseMethod"/>
 *         &lt;element name="maxActive" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="maxResponse" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="effectiveMaxResponse" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="probesShared" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="rulesShared" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prioritizedRecords" type="{http://schema.ultraservice.neustar.com/v01/}PrioritizedRecordList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolConfiguration", propOrder = {
    "name",
    "failoverEnabled",
    "probingEnabled",
    "responseMethod",
    "maxActive",
    "maxResponse",
    "effectiveMaxResponse",
    "probesShared",
    "rulesShared",
    "ttl",
    "description",
    "prioritizedRecords"
})
public class PoolConfiguration {

    @XmlElement(required = true)
    protected String name;
    protected boolean failoverEnabled;
    protected boolean probingEnabled;
    @XmlElement(required = true)
    protected ResponseMethod responseMethod;
    protected long maxActive;
    protected long maxResponse;
    protected long effectiveMaxResponse;
    protected boolean probesShared;
    protected boolean rulesShared;
    protected long ttl;
    protected String description;
    protected PrioritizedRecordList prioritizedRecords;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the failoverEnabled property.
     * 
     */
    public boolean isFailoverEnabled() {
        return failoverEnabled;
    }

    /**
     * Sets the value of the failoverEnabled property.
     * 
     */
    public void setFailoverEnabled(boolean value) {
        this.failoverEnabled = value;
    }

    /**
     * Gets the value of the probingEnabled property.
     * 
     */
    public boolean isProbingEnabled() {
        return probingEnabled;
    }

    /**
     * Sets the value of the probingEnabled property.
     * 
     */
    public void setProbingEnabled(boolean value) {
        this.probingEnabled = value;
    }

    /**
     * Gets the value of the responseMethod property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseMethod }
     *     
     */
    public ResponseMethod getResponseMethod() {
        return responseMethod;
    }

    /**
     * Sets the value of the responseMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseMethod }
     *     
     */
    public void setResponseMethod(ResponseMethod value) {
        this.responseMethod = value;
    }

    /**
     * Gets the value of the maxActive property.
     * 
     */
    public long getMaxActive() {
        return maxActive;
    }

    /**
     * Sets the value of the maxActive property.
     * 
     */
    public void setMaxActive(long value) {
        this.maxActive = value;
    }

    /**
     * Gets the value of the maxResponse property.
     * 
     */
    public long getMaxResponse() {
        return maxResponse;
    }

    /**
     * Sets the value of the maxResponse property.
     * 
     */
    public void setMaxResponse(long value) {
        this.maxResponse = value;
    }

    /**
     * Gets the value of the effectiveMaxResponse property.
     * 
     */
    public long getEffectiveMaxResponse() {
        return effectiveMaxResponse;
    }

    /**
     * Sets the value of the effectiveMaxResponse property.
     * 
     */
    public void setEffectiveMaxResponse(long value) {
        this.effectiveMaxResponse = value;
    }

    /**
     * Gets the value of the probesShared property.
     * 
     */
    public boolean isProbesShared() {
        return probesShared;
    }

    /**
     * Sets the value of the probesShared property.
     * 
     */
    public void setProbesShared(boolean value) {
        this.probesShared = value;
    }

    /**
     * Gets the value of the rulesShared property.
     * 
     */
    public boolean isRulesShared() {
        return rulesShared;
    }

    /**
     * Sets the value of the rulesShared property.
     * 
     */
    public void setRulesShared(boolean value) {
        this.rulesShared = value;
    }

    /**
     * Gets the value of the ttl property.
     * 
     */
    public long getTtl() {
        return ttl;
    }

    /**
     * Sets the value of the ttl property.
     * 
     */
    public void setTtl(long value) {
        this.ttl = value;
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
     * Gets the value of the prioritizedRecords property.
     * 
     * @return
     *     possible object is
     *     {@link PrioritizedRecordList }
     *     
     */
    public PrioritizedRecordList getPrioritizedRecords() {
        return prioritizedRecords;
    }

    /**
     * Sets the value of the prioritizedRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrioritizedRecordList }
     *     
     */
    public void setPrioritizedRecords(PrioritizedRecordList value) {
        this.prioritizedRecords = value;
    }

}

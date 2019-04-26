
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARPoolConfigurationUpdate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ARPoolConfigurationUpdate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="failoverEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="probingEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="responseMethod" type="{http://schema.ultraservice.neustar.com/v01/}ResponseMethod" minOccurs="0"/>
 *         &lt;element name="maxActive" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="maxResponse" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="effectiveMaxResponse" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
@XmlType(name = "ARPoolConfigurationUpdate", propOrder = {
    "failoverEnabled",
    "probingEnabled",
    "responseMethod",
    "maxActive",
    "maxResponse",
    "effectiveMaxResponse",
    "ttl",
    "description",
    "prioritizedRecords"
})
public class ARPoolConfigurationUpdate {

    protected Boolean failoverEnabled;
    protected Boolean probingEnabled;
    protected ResponseMethod responseMethod;
    protected Long maxActive;
    protected Long maxResponse;
    protected Long effectiveMaxResponse;
    protected Long ttl;
    protected String description;
    protected PrioritizedRecordList prioritizedRecords;

    /**
     * Gets the value of the failoverEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFailoverEnabled() {
        return failoverEnabled;
    }

    /**
     * Sets the value of the failoverEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFailoverEnabled(Boolean value) {
        this.failoverEnabled = value;
    }

    /**
     * Gets the value of the probingEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isProbingEnabled() {
        return probingEnabled;
    }

    /**
     * Sets the value of the probingEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setProbingEnabled(Boolean value) {
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
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMaxActive() {
        return maxActive;
    }

    /**
     * Sets the value of the maxActive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMaxActive(Long value) {
        this.maxActive = value;
    }

    /**
     * Gets the value of the maxResponse property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMaxResponse() {
        return maxResponse;
    }

    /**
     * Sets the value of the maxResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMaxResponse(Long value) {
        this.maxResponse = value;
    }

    /**
     * Gets the value of the effectiveMaxResponse property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEffectiveMaxResponse() {
        return effectiveMaxResponse;
    }

    /**
     * Sets the value of the effectiveMaxResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEffectiveMaxResponse(Long value) {
        this.effectiveMaxResponse = value;
    }

    /**
     * Gets the value of the ttl property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTtl() {
        return ttl;
    }

    /**
     * Sets the value of the ttl property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTtl(Long value) {
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

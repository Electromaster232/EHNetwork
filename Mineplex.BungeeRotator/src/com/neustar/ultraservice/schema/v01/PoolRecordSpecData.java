
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolRecordSpecData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolRecordSpecData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="poolRecordID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="poolId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="recordState" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="probing" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="allFail" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="weight" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failOverDelay" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="threshold" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ttl" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolRecordSpecData")
public class PoolRecordSpecData {

    @XmlAttribute(name = "poolRecordID", required = true)
    protected String poolRecordID;
    @XmlAttribute(name = "poolId", required = true)
    protected String poolId;
    @XmlAttribute(name = "description", required = true)
    protected String description;
    @XmlAttribute(name = "recordState", required = true)
    protected String recordState;
    @XmlAttribute(name = "probing", required = true)
    protected String probing;
    @XmlAttribute(name = "allFail", required = true)
    protected String allFail;
    @XmlAttribute(name = "weight", required = true)
    protected String weight;
    @XmlAttribute(name = "failOverDelay", required = true)
    protected long failOverDelay;
    @XmlAttribute(name = "threshold", required = true)
    protected String threshold;
    @XmlAttribute(name = "ttl", required = true)
    protected String ttl;

    /**
     * Gets the value of the poolRecordID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolRecordID() {
        return poolRecordID;
    }

    /**
     * Sets the value of the poolRecordID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolRecordID(String value) {
        this.poolRecordID = value;
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
     * Gets the value of the recordState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordState() {
        return recordState;
    }

    /**
     * Sets the value of the recordState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordState(String value) {
        this.recordState = value;
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
     * Gets the value of the allFail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllFail() {
        return allFail;
    }

    /**
     * Sets the value of the allFail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllFail(String value) {
        this.allFail = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeight(String value) {
        this.weight = value;
    }

    /**
     * Gets the value of the failOverDelay property.
     * 
     */
    public long getFailOverDelay() {
        return failOverDelay;
    }

    /**
     * Sets the value of the failOverDelay property.
     * 
     */
    public void setFailOverDelay(long value) {
        this.failOverDelay = value;
    }

    /**
     * Gets the value of the threshold property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThreshold() {
        return threshold;
    }

    /**
     * Sets the value of the threshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThreshold(String value) {
        this.threshold = value;
    }

    /**
     * Gets the value of the ttl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTtl() {
        return ttl;
    }

    /**
     * Sets the value of the ttl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTtl(String value) {
        this.ttl = value;
    }

}

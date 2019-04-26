
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolRecordData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolRecordData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="subPoolId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="poolRecordID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="poolId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pointsTo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="weight" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="priority" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="recordType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="forceAnswer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="probing" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="status" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="serving" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolRecordData")
public class PoolRecordData {

    @XmlAttribute(name = "subPoolId")
    protected String subPoolId;
    @XmlAttribute(name = "poolRecordID", required = true)
    protected String poolRecordID;
    @XmlAttribute(name = "poolId", required = true)
    protected String poolId;
    @XmlAttribute(name = "pointsTo", required = true)
    protected String pointsTo;
    @XmlAttribute(name = "weight")
    protected String weight;
    @XmlAttribute(name = "priority", required = true)
    protected String priority;
    @XmlAttribute(name = "recordType", required = true)
    protected String recordType;
    @XmlAttribute(name = "forceAnswer", required = true)
    protected String forceAnswer;
    @XmlAttribute(name = "probing", required = true)
    protected String probing;
    @XmlAttribute(name = "status", required = true)
    protected String status;
    @XmlAttribute(name = "serving", required = true)
    protected String serving;
    @XmlAttribute(name = "description", required = true)
    protected String description;

    /**
     * Gets the value of the subPoolId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubPoolId() {
        return subPoolId;
    }

    /**
     * Sets the value of the subPoolId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubPoolId(String value) {
        this.subPoolId = value;
    }

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
     * Gets the value of the pointsTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPointsTo() {
        return pointsTo;
    }

    /**
     * Sets the value of the pointsTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPointsTo(String value) {
        this.pointsTo = value;
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
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the recordType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordType() {
        return recordType;
    }

    /**
     * Sets the value of the recordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordType(String value) {
        this.recordType = value;
    }

    /**
     * Gets the value of the forceAnswer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForceAnswer() {
        return forceAnswer;
    }

    /**
     * Sets the value of the forceAnswer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForceAnswer(String value) {
        this.forceAnswer = value;
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

    /**
     * Gets the value of the serving property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServing() {
        return serving;
    }

    /**
     * Sets the value of the serving property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServing(String value) {
        this.serving = value;
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

}

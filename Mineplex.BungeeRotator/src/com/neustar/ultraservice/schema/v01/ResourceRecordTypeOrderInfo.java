
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceRecordTypeOrderInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceRecordTypeOrderInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="recordOrder" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="recordType" use="required" type="{http://schema.ultraservice.neustar.com/v01/}ResourceRecordType" />
 *       &lt;attribute name="recordDisplay" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceRecordTypeOrderInfo")
public class ResourceRecordTypeOrderInfo {

    @XmlAttribute(name = "recordOrder", required = true)
    protected int recordOrder;
    @XmlAttribute(name = "recordType", required = true)
    protected ResourceRecordType recordType;
    @XmlAttribute(name = "recordDisplay", required = true)
    protected boolean recordDisplay;

    /**
     * Gets the value of the recordOrder property.
     * 
     */
    public int getRecordOrder() {
        return recordOrder;
    }

    /**
     * Sets the value of the recordOrder property.
     * 
     */
    public void setRecordOrder(int value) {
        this.recordOrder = value;
    }

    /**
     * Gets the value of the recordType property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceRecordType }
     *     
     */
    public ResourceRecordType getRecordType() {
        return recordType;
    }

    /**
     * Sets the value of the recordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceRecordType }
     *     
     */
    public void setRecordType(ResourceRecordType value) {
        this.recordType = value;
    }

    /**
     * Gets the value of the recordDisplay property.
     * 
     */
    public boolean isRecordDisplay() {
        return recordDisplay;
    }

    /**
     * Sets the value of the recordDisplay property.
     * 
     */
    public void setRecordDisplay(boolean value) {
        this.recordDisplay = value;
    }

}

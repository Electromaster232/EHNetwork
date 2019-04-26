
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlertPrioritizedRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AlertPrioritizedRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="oldRecordState" type="{http://schema.ultraservice.neustar.com/v01/}RecordState"/>
 *         &lt;element name="newRecordState" type="{http://schema.ultraservice.neustar.com/v01/}RecordState"/>
 *       &lt;/sequence>
 *       &lt;attribute name="recordData" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="recordType" use="required" type="{http://schema.ultraservice.neustar.com/v01/}RecordType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlertPrioritizedRecord", propOrder = {
    "oldRecordState",
    "newRecordState"
})
public class AlertPrioritizedRecord {

    @XmlElement(required = true)
    protected RecordState oldRecordState;
    @XmlElement(required = true)
    protected RecordState newRecordState;
    @XmlAttribute(name = "recordData", required = true)
    protected String recordData;
    @XmlAttribute(name = "recordType", required = true)
    protected RecordType recordType;

    /**
     * Gets the value of the oldRecordState property.
     * 
     * @return
     *     possible object is
     *     {@link RecordState }
     *     
     */
    public RecordState getOldRecordState() {
        return oldRecordState;
    }

    /**
     * Sets the value of the oldRecordState property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordState }
     *     
     */
    public void setOldRecordState(RecordState value) {
        this.oldRecordState = value;
    }

    /**
     * Gets the value of the newRecordState property.
     * 
     * @return
     *     possible object is
     *     {@link RecordState }
     *     
     */
    public RecordState getNewRecordState() {
        return newRecordState;
    }

    /**
     * Sets the value of the newRecordState property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordState }
     *     
     */
    public void setNewRecordState(RecordState value) {
        this.newRecordState = value;
    }

    /**
     * Gets the value of the recordData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordData() {
        return recordData;
    }

    /**
     * Sets the value of the recordData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordData(String value) {
        this.recordData = value;
    }

    /**
     * Gets the value of the recordType property.
     * 
     * @return
     *     possible object is
     *     {@link RecordType }
     *     
     */
    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * Sets the value of the recordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordType }
     *     
     */
    public void setRecordType(RecordType value) {
        this.recordType = value;
    }

}

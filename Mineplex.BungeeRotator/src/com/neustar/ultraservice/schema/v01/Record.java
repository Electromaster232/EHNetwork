
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Record complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Record">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ARecord" type="{http://schema.ultraservice.neustar.com/v01/}ARecord"/>
 *         &lt;element name="CNAMERecord" type="{http://schema.ultraservice.neustar.com/v01/}CNAMERecord"/>
 *         &lt;element name="SubPoolRecord" type="{http://schema.ultraservice.neustar.com/v01/}SUBPOOLRecord"/>
 *       &lt;/sequence>
 *       &lt;attribute name="recordType" use="required" type="{http://schema.ultraservice.neustar.com/v01/}RecordType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Record", propOrder = {
    "aRecord",
    "cnameRecord",
    "subPoolRecord"
})
public class Record {

    @XmlElement(name = "ARecord", required = true)
    protected ARecord aRecord;
    @XmlElement(name = "CNAMERecord", required = true)
    protected CNAMERecord cnameRecord;
    @XmlElement(name = "SubPoolRecord", required = true)
    protected SUBPOOLRecord subPoolRecord;
    @XmlAttribute(name = "recordType", required = true)
    protected RecordType recordType;

    /**
     * Gets the value of the aRecord property.
     * 
     * @return
     *     possible object is
     *     {@link ARecord }
     *     
     */
    public ARecord getARecord() {
        return aRecord;
    }

    /**
     * Sets the value of the aRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link ARecord }
     *     
     */
    public void setARecord(ARecord value) {
        this.aRecord = value;
    }

    /**
     * Gets the value of the cnameRecord property.
     * 
     * @return
     *     possible object is
     *     {@link CNAMERecord }
     *     
     */
    public CNAMERecord getCNAMERecord() {
        return cnameRecord;
    }

    /**
     * Sets the value of the cnameRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link CNAMERecord }
     *     
     */
    public void setCNAMERecord(CNAMERecord value) {
        this.cnameRecord = value;
    }

    /**
     * Gets the value of the subPoolRecord property.
     * 
     * @return
     *     possible object is
     *     {@link SUBPOOLRecord }
     *     
     */
    public SUBPOOLRecord getSubPoolRecord() {
        return subPoolRecord;
    }

    /**
     * Sets the value of the subPoolRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link SUBPOOLRecord }
     *     
     */
    public void setSubPoolRecord(SUBPOOLRecord value) {
        this.subPoolRecord = value;
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

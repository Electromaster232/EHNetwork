
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlertAllFailDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AlertAllFailDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="oldRecordState" type="{http://schema.ultraservice.neustar.com/v01/}RecordState"/>
 *         &lt;element name="newRecordState" type="{http://schema.ultraservice.neustar.com/v01/}RecordState"/>
 *         &lt;element name="allFailRecords" type="{http://schema.ultraservice.neustar.com/v01/}AlertAllFailRecordsList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlertAllFailDetails", propOrder = {
    "oldRecordState",
    "newRecordState",
    "allFailRecords"
})
public class AlertAllFailDetails {

    @XmlElement(required = true)
    protected RecordState oldRecordState;
    @XmlElement(required = true)
    protected RecordState newRecordState;
    protected AlertAllFailRecordsList allFailRecords;

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
     * Gets the value of the allFailRecords property.
     * 
     * @return
     *     possible object is
     *     {@link AlertAllFailRecordsList }
     *     
     */
    public AlertAllFailRecordsList getAllFailRecords() {
        return allFailRecords;
    }

    /**
     * Sets the value of the allFailRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlertAllFailRecordsList }
     *     
     */
    public void setAllFailRecords(AlertAllFailRecordsList value) {
        this.allFailRecords = value;
    }

}

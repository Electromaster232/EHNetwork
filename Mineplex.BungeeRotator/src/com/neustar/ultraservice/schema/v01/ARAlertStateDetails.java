
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARAlertStateDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ARAlertStateDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="oldRecordState" type="{http://schema.ultraservice.neustar.com/v01/}RecordState"/>
 *         &lt;element name="newRecordState" type="{http://schema.ultraservice.neustar.com/v01/}RecordState"/>
 *         &lt;element name="prioritizedRecords" type="{http://schema.ultraservice.neustar.com/v01/}PrioritizedRecordsList"/>
 *         &lt;element name="allFailDetails" type="{http://schema.ultraservice.neustar.com/v01/}AlertAllFailDetails"/>
 *         &lt;element name="poolStatus" type="{http://schema.ultraservice.neustar.com/v01/}AlertPoolStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ARAlertStateDetails", propOrder = {
    "oldRecordState",
    "newRecordState",
    "prioritizedRecords",
    "allFailDetails",
    "poolStatus"
})
public class ARAlertStateDetails {

    @XmlElement(required = true)
    protected RecordState oldRecordState;
    @XmlElement(required = true)
    protected RecordState newRecordState;
    @XmlElement(required = true)
    protected PrioritizedRecordsList prioritizedRecords;
    @XmlElement(required = true)
    protected AlertAllFailDetails allFailDetails;
    protected AlertPoolStatus poolStatus;

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
     * Gets the value of the prioritizedRecords property.
     * 
     * @return
     *     possible object is
     *     {@link PrioritizedRecordsList }
     *     
     */
    public PrioritizedRecordsList getPrioritizedRecords() {
        return prioritizedRecords;
    }

    /**
     * Sets the value of the prioritizedRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrioritizedRecordsList }
     *     
     */
    public void setPrioritizedRecords(PrioritizedRecordsList value) {
        this.prioritizedRecords = value;
    }

    /**
     * Gets the value of the allFailDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AlertAllFailDetails }
     *     
     */
    public AlertAllFailDetails getAllFailDetails() {
        return allFailDetails;
    }

    /**
     * Sets the value of the allFailDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlertAllFailDetails }
     *     
     */
    public void setAllFailDetails(AlertAllFailDetails value) {
        this.allFailDetails = value;
    }

    /**
     * Gets the value of the poolStatus property.
     * 
     * @return
     *     possible object is
     *     {@link AlertPoolStatus }
     *     
     */
    public AlertPoolStatus getPoolStatus() {
        return poolStatus;
    }

    /**
     * Sets the value of the poolStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlertPoolStatus }
     *     
     */
    public void setPoolStatus(AlertPoolStatus value) {
        this.poolStatus = value;
    }

}

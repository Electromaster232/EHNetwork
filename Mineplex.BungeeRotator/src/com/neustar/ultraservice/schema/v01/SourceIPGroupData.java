
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SourceIPGroupData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SourceIPGroupData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GroupData" type="{http://schema.ultraservice.neustar.com/v01/}GroupData"/>
 *         &lt;element name="SourceIPGroupDetails" type="{http://schema.ultraservice.neustar.com/v01/}SourceIPGroupDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourceIPGroupData", propOrder = {
    "groupData",
    "sourceIPGroupDetails"
})
public class SourceIPGroupData {

    @XmlElement(name = "GroupData", required = true)
    protected GroupData groupData;
    @XmlElement(name = "SourceIPGroupDetails", required = true, nillable = true)
    protected SourceIPGroupDetails sourceIPGroupDetails;

    /**
     * Gets the value of the groupData property.
     * 
     * @return
     *     possible object is
     *     {@link GroupData }
     *     
     */
    public GroupData getGroupData() {
        return groupData;
    }

    /**
     * Sets the value of the groupData property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupData }
     *     
     */
    public void setGroupData(GroupData value) {
        this.groupData = value;
    }

    /**
     * Gets the value of the sourceIPGroupDetails property.
     * 
     * @return
     *     possible object is
     *     {@link SourceIPGroupDetails }
     *     
     */
    public SourceIPGroupDetails getSourceIPGroupDetails() {
        return sourceIPGroupDetails;
    }

    /**
     * Sets the value of the sourceIPGroupDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceIPGroupDetails }
     *     
     */
    public void setSourceIPGroupDetails(SourceIPGroupDetails value) {
        this.sourceIPGroupDetails = value;
    }

}

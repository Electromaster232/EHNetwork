
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GroupData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GroupData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="groupingType" use="required" type="{http://schema.ultraservice.neustar.com/v01/}GroupingType" />
 *       &lt;attribute name="copyExistingGroupId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="newCopiedGroupName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="assignExistingGroupId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="assignGlobalGroupId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroupData")
public class GroupData {

    @XmlAttribute(name = "groupingType", required = true)
    protected GroupingType groupingType;
    @XmlAttribute(name = "copyExistingGroupId")
    protected String copyExistingGroupId;
    @XmlAttribute(name = "newCopiedGroupName")
    protected String newCopiedGroupName;
    @XmlAttribute(name = "assignExistingGroupId")
    protected String assignExistingGroupId;
    @XmlAttribute(name = "assignGlobalGroupId")
    protected String assignGlobalGroupId;

    /**
     * Gets the value of the groupingType property.
     * 
     * @return
     *     possible object is
     *     {@link GroupingType }
     *     
     */
    public GroupingType getGroupingType() {
        return groupingType;
    }

    /**
     * Sets the value of the groupingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupingType }
     *     
     */
    public void setGroupingType(GroupingType value) {
        this.groupingType = value;
    }

    /**
     * Gets the value of the copyExistingGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCopyExistingGroupId() {
        return copyExistingGroupId;
    }

    /**
     * Sets the value of the copyExistingGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCopyExistingGroupId(String value) {
        this.copyExistingGroupId = value;
    }

    /**
     * Gets the value of the newCopiedGroupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewCopiedGroupName() {
        return newCopiedGroupName;
    }

    /**
     * Sets the value of the newCopiedGroupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewCopiedGroupName(String value) {
        this.newCopiedGroupName = value;
    }

    /**
     * Gets the value of the assignExistingGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignExistingGroupId() {
        return assignExistingGroupId;
    }

    /**
     * Sets the value of the assignExistingGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignExistingGroupId(String value) {
        this.assignExistingGroupId = value;
    }

    /**
     * Gets the value of the assignGlobalGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignGlobalGroupId() {
        return assignGlobalGroupId;
    }

    /**
     * Sets the value of the assignGlobalGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignGlobalGroupId(String value) {
        this.assignGlobalGroupId = value;
    }

}

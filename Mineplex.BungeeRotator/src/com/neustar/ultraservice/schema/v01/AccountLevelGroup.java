
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountLevelGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountLevelGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="GroupId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="GroupName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="RecordsCount" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="GroupType" type="{http://schema.ultraservice.neustar.com/v01/}DirPoolType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountLevelGroup")
public class AccountLevelGroup {

    @XmlAttribute(name = "GroupId")
    protected String groupId;
    @XmlAttribute(name = "GroupName")
    protected String groupName;
    @XmlAttribute(name = "RecordsCount", required = true)
    protected int recordsCount;
    @XmlAttribute(name = "GroupType")
    protected DirPoolType groupType;

    /**
     * Gets the value of the groupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Sets the value of the groupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupId(String value) {
        this.groupId = value;
    }

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the recordsCount property.
     * 
     */
    public int getRecordsCount() {
        return recordsCount;
    }

    /**
     * Sets the value of the recordsCount property.
     * 
     */
    public void setRecordsCount(int value) {
        this.recordsCount = value;
    }

    /**
     * Gets the value of the groupType property.
     * 
     * @return
     *     possible object is
     *     {@link DirPoolType }
     *     
     */
    public DirPoolType getGroupType() {
        return groupType;
    }

    /**
     * Sets the value of the groupType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirPoolType }
     *     
     */
    public void setGroupType(DirPoolType value) {
        this.groupType = value;
    }

}

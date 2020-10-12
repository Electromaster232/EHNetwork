
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AcctToPoolGroupConversionDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AcctToPoolGroupConversionDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="accountLevelGroupId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="newGroupName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dirPoolRecordId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AcctToPoolGroupConversionDetails")
public class AcctToPoolGroupConversionDetails {

    @XmlAttribute(name = "accountLevelGroupId", required = true)
    protected String accountLevelGroupId;
    @XmlAttribute(name = "newGroupName", required = true)
    protected String newGroupName;
    @XmlAttribute(name = "dirPoolRecordId", required = true)
    protected String dirPoolRecordId;

    /**
     * Gets the value of the accountLevelGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountLevelGroupId() {
        return accountLevelGroupId;
    }

    /**
     * Sets the value of the accountLevelGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountLevelGroupId(String value) {
        this.accountLevelGroupId = value;
    }

    /**
     * Gets the value of the newGroupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewGroupName() {
        return newGroupName;
    }

    /**
     * Sets the value of the newGroupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewGroupName(String value) {
        this.newGroupName = value;
    }

    /**
     * Gets the value of the dirPoolRecordId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirPoolRecordId() {
        return dirPoolRecordId;
    }

    /**
     * Sets the value of the dirPoolRecordId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirPoolRecordId(String value) {
        this.dirPoolRecordId = value;
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountDetailsData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountDetailsData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="accountID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountHolderUserName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="primaryUserUserName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="numberOfUsers" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="numberOfUserGroups" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountHolderType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountDetailsData")
public class AccountDetailsData {

    @XmlAttribute(name = "accountID", required = true)
    protected String accountID;
    @XmlAttribute(name = "accountName", required = true)
    protected String accountName;
    @XmlAttribute(name = "accountHolderUserName", required = true)
    protected String accountHolderUserName;
    @XmlAttribute(name = "primaryUserUserName", required = true)
    protected String primaryUserUserName;
    @XmlAttribute(name = "numberOfUsers", required = true)
    protected String numberOfUsers;
    @XmlAttribute(name = "numberOfUserGroups", required = true)
    protected String numberOfUserGroups;
    @XmlAttribute(name = "accountHolderType", required = true)
    protected String accountHolderType;
    @XmlAttribute(name = "accountType", required = true)
    protected String accountType;

    /**
     * Gets the value of the accountID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountID() {
        return accountID;
    }

    /**
     * Sets the value of the accountID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountID(String value) {
        this.accountID = value;
    }

    /**
     * Gets the value of the accountName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Sets the value of the accountName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountName(String value) {
        this.accountName = value;
    }

    /**
     * Gets the value of the accountHolderUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountHolderUserName() {
        return accountHolderUserName;
    }

    /**
     * Sets the value of the accountHolderUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountHolderUserName(String value) {
        this.accountHolderUserName = value;
    }

    /**
     * Gets the value of the primaryUserUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryUserUserName() {
        return primaryUserUserName;
    }

    /**
     * Sets the value of the primaryUserUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryUserUserName(String value) {
        this.primaryUserUserName = value;
    }

    /**
     * Gets the value of the numberOfUsers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfUsers() {
        return numberOfUsers;
    }

    /**
     * Sets the value of the numberOfUsers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfUsers(String value) {
        this.numberOfUsers = value;
    }

    /**
     * Gets the value of the numberOfUserGroups property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfUserGroups() {
        return numberOfUserGroups;
    }

    /**
     * Sets the value of the numberOfUserGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfUserGroups(String value) {
        this.numberOfUserGroups = value;
    }

    /**
     * Gets the value of the accountHolderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountHolderType() {
        return accountHolderType;
    }

    /**
     * Sets the value of the accountHolderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountHolderType(String value) {
        this.accountHolderType = value;
    }

    /**
     * Gets the value of the accountType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the value of the accountType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountType(String value) {
        this.accountType = value;
    }

}

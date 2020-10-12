
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AllUsersDetailsData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AllUsersDetailsData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="firstName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountNamesList" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastLoggedInTime" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountsCount" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastLogged" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="lastLoggedCount" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AllUsersDetailsData")
public class AllUsersDetailsData {

    @XmlAttribute(name = "firstName", required = true)
    protected String firstName;
    @XmlAttribute(name = "lastName", required = true)
    protected String lastName;
    @XmlAttribute(name = "accountNamesList", required = true)
    protected String accountNamesList;
    @XmlAttribute(name = "lastLoggedInTime", required = true)
    protected String lastLoggedInTime;
    @XmlAttribute(name = "accountName", required = true)
    protected String accountName;
    @XmlAttribute(name = "accountsCount", required = true)
    protected String accountsCount;
    @XmlAttribute(name = "lastLogged", required = true)
    protected String lastLogged;
    @XmlAttribute(name = "lastLoggedCount", required = true)
    protected String lastLoggedCount;

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the accountNamesList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNamesList() {
        return accountNamesList;
    }

    /**
     * Sets the value of the accountNamesList property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNamesList(String value) {
        this.accountNamesList = value;
    }

    /**
     * Gets the value of the lastLoggedInTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastLoggedInTime() {
        return lastLoggedInTime;
    }

    /**
     * Sets the value of the lastLoggedInTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastLoggedInTime(String value) {
        this.lastLoggedInTime = value;
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
     * Gets the value of the accountsCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountsCount() {
        return accountsCount;
    }

    /**
     * Sets the value of the accountsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountsCount(String value) {
        this.accountsCount = value;
    }

    /**
     * Gets the value of the lastLogged property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastLogged() {
        return lastLogged;
    }

    /**
     * Sets the value of the lastLogged property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastLogged(String value) {
        this.lastLogged = value;
    }

    /**
     * Gets the value of the lastLoggedCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastLoggedCount() {
        return lastLoggedCount;
    }

    /**
     * Sets the value of the lastLoggedCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastLoggedCount(String value) {
        this.lastLoggedCount = value;
    }

}

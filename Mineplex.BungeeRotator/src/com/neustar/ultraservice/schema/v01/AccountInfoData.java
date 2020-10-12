
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountInfoData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountInfoData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="accountID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountStatus" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="totalUsers" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="created" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="orgAccountHolderUserName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="orgPrimaryUserUserName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="indAccountHolderAndPrimaryUserName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountHolderAddress" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountHolderAddress2" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountHolderCity" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountHolderState" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountHolderZip" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountHolderCountry" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="orgPrimaryUserAddress" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="orgPrimaryUserAddress2" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="orgPrimaryUserCity" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="orgPrimaryUserState" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="orgPrimaryUserZip" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="orgPrimaryUserCountry" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountInfoData")
public class AccountInfoData {

    @XmlAttribute(name = "accountID", required = true)
    protected String accountID;
    @XmlAttribute(name = "accountName", required = true)
    protected String accountName;
    @XmlAttribute(name = "accountStatus", required = true)
    protected String accountStatus;
    @XmlAttribute(name = "totalUsers", required = true)
    protected String totalUsers;
    @XmlAttribute(name = "created", required = true)
    protected String created;
    @XmlAttribute(name = "orgAccountHolderUserName", required = true)
    protected String orgAccountHolderUserName;
    @XmlAttribute(name = "orgPrimaryUserUserName", required = true)
    protected String orgPrimaryUserUserName;
    @XmlAttribute(name = "accountType", required = true)
    protected String accountType;
    @XmlAttribute(name = "indAccountHolderAndPrimaryUserName", required = true)
    protected String indAccountHolderAndPrimaryUserName;
    @XmlAttribute(name = "accountHolderAddress", required = true)
    protected String accountHolderAddress;
    @XmlAttribute(name = "accountHolderAddress2", required = true)
    protected String accountHolderAddress2;
    @XmlAttribute(name = "accountHolderCity", required = true)
    protected String accountHolderCity;
    @XmlAttribute(name = "accountHolderState", required = true)
    protected String accountHolderState;
    @XmlAttribute(name = "accountHolderZip", required = true)
    protected String accountHolderZip;
    @XmlAttribute(name = "accountHolderCountry", required = true)
    protected String accountHolderCountry;
    @XmlAttribute(name = "orgPrimaryUserAddress", required = true)
    protected String orgPrimaryUserAddress;
    @XmlAttribute(name = "orgPrimaryUserAddress2", required = true)
    protected String orgPrimaryUserAddress2;
    @XmlAttribute(name = "orgPrimaryUserCity", required = true)
    protected String orgPrimaryUserCity;
    @XmlAttribute(name = "orgPrimaryUserState", required = true)
    protected String orgPrimaryUserState;
    @XmlAttribute(name = "orgPrimaryUserZip", required = true)
    protected String orgPrimaryUserZip;
    @XmlAttribute(name = "orgPrimaryUserCountry", required = true)
    protected String orgPrimaryUserCountry;

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
     * Gets the value of the accountStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountStatus() {
        return accountStatus;
    }

    /**
     * Sets the value of the accountStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountStatus(String value) {
        this.accountStatus = value;
    }

    /**
     * Gets the value of the totalUsers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalUsers() {
        return totalUsers;
    }

    /**
     * Sets the value of the totalUsers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalUsers(String value) {
        this.totalUsers = value;
    }

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreated(String value) {
        this.created = value;
    }

    /**
     * Gets the value of the orgAccountHolderUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgAccountHolderUserName() {
        return orgAccountHolderUserName;
    }

    /**
     * Sets the value of the orgAccountHolderUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgAccountHolderUserName(String value) {
        this.orgAccountHolderUserName = value;
    }

    /**
     * Gets the value of the orgPrimaryUserUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgPrimaryUserUserName() {
        return orgPrimaryUserUserName;
    }

    /**
     * Sets the value of the orgPrimaryUserUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgPrimaryUserUserName(String value) {
        this.orgPrimaryUserUserName = value;
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

    /**
     * Gets the value of the indAccountHolderAndPrimaryUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndAccountHolderAndPrimaryUserName() {
        return indAccountHolderAndPrimaryUserName;
    }

    /**
     * Sets the value of the indAccountHolderAndPrimaryUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndAccountHolderAndPrimaryUserName(String value) {
        this.indAccountHolderAndPrimaryUserName = value;
    }

    /**
     * Gets the value of the accountHolderAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountHolderAddress() {
        return accountHolderAddress;
    }

    /**
     * Sets the value of the accountHolderAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountHolderAddress(String value) {
        this.accountHolderAddress = value;
    }

    /**
     * Gets the value of the accountHolderAddress2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountHolderAddress2() {
        return accountHolderAddress2;
    }

    /**
     * Sets the value of the accountHolderAddress2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountHolderAddress2(String value) {
        this.accountHolderAddress2 = value;
    }

    /**
     * Gets the value of the accountHolderCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountHolderCity() {
        return accountHolderCity;
    }

    /**
     * Sets the value of the accountHolderCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountHolderCity(String value) {
        this.accountHolderCity = value;
    }

    /**
     * Gets the value of the accountHolderState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountHolderState() {
        return accountHolderState;
    }

    /**
     * Sets the value of the accountHolderState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountHolderState(String value) {
        this.accountHolderState = value;
    }

    /**
     * Gets the value of the accountHolderZip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountHolderZip() {
        return accountHolderZip;
    }

    /**
     * Sets the value of the accountHolderZip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountHolderZip(String value) {
        this.accountHolderZip = value;
    }

    /**
     * Gets the value of the accountHolderCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountHolderCountry() {
        return accountHolderCountry;
    }

    /**
     * Sets the value of the accountHolderCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountHolderCountry(String value) {
        this.accountHolderCountry = value;
    }

    /**
     * Gets the value of the orgPrimaryUserAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgPrimaryUserAddress() {
        return orgPrimaryUserAddress;
    }

    /**
     * Sets the value of the orgPrimaryUserAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgPrimaryUserAddress(String value) {
        this.orgPrimaryUserAddress = value;
    }

    /**
     * Gets the value of the orgPrimaryUserAddress2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgPrimaryUserAddress2() {
        return orgPrimaryUserAddress2;
    }

    /**
     * Sets the value of the orgPrimaryUserAddress2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgPrimaryUserAddress2(String value) {
        this.orgPrimaryUserAddress2 = value;
    }

    /**
     * Gets the value of the orgPrimaryUserCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgPrimaryUserCity() {
        return orgPrimaryUserCity;
    }

    /**
     * Sets the value of the orgPrimaryUserCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgPrimaryUserCity(String value) {
        this.orgPrimaryUserCity = value;
    }

    /**
     * Gets the value of the orgPrimaryUserState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgPrimaryUserState() {
        return orgPrimaryUserState;
    }

    /**
     * Sets the value of the orgPrimaryUserState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgPrimaryUserState(String value) {
        this.orgPrimaryUserState = value;
    }

    /**
     * Gets the value of the orgPrimaryUserZip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgPrimaryUserZip() {
        return orgPrimaryUserZip;
    }

    /**
     * Sets the value of the orgPrimaryUserZip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgPrimaryUserZip(String value) {
        this.orgPrimaryUserZip = value;
    }

    /**
     * Gets the value of the orgPrimaryUserCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgPrimaryUserCountry() {
        return orgPrimaryUserCountry;
    }

    /**
     * Sets the value of the orgPrimaryUserCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgPrimaryUserCountry(String value) {
        this.orgPrimaryUserCountry = value;
    }

}

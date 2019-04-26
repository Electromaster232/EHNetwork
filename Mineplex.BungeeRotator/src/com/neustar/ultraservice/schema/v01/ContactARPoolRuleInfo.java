
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContactARPoolRuleInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContactARPoolRuleInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arPoolRuleKey" type="{http://schema.ultraservice.neustar.com/v01/}ARPoolRuleKey"/>
 *         &lt;element name="addressBookEntryKeys" type="{http://schema.ultraservice.neustar.com/v01/}AddressBookEntryKeys"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactARPoolRuleInfo", propOrder = {
    "arPoolRuleKey",
    "addressBookEntryKeys"
})
public class ContactARPoolRuleInfo {

    @XmlElement(required = true)
    protected ARPoolRuleKey arPoolRuleKey;
    @XmlElement(required = true)
    protected AddressBookEntryKeys addressBookEntryKeys;

    /**
     * Gets the value of the arPoolRuleKey property.
     * 
     * @return
     *     possible object is
     *     {@link ARPoolRuleKey }
     *     
     */
    public ARPoolRuleKey getArPoolRuleKey() {
        return arPoolRuleKey;
    }

    /**
     * Sets the value of the arPoolRuleKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link ARPoolRuleKey }
     *     
     */
    public void setArPoolRuleKey(ARPoolRuleKey value) {
        this.arPoolRuleKey = value;
    }

    /**
     * Gets the value of the addressBookEntryKeys property.
     * 
     * @return
     *     possible object is
     *     {@link AddressBookEntryKeys }
     *     
     */
    public AddressBookEntryKeys getAddressBookEntryKeys() {
        return addressBookEntryKeys;
    }

    /**
     * Sets the value of the addressBookEntryKeys property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressBookEntryKeys }
     *     
     */
    public void setAddressBookEntryKeys(AddressBookEntryKeys value) {
        this.addressBookEntryKeys = value;
    }

}

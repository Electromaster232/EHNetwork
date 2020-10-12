
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddressBookEntryListKey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressBookEntryListKey">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="entryListParams" type="{http://schema.ultraservice.neustar.com/v01/}EntryListParams" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressBookEntryListKey", propOrder = {
    "accountName",
    "entryListParams"
})
public class AddressBookEntryListKey {

    @XmlElement(required = true)
    protected String accountName;
    protected EntryListParams entryListParams;

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
     * Gets the value of the entryListParams property.
     * 
     * @return
     *     possible object is
     *     {@link EntryListParams }
     *     
     */
    public EntryListParams getEntryListParams() {
        return entryListParams;
    }

    /**
     * Sets the value of the entryListParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntryListParams }
     *     
     */
    public void setEntryListParams(EntryListParams value) {
        this.entryListParams = value;
    }

}

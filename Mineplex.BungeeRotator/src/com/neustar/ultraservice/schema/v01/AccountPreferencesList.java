
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountPreferencesList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountPreferencesList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccountPreferenceDetail" type="{http://schema.ultraservice.neustar.com/v01/}AccountPreferenceDetail" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountPreferencesList", propOrder = {
    "accountPreferenceDetail"
})
public class AccountPreferencesList {

    @XmlElement(name = "AccountPreferenceDetail", required = true)
    protected List<AccountPreferenceDetail> accountPreferenceDetail;

    /**
     * Gets the value of the accountPreferenceDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the accountPreferenceDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccountPreferenceDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccountPreferenceDetail }
     * 
     * 
     */
    public List<AccountPreferenceDetail> getAccountPreferenceDetail() {
        if (accountPreferenceDetail == null) {
            accountPreferenceDetail = new ArrayList<AccountPreferenceDetail>();
        }
        return this.accountPreferenceDetail;
    }

}

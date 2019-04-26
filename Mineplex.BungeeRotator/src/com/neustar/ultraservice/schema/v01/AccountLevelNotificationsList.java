
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountLevelNotificationsList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountLevelNotificationsList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccountLevelNotificationsData" type="{http://schema.ultraservice.neustar.com/v01/}AccountLevelNotificationsData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountLevelNotificationsList", propOrder = {
    "accountLevelNotificationsData"
})
public class AccountLevelNotificationsList {

    @XmlElement(name = "AccountLevelNotificationsData", required = true)
    protected List<AccountLevelNotificationsData> accountLevelNotificationsData;

    /**
     * Gets the value of the accountLevelNotificationsData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the accountLevelNotificationsData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccountLevelNotificationsData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccountLevelNotificationsData }
     * 
     * 
     */
    public List<AccountLevelNotificationsData> getAccountLevelNotificationsData() {
        if (accountLevelNotificationsData == null) {
            accountLevelNotificationsData = new ArrayList<AccountLevelNotificationsData>();
        }
        return this.accountLevelNotificationsData;
    }

}

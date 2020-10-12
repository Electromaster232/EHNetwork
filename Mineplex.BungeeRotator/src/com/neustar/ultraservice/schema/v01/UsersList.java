
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UsersList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UsersList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserDetailsData" type="{http://schema.ultraservice.neustar.com/v01/}UserDetailsData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UsersList", propOrder = {
    "userDetailsData"
})
public class UsersList {

    @XmlElement(name = "UserDetailsData", required = true)
    protected List<UserDetailsData> userDetailsData;

    /**
     * Gets the value of the userDetailsData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userDetailsData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserDetailsData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserDetailsData }
     * 
     * 
     */
    public List<UserDetailsData> getUserDetailsData() {
        if (userDetailsData == null) {
            userDetailsData = new ArrayList<UserDetailsData>();
        }
        return this.userDetailsData;
    }

}

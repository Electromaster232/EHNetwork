
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserContactInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserContactInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserContactInfoValues" type="{http://schema.ultraservice.neustar.com/v01/}UserContactInfoValues"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserContactInfo", propOrder = {
    "userContactInfoValues"
})
public class UserContactInfo {

    @XmlElement(name = "UserContactInfoValues", required = true)
    protected UserContactInfoValues userContactInfoValues;

    /**
     * Gets the value of the userContactInfoValues property.
     * 
     * @return
     *     possible object is
     *     {@link UserContactInfoValues }
     *     
     */
    public UserContactInfoValues getUserContactInfoValues() {
        return userContactInfoValues;
    }

    /**
     * Sets the value of the userContactInfoValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserContactInfoValues }
     *     
     */
    public void setUserContactInfoValues(UserContactInfoValues value) {
        this.userContactInfoValues = value;
    }

}

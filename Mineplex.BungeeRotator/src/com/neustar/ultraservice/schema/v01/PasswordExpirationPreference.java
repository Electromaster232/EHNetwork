
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PasswordExpirationPreference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PasswordExpirationPreference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="PasswordExpiration" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PasswordExpirationPreference")
public class PasswordExpirationPreference {

    @XmlAttribute(name = "PasswordExpiration")
    protected String passwordExpiration;

    /**
     * Gets the value of the passwordExpiration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordExpiration() {
        return passwordExpiration;
    }

    /**
     * Sets the value of the passwordExpiration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordExpiration(String value) {
        this.passwordExpiration = value;
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecurityPreferences complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecurityPreferences">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SecurityQuestions" type="{http://schema.ultraservice.neustar.com/v01/}SecurityQuestions"/>
 *         &lt;element name="InactiveTimeOutPreference" type="{http://schema.ultraservice.neustar.com/v01/}InactiveTimeOutPreference"/>
 *         &lt;element name="PasswordExpirationPreference" type="{http://schema.ultraservice.neustar.com/v01/}PasswordExpirationPreference"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecurityPreferences", propOrder = {
    "securityQuestions",
    "inactiveTimeOutPreference",
    "passwordExpirationPreference"
})
public class SecurityPreferences {

    @XmlElement(name = "SecurityQuestions", required = true)
    protected SecurityQuestions securityQuestions;
    @XmlElement(name = "InactiveTimeOutPreference", required = true)
    protected InactiveTimeOutPreference inactiveTimeOutPreference;
    @XmlElement(name = "PasswordExpirationPreference", required = true)
    protected PasswordExpirationPreference passwordExpirationPreference;

    /**
     * Gets the value of the securityQuestions property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityQuestions }
     *     
     */
    public SecurityQuestions getSecurityQuestions() {
        return securityQuestions;
    }

    /**
     * Sets the value of the securityQuestions property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityQuestions }
     *     
     */
    public void setSecurityQuestions(SecurityQuestions value) {
        this.securityQuestions = value;
    }

    /**
     * Gets the value of the inactiveTimeOutPreference property.
     * 
     * @return
     *     possible object is
     *     {@link InactiveTimeOutPreference }
     *     
     */
    public InactiveTimeOutPreference getInactiveTimeOutPreference() {
        return inactiveTimeOutPreference;
    }

    /**
     * Sets the value of the inactiveTimeOutPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link InactiveTimeOutPreference }
     *     
     */
    public void setInactiveTimeOutPreference(InactiveTimeOutPreference value) {
        this.inactiveTimeOutPreference = value;
    }

    /**
     * Gets the value of the passwordExpirationPreference property.
     * 
     * @return
     *     possible object is
     *     {@link PasswordExpirationPreference }
     *     
     */
    public PasswordExpirationPreference getPasswordExpirationPreference() {
        return passwordExpirationPreference;
    }

    /**
     * Sets the value of the passwordExpirationPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link PasswordExpirationPreference }
     *     
     */
    public void setPasswordExpirationPreference(PasswordExpirationPreference value) {
        this.passwordExpirationPreference = value;
    }

}

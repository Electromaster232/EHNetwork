
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecurityQuestions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecurityQuestions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="SecurityQuestion1" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SecretAnswer1" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SecurityQuestion2" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SecretAnswer2" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SecurityQuestion3" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SecretAnswer3" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecurityQuestions")
public class SecurityQuestions {

    @XmlAttribute(name = "SecurityQuestion1")
    protected String securityQuestion1;
    @XmlAttribute(name = "SecretAnswer1")
    protected String secretAnswer1;
    @XmlAttribute(name = "SecurityQuestion2")
    protected String securityQuestion2;
    @XmlAttribute(name = "SecretAnswer2")
    protected String secretAnswer2;
    @XmlAttribute(name = "SecurityQuestion3")
    protected String securityQuestion3;
    @XmlAttribute(name = "SecretAnswer3")
    protected String secretAnswer3;

    /**
     * Gets the value of the securityQuestion1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityQuestion1() {
        return securityQuestion1;
    }

    /**
     * Sets the value of the securityQuestion1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityQuestion1(String value) {
        this.securityQuestion1 = value;
    }

    /**
     * Gets the value of the secretAnswer1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretAnswer1() {
        return secretAnswer1;
    }

    /**
     * Sets the value of the secretAnswer1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretAnswer1(String value) {
        this.secretAnswer1 = value;
    }

    /**
     * Gets the value of the securityQuestion2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityQuestion2() {
        return securityQuestion2;
    }

    /**
     * Sets the value of the securityQuestion2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityQuestion2(String value) {
        this.securityQuestion2 = value;
    }

    /**
     * Gets the value of the secretAnswer2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretAnswer2() {
        return secretAnswer2;
    }

    /**
     * Sets the value of the secretAnswer2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretAnswer2(String value) {
        this.secretAnswer2 = value;
    }

    /**
     * Gets the value of the securityQuestion3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityQuestion3() {
        return securityQuestion3;
    }

    /**
     * Sets the value of the securityQuestion3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityQuestion3(String value) {
        this.securityQuestion3 = value;
    }

    /**
     * Gets the value of the secretAnswer3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretAnswer3() {
        return secretAnswer3;
    }

    /**
     * Sets the value of the secretAnswer3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretAnswer3(String value) {
        this.secretAnswer3 = value;
    }

}

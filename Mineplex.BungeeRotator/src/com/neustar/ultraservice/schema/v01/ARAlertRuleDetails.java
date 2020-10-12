
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARAlertRuleDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ARAlertRuleDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="oldRule" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="newRule" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ARAlertRuleDetails", propOrder = {
    "oldRule",
    "newRule"
})
public class ARAlertRuleDetails {

    @XmlElement(required = true)
    protected String oldRule;
    @XmlElement(required = true)
    protected String newRule;

    /**
     * Gets the value of the oldRule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldRule() {
        return oldRule;
    }

    /**
     * Sets the value of the oldRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldRule(String value) {
        this.oldRule = value;
    }

    /**
     * Gets the value of the newRule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewRule() {
        return newRule;
    }

    /**
     * Sets the value of the newRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewRule(String value) {
        this.newRule = value;
    }

}

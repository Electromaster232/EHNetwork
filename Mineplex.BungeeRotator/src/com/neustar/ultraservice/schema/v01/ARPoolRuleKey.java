
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARPoolRuleKey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ARPoolRuleKey">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="poolKey" type="{http://schema.ultraservice.neustar.com/v01/}PoolKey"/>
 *         &lt;element name="ruleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ARPoolRuleKey", propOrder = {
    "poolKey",
    "ruleName"
})
public class ARPoolRuleKey {

    @XmlElement(required = true)
    protected PoolKey poolKey;
    @XmlElement(required = true)
    protected String ruleName;

    /**
     * Gets the value of the poolKey property.
     * 
     * @return
     *     possible object is
     *     {@link PoolKey }
     *     
     */
    public PoolKey getPoolKey() {
        return poolKey;
    }

    /**
     * Sets the value of the poolKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link PoolKey }
     *     
     */
    public void setPoolKey(PoolKey value) {
        this.poolKey = value;
    }

    /**
     * Gets the value of the ruleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Sets the value of the ruleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleName(String value) {
        this.ruleName = value;
    }

}

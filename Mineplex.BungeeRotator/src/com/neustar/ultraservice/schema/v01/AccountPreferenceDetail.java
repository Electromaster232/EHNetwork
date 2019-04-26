
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountPreferenceDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountPreferenceDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExternalValues" type="{http://schema.ultraservice.neustar.com/v01/}ExternalValues"/>
 *       &lt;/sequence>
 *       &lt;attribute name="RecordTypeTTL" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountPreferenceDetail", propOrder = {
    "externalValues"
})
public class AccountPreferenceDetail {

    @XmlElement(name = "ExternalValues", required = true)
    protected ExternalValues externalValues;
    @XmlAttribute(name = "RecordTypeTTL", required = true)
    protected String recordTypeTTL;

    /**
     * Gets the value of the externalValues property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalValues }
     *     
     */
    public ExternalValues getExternalValues() {
        return externalValues;
    }

    /**
     * Sets the value of the externalValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalValues }
     *     
     */
    public void setExternalValues(ExternalValues value) {
        this.externalValues = value;
    }

    /**
     * Gets the value of the recordTypeTTL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordTypeTTL() {
        return recordTypeTTL;
    }

    /**
     * Sets the value of the recordTypeTTL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordTypeTTL(String value) {
        this.recordTypeTTL = value;
    }

}

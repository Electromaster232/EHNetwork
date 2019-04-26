
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SimpleFailoverConversionInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SimpleFailoverConversionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="simpleFailoverPoolKey" type="{http://schema.ultraservice.neustar.com/v01/}SimpleFailoverPoolKey"/>
 *         &lt;element name="conversionType" type="{http://schema.ultraservice.neustar.com/v01/}simpleFailoverConversion"/>
 *         &lt;element name="keptRecord" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleFailoverConversionInfo", propOrder = {
    "simpleFailoverPoolKey",
    "conversionType",
    "keptRecord"
})
public class SimpleFailoverConversionInfo {

    @XmlElement(required = true)
    protected SimpleFailoverPoolKey simpleFailoverPoolKey;
    @XmlElement(required = true)
    protected SimpleFailoverConversion conversionType;
    protected String keptRecord;

    /**
     * Gets the value of the simpleFailoverPoolKey property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleFailoverPoolKey }
     *     
     */
    public SimpleFailoverPoolKey getSimpleFailoverPoolKey() {
        return simpleFailoverPoolKey;
    }

    /**
     * Sets the value of the simpleFailoverPoolKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleFailoverPoolKey }
     *     
     */
    public void setSimpleFailoverPoolKey(SimpleFailoverPoolKey value) {
        this.simpleFailoverPoolKey = value;
    }

    /**
     * Gets the value of the conversionType property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleFailoverConversion }
     *     
     */
    public SimpleFailoverConversion getConversionType() {
        return conversionType;
    }

    /**
     * Sets the value of the conversionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleFailoverConversion }
     *     
     */
    public void setConversionType(SimpleFailoverConversion value) {
        this.conversionType = value;
    }

    /**
     * Gets the value of the keptRecord property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeptRecord() {
        return keptRecord;
    }

    /**
     * Sets the value of the keptRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeptRecord(String value) {
        this.keptRecord = value;
    }

}

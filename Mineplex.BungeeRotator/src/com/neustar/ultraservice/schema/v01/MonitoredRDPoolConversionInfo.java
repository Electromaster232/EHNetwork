
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.neustar.ultra.api.webservice.v01.ConversionTypeEnum;


/**
 * <p>Java class for MonitoredRDPoolConversionInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MonitoredRDPoolConversionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="monitoredRDPoolKey" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolKey"/>
 *         &lt;element name="conversionType" type="{http://webservice.api.ultra.neustar.com/v01/}conversionTypeEnum"/>
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
@XmlType(name = "MonitoredRDPoolConversionInfo", propOrder = {
    "monitoredRDPoolKey",
    "conversionType",
    "keptRecord"
})
public class MonitoredRDPoolConversionInfo {

    @XmlElement(required = true)
    protected MonitoredRDPoolKey monitoredRDPoolKey;
    @XmlElement(required = true)
    protected ConversionTypeEnum conversionType;
    protected String keptRecord;

    /**
     * Gets the value of the monitoredRDPoolKey property.
     * 
     * @return
     *     possible object is
     *     {@link MonitoredRDPoolKey }
     *     
     */
    public MonitoredRDPoolKey getMonitoredRDPoolKey() {
        return monitoredRDPoolKey;
    }

    /**
     * Sets the value of the monitoredRDPoolKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitoredRDPoolKey }
     *     
     */
    public void setMonitoredRDPoolKey(MonitoredRDPoolKey value) {
        this.monitoredRDPoolKey = value;
    }

    /**
     * Gets the value of the conversionType property.
     * 
     * @return
     *     possible object is
     *     {@link ConversionTypeEnum }
     *     
     */
    public ConversionTypeEnum getConversionType() {
        return conversionType;
    }

    /**
     * Sets the value of the conversionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConversionTypeEnum }
     *     
     */
    public void setConversionType(ConversionTypeEnum value) {
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

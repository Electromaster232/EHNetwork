
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.neustar.ultra.api.webservice.v01.ConvertTypeEnum;


/**
 * <p>Java class for rDPoolConversionInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rDPoolConversionInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="zoneName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hostName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="conversionType" type="{http://webservice.api.ultra.neustar.com/v01/}convertTypeEnum"/>
 *         &lt;element name="recordToKeep" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="probeInfo" type="{http://schema.ultraservice.neustar.com/v01/}probeInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rDPoolConversionInfo", propOrder = {
    "zoneName",
    "hostName",
    "conversionType",
    "recordToKeep",
    "probeInfo"
})
public class RDPoolConversionInfo {

    @XmlElement(required = true)
    protected String zoneName;
    @XmlElement(required = true)
    protected String hostName;
    @XmlElement(required = true)
    protected ConvertTypeEnum conversionType;
    protected String recordToKeep;
    protected ProbeInfo2 probeInfo;

    /**
     * Gets the value of the zoneName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * Sets the value of the zoneName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneName(String value) {
        this.zoneName = value;
    }

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the conversionType property.
     * 
     * @return
     *     possible object is
     *     {@link ConvertTypeEnum }
     *     
     */
    public ConvertTypeEnum getConversionType() {
        return conversionType;
    }

    /**
     * Sets the value of the conversionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConvertTypeEnum }
     *     
     */
    public void setConversionType(ConvertTypeEnum value) {
        this.conversionType = value;
    }

    /**
     * Gets the value of the recordToKeep property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordToKeep() {
        return recordToKeep;
    }

    /**
     * Sets the value of the recordToKeep property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordToKeep(String value) {
        this.recordToKeep = value;
    }

    /**
     * Gets the value of the probeInfo property.
     * 
     * @return
     *     possible object is
     *     {@link ProbeInfo2 }
     *     
     */
    public ProbeInfo2 getProbeInfo() {
        return probeInfo;
    }

    /**
     * Sets the value of the probeInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProbeInfo2 }
     *     
     */
    public void setProbeInfo(ProbeInfo2 value) {
        this.probeInfo = value;
    }

}

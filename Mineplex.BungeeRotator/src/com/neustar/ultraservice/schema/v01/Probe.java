
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Probe complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Probe">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="probeDefinitionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="regionThreshold" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="regions" type="{http://schema.ultraservice.neustar.com/v01/}ProbeRegions"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Probe", propOrder = {
    "probeDefinitionName",
    "regionThreshold",
    "regions"
})
public class Probe {

    @XmlElement(required = true)
    protected String probeDefinitionName;
    protected int regionThreshold;
    @XmlElement(required = true)
    protected ProbeRegions regions;

    /**
     * Gets the value of the probeDefinitionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeDefinitionName() {
        return probeDefinitionName;
    }

    /**
     * Sets the value of the probeDefinitionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeDefinitionName(String value) {
        this.probeDefinitionName = value;
    }

    /**
     * Gets the value of the regionThreshold property.
     * 
     */
    public int getRegionThreshold() {
        return regionThreshold;
    }

    /**
     * Sets the value of the regionThreshold property.
     * 
     */
    public void setRegionThreshold(int value) {
        this.regionThreshold = value;
    }

    /**
     * Gets the value of the regions property.
     * 
     * @return
     *     possible object is
     *     {@link ProbeRegions }
     *     
     */
    public ProbeRegions getRegions() {
        return regions;
    }

    /**
     * Sets the value of the regions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProbeRegions }
     *     
     */
    public void setRegions(ProbeRegions value) {
        this.regions = value;
    }

}

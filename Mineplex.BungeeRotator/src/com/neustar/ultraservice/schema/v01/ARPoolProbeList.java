
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARPoolProbeList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ARPoolProbeList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arPoolProbes" type="{http://schema.ultraservice.neustar.com/v01/}ARPoolProbes" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ARPoolProbeList", propOrder = {
    "arPoolProbes"
})
public class ARPoolProbeList {

    protected ARPoolProbes arPoolProbes;

    /**
     * Gets the value of the arPoolProbes property.
     * 
     * @return
     *     possible object is
     *     {@link ARPoolProbes }
     *     
     */
    public ARPoolProbes getArPoolProbes() {
        return arPoolProbes;
    }

    /**
     * Sets the value of the arPoolProbes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ARPoolProbes }
     *     
     */
    public void setArPoolProbes(ARPoolProbes value) {
        this.arPoolProbes = value;
    }

}

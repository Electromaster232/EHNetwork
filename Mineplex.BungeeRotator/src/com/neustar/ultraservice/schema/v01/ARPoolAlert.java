
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARPoolAlert complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ARPoolAlert">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="poolAlertDetails" type="{http://schema.ultraservice.neustar.com/v01/}PoolAlertDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ARPoolAlert", propOrder = {
    "poolAlertDetails"
})
public class ARPoolAlert {

    @XmlElement(required = true)
    protected PoolAlertDetails poolAlertDetails;

    /**
     * Gets the value of the poolAlertDetails property.
     * 
     * @return
     *     possible object is
     *     {@link PoolAlertDetails }
     *     
     */
    public PoolAlertDetails getPoolAlertDetails() {
        return poolAlertDetails;
    }

    /**
     * Sets the value of the poolAlertDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link PoolAlertDetails }
     *     
     */
    public void setPoolAlertDetails(PoolAlertDetails value) {
        this.poolAlertDetails = value;
    }

}

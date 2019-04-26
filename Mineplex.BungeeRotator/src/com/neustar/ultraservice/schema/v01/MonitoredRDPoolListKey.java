
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MonitoredRDPoolListKey complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MonitoredRDPoolListKey">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="zoneName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="poolListParams" type="{http://schema.ultraservice.neustar.com/v01/}PoolListParams"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonitoredRDPoolListKey", propOrder = {
    "zoneName",
    "poolListParams"
})
public class MonitoredRDPoolListKey {

    @XmlElement(required = true)
    protected String zoneName;
    @XmlElement(required = true)
    protected PoolListParams poolListParams;

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
     * Gets the value of the poolListParams property.
     * 
     * @return
     *     possible object is
     *     {@link PoolListParams }
     *     
     */
    public PoolListParams getPoolListParams() {
        return poolListParams;
    }

    /**
     * Sets the value of the poolListParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link PoolListParams }
     *     
     */
    public void setPoolListParams(PoolListParams value) {
        this.poolListParams = value;
    }

}

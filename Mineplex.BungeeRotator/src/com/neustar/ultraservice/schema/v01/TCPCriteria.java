
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TCPCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TCPCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="connectTime" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="connectTimeAverage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TCPCriteria", propOrder = {
    "connectTime",
    "connectTimeAverage"
})
public class TCPCriteria {

    protected int connectTime;
    protected Integer connectTimeAverage;

    /**
     * Gets the value of the connectTime property.
     * 
     */
    public int getConnectTime() {
        return connectTime;
    }

    /**
     * Sets the value of the connectTime property.
     * 
     */
    public void setConnectTime(int value) {
        this.connectTime = value;
    }

    /**
     * Gets the value of the connectTimeAverage property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getConnectTimeAverage() {
        return connectTimeAverage;
    }

    /**
     * Sets the value of the connectTimeAverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setConnectTimeAverage(Integer value) {
        this.connectTimeAverage = value;
    }

}

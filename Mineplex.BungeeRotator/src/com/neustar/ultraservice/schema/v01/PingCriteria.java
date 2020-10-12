
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PingCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PingCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="runTimeAverage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="connectTime" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="connectTimeAverage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="percentLost" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PingCriteria", propOrder = {
    "runTime",
    "runTimeAverage",
    "connectTime",
    "connectTimeAverage",
    "percentLost"
})
public class PingCriteria {

    protected Integer runTime;
    protected Integer runTimeAverage;
    protected Integer connectTime;
    protected Integer connectTimeAverage;
    protected Integer percentLost;

    /**
     * Gets the value of the runTime property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRunTime() {
        return runTime;
    }

    /**
     * Sets the value of the runTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRunTime(Integer value) {
        this.runTime = value;
    }

    /**
     * Gets the value of the runTimeAverage property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRunTimeAverage() {
        return runTimeAverage;
    }

    /**
     * Sets the value of the runTimeAverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRunTimeAverage(Integer value) {
        this.runTimeAverage = value;
    }

    /**
     * Gets the value of the connectTime property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getConnectTime() {
        return connectTime;
    }

    /**
     * Sets the value of the connectTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setConnectTime(Integer value) {
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

    /**
     * Gets the value of the percentLost property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPercentLost() {
        return percentLost;
    }

    /**
     * Sets the value of the percentLost property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPercentLost(Integer value) {
        this.percentLost = value;
    }

}

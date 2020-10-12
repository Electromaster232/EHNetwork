
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SMTPCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SMTPCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="runTimeAverage" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "SMTPCriteria", propOrder = {
    "runTime",
    "runTimeAverage",
    "connectTime",
    "connectTimeAverage"
})
public class SMTPCriteria {

    protected int runTime;
    protected Integer runTimeAverage;
    protected int connectTime;
    protected Integer connectTimeAverage;

    /**
     * Gets the value of the runTime property.
     * 
     */
    public int getRunTime() {
        return runTime;
    }

    /**
     * Sets the value of the runTime property.
     * 
     */
    public void setRunTime(int value) {
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

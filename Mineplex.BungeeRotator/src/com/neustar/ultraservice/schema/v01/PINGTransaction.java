
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PINGTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PINGTransaction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="warningCriteria" type="{http://schema.ultraservice.neustar.com/v01/}PingCriteria" minOccurs="0"/>
 *         &lt;element name="criticalCriteria" type="{http://schema.ultraservice.neustar.com/v01/}PingCriteria" minOccurs="0"/>
 *         &lt;element name="failCriteria" type="{http://schema.ultraservice.neustar.com/v01/}PingCriteria" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="packets" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PINGTransaction", propOrder = {
    "warningCriteria",
    "criticalCriteria",
    "failCriteria"
})
public class PINGTransaction {

    protected PingCriteria warningCriteria;
    protected PingCriteria criticalCriteria;
    protected PingCriteria failCriteria;
    @XmlAttribute(name = "packets")
    protected Integer packets;
    @XmlAttribute(name = "size")
    protected Integer size;

    /**
     * Gets the value of the warningCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link PingCriteria }
     *     
     */
    public PingCriteria getWarningCriteria() {
        return warningCriteria;
    }

    /**
     * Sets the value of the warningCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link PingCriteria }
     *     
     */
    public void setWarningCriteria(PingCriteria value) {
        this.warningCriteria = value;
    }

    /**
     * Gets the value of the criticalCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link PingCriteria }
     *     
     */
    public PingCriteria getCriticalCriteria() {
        return criticalCriteria;
    }

    /**
     * Sets the value of the criticalCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link PingCriteria }
     *     
     */
    public void setCriticalCriteria(PingCriteria value) {
        this.criticalCriteria = value;
    }

    /**
     * Gets the value of the failCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link PingCriteria }
     *     
     */
    public PingCriteria getFailCriteria() {
        return failCriteria;
    }

    /**
     * Sets the value of the failCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link PingCriteria }
     *     
     */
    public void setFailCriteria(PingCriteria value) {
        this.failCriteria = value;
    }

    /**
     * Gets the value of the packets property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPackets() {
        return packets;
    }

    /**
     * Sets the value of the packets property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPackets(Integer value) {
        this.packets = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSize(Integer value) {
        this.size = value;
    }

}

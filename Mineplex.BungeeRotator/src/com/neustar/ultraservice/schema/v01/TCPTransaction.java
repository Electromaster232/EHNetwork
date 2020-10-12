
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TCPTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TCPTransaction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="warningCriteria" type="{http://schema.ultraservice.neustar.com/v01/}TCPCriteria" minOccurs="0"/>
 *         &lt;element name="criticalCriteria" type="{http://schema.ultraservice.neustar.com/v01/}TCPCriteria" minOccurs="0"/>
 *         &lt;element name="failCriteria" type="{http://schema.ultraservice.neustar.com/v01/}TCPCriteria" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="port" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="controlIP" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TCPTransaction", propOrder = {
    "warningCriteria",
    "criticalCriteria",
    "failCriteria"
})
public class TCPTransaction {

    protected TCPCriteria warningCriteria;
    protected TCPCriteria criticalCriteria;
    protected TCPCriteria failCriteria;
    @XmlAttribute(name = "port", required = true)
    protected int port;
    @XmlAttribute(name = "controlIP")
    protected String controlIP;

    /**
     * Gets the value of the warningCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link TCPCriteria }
     *     
     */
    public TCPCriteria getWarningCriteria() {
        return warningCriteria;
    }

    /**
     * Sets the value of the warningCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCPCriteria }
     *     
     */
    public void setWarningCriteria(TCPCriteria value) {
        this.warningCriteria = value;
    }

    /**
     * Gets the value of the criticalCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link TCPCriteria }
     *     
     */
    public TCPCriteria getCriticalCriteria() {
        return criticalCriteria;
    }

    /**
     * Sets the value of the criticalCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCPCriteria }
     *     
     */
    public void setCriticalCriteria(TCPCriteria value) {
        this.criticalCriteria = value;
    }

    /**
     * Gets the value of the failCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link TCPCriteria }
     *     
     */
    public TCPCriteria getFailCriteria() {
        return failCriteria;
    }

    /**
     * Sets the value of the failCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCPCriteria }
     *     
     */
    public void setFailCriteria(TCPCriteria value) {
        this.failCriteria = value;
    }

    /**
     * Gets the value of the port property.
     * 
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     */
    public void setPort(int value) {
        this.port = value;
    }

    /**
     * Gets the value of the controlIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getControlIP() {
        return controlIP;
    }

    /**
     * Sets the value of the controlIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setControlIP(String value) {
        this.controlIP = value;
    }

}

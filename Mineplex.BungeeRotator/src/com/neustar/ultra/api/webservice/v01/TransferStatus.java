
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transferStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transferStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastUpdate" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nextUpdate" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="soaSerial" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="transferStatusType" type="{http://webservice.api.ultra.neustar.com/v01/}transferStatusType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transferStatus", propOrder = {
    "lastUpdate",
    "message",
    "nextUpdate",
    "soaSerial",
    "transferStatusType"
})
public class TransferStatus {

    protected long lastUpdate;
    protected String message;
    protected long nextUpdate;
    protected long soaSerial;
    protected TransferStatusType transferStatusType;

    /**
     * Gets the value of the lastUpdate property.
     * 
     */
    public long getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the value of the lastUpdate property.
     * 
     */
    public void setLastUpdate(long value) {
        this.lastUpdate = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the nextUpdate property.
     * 
     */
    public long getNextUpdate() {
        return nextUpdate;
    }

    /**
     * Sets the value of the nextUpdate property.
     * 
     */
    public void setNextUpdate(long value) {
        this.nextUpdate = value;
    }

    /**
     * Gets the value of the soaSerial property.
     * 
     */
    public long getSoaSerial() {
        return soaSerial;
    }

    /**
     * Sets the value of the soaSerial property.
     * 
     */
    public void setSoaSerial(long value) {
        this.soaSerial = value;
    }

    /**
     * Gets the value of the transferStatusType property.
     * 
     * @return
     *     possible object is
     *     {@link TransferStatusType }
     *     
     */
    public TransferStatusType getTransferStatusType() {
        return transferStatusType;
    }

    /**
     * Sets the value of the transferStatusType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferStatusType }
     *     
     */
    public void setTransferStatusType(TransferStatusType value) {
        this.transferStatusType = value;
    }

}

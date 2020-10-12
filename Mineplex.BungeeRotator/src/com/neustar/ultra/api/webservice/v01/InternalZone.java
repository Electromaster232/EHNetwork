
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for internalZone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="internalZone">
 *   &lt;complexContent>
 *     &lt;extension base="{http://webservice.api.ultra.neustar.com/v01/}zone">
 *       &lt;sequence>
 *         &lt;element name="auditSummary" type="{http://webservice.api.ultra.neustar.com/v01/}auditSummary" minOccurs="0"/>
 *         &lt;element name="transferInfo" type="{http://webservice.api.ultra.neustar.com/v01/}transferInfo" minOccurs="0"/>
 *         &lt;element name="transferStatus" type="{http://webservice.api.ultra.neustar.com/v01/}transferStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "internalZone", propOrder = {
    "auditSummary",
    "transferInfo",
    "transferStatus"
})
public class InternalZone
    extends Zone
{

    protected AuditSummary auditSummary;
    protected TransferInfo transferInfo;
    protected TransferStatus transferStatus;

    /**
     * Gets the value of the auditSummary property.
     * 
     * @return
     *     possible object is
     *     {@link AuditSummary }
     *     
     */
    public AuditSummary getAuditSummary() {
        return auditSummary;
    }

    /**
     * Sets the value of the auditSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuditSummary }
     *     
     */
    public void setAuditSummary(AuditSummary value) {
        this.auditSummary = value;
    }

    /**
     * Gets the value of the transferInfo property.
     * 
     * @return
     *     possible object is
     *     {@link TransferInfo }
     *     
     */
    public TransferInfo getTransferInfo() {
        return transferInfo;
    }

    /**
     * Sets the value of the transferInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferInfo }
     *     
     */
    public void setTransferInfo(TransferInfo value) {
        this.transferInfo = value;
    }

    /**
     * Gets the value of the transferStatus property.
     * 
     * @return
     *     possible object is
     *     {@link TransferStatus }
     *     
     */
    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    /**
     * Sets the value of the transferStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransferStatus }
     *     
     */
    public void setTransferStatus(TransferStatus value) {
        this.transferStatus = value;
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DNSProbeMaster complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DNSProbeMaster">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DNSProbeData" type="{http://schema.ultraservice.neustar.com/v01/}DNSProbeData"/>
 *         &lt;element name="FTPProbeData" type="{http://schema.ultraservice.neustar.com/v01/}FTPProbeData"/>
 *         &lt;element name="HTTPProbeData" type="{http://schema.ultraservice.neustar.com/v01/}HTTPProbeData"/>
 *         &lt;element name="PINGProbeData" type="{http://schema.ultraservice.neustar.com/v01/}PINGProbeData"/>
 *         &lt;element name="PROXYProbeData" type="{http://schema.ultraservice.neustar.com/v01/}PROXYProbeData"/>
 *         &lt;element name="SMTPAvailabilityProbeData" type="{http://schema.ultraservice.neustar.com/v01/}SMTPAvailabilityProbeData"/>
 *         &lt;element name="SMTPSendMailProbeData" type="{http://schema.ultraservice.neustar.com/v01/}SMTPSendMailProbeData"/>
 *         &lt;element name="TCPProbeData" type="{http://schema.ultraservice.neustar.com/v01/}TCPProbeData"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DNSProbeMaster", propOrder = {
    "dnsProbeData",
    "ftpProbeData",
    "httpProbeData",
    "pingProbeData",
    "proxyProbeData",
    "smtpAvailabilityProbeData",
    "smtpSendMailProbeData",
    "tcpProbeData"
})
public class DNSProbeMaster {

    @XmlElement(name = "DNSProbeData", required = true)
    protected DNSProbeData dnsProbeData;
    @XmlElement(name = "FTPProbeData", required = true)
    protected FTPProbeData ftpProbeData;
    @XmlElement(name = "HTTPProbeData", required = true)
    protected HTTPProbeData httpProbeData;
    @XmlElement(name = "PINGProbeData", required = true)
    protected PINGProbeData pingProbeData;
    @XmlElement(name = "PROXYProbeData", required = true)
    protected PROXYProbeData proxyProbeData;
    @XmlElement(name = "SMTPAvailabilityProbeData", required = true)
    protected SMTPAvailabilityProbeData smtpAvailabilityProbeData;
    @XmlElement(name = "SMTPSendMailProbeData", required = true)
    protected SMTPSendMailProbeData smtpSendMailProbeData;
    @XmlElement(name = "TCPProbeData", required = true)
    protected TCPProbeData tcpProbeData;

    /**
     * Gets the value of the dnsProbeData property.
     * 
     * @return
     *     possible object is
     *     {@link DNSProbeData }
     *     
     */
    public DNSProbeData getDNSProbeData() {
        return dnsProbeData;
    }

    /**
     * Sets the value of the dnsProbeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link DNSProbeData }
     *     
     */
    public void setDNSProbeData(DNSProbeData value) {
        this.dnsProbeData = value;
    }

    /**
     * Gets the value of the ftpProbeData property.
     * 
     * @return
     *     possible object is
     *     {@link FTPProbeData }
     *     
     */
    public FTPProbeData getFTPProbeData() {
        return ftpProbeData;
    }

    /**
     * Sets the value of the ftpProbeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link FTPProbeData }
     *     
     */
    public void setFTPProbeData(FTPProbeData value) {
        this.ftpProbeData = value;
    }

    /**
     * Gets the value of the httpProbeData property.
     * 
     * @return
     *     possible object is
     *     {@link HTTPProbeData }
     *     
     */
    public HTTPProbeData getHTTPProbeData() {
        return httpProbeData;
    }

    /**
     * Sets the value of the httpProbeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link HTTPProbeData }
     *     
     */
    public void setHTTPProbeData(HTTPProbeData value) {
        this.httpProbeData = value;
    }

    /**
     * Gets the value of the pingProbeData property.
     * 
     * @return
     *     possible object is
     *     {@link PINGProbeData }
     *     
     */
    public PINGProbeData getPINGProbeData() {
        return pingProbeData;
    }

    /**
     * Sets the value of the pingProbeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link PINGProbeData }
     *     
     */
    public void setPINGProbeData(PINGProbeData value) {
        this.pingProbeData = value;
    }

    /**
     * Gets the value of the proxyProbeData property.
     * 
     * @return
     *     possible object is
     *     {@link PROXYProbeData }
     *     
     */
    public PROXYProbeData getPROXYProbeData() {
        return proxyProbeData;
    }

    /**
     * Sets the value of the proxyProbeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link PROXYProbeData }
     *     
     */
    public void setPROXYProbeData(PROXYProbeData value) {
        this.proxyProbeData = value;
    }

    /**
     * Gets the value of the smtpAvailabilityProbeData property.
     * 
     * @return
     *     possible object is
     *     {@link SMTPAvailabilityProbeData }
     *     
     */
    public SMTPAvailabilityProbeData getSMTPAvailabilityProbeData() {
        return smtpAvailabilityProbeData;
    }

    /**
     * Sets the value of the smtpAvailabilityProbeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SMTPAvailabilityProbeData }
     *     
     */
    public void setSMTPAvailabilityProbeData(SMTPAvailabilityProbeData value) {
        this.smtpAvailabilityProbeData = value;
    }

    /**
     * Gets the value of the smtpSendMailProbeData property.
     * 
     * @return
     *     possible object is
     *     {@link SMTPSendMailProbeData }
     *     
     */
    public SMTPSendMailProbeData getSMTPSendMailProbeData() {
        return smtpSendMailProbeData;
    }

    /**
     * Sets the value of the smtpSendMailProbeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SMTPSendMailProbeData }
     *     
     */
    public void setSMTPSendMailProbeData(SMTPSendMailProbeData value) {
        this.smtpSendMailProbeData = value;
    }

    /**
     * Gets the value of the tcpProbeData property.
     * 
     * @return
     *     possible object is
     *     {@link TCPProbeData }
     *     
     */
    public TCPProbeData getTCPProbeData() {
        return tcpProbeData;
    }

    /**
     * Sets the value of the tcpProbeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCPProbeData }
     *     
     */
    public void setTCPProbeData(TCPProbeData value) {
        this.tcpProbeData = value;
    }

}

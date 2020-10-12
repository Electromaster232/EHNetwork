
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProbeDefinitionTransactions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProbeDefinitionTransactions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="httpTransactions" type="{http://schema.ultraservice.neustar.com/v01/}ProbeDefinitionHttpTransactions" minOccurs="0"/>
 *         &lt;element name="dnsTransaction" type="{http://schema.ultraservice.neustar.com/v01/}DNSTransaction" minOccurs="0"/>
 *         &lt;element name="pingTransaction" type="{http://schema.ultraservice.neustar.com/v01/}PINGTransaction" minOccurs="0"/>
 *         &lt;element name="tcpTransaction" type="{http://schema.ultraservice.neustar.com/v01/}TCPTransaction" minOccurs="0"/>
 *         &lt;element name="ftpTransaction" type="{http://schema.ultraservice.neustar.com/v01/}FTPTransaction" minOccurs="0"/>
 *         &lt;element name="smtpTransaction" type="{http://schema.ultraservice.neustar.com/v01/}SMTPTransaction" minOccurs="0"/>
 *         &lt;element name="smtp2Transaction" type="{http://schema.ultraservice.neustar.com/v01/}SMTP2Transaction" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProbeDefinitionTransactions", propOrder = {
    "httpTransactions",
    "dnsTransaction",
    "pingTransaction",
    "tcpTransaction",
    "ftpTransaction",
    "smtpTransaction",
    "smtp2Transaction"
})
public class ProbeDefinitionTransactions {

    protected ProbeDefinitionHttpTransactions httpTransactions;
    protected DNSTransaction dnsTransaction;
    protected PINGTransaction pingTransaction;
    protected TCPTransaction tcpTransaction;
    protected FTPTransaction ftpTransaction;
    protected SMTPTransaction smtpTransaction;
    protected SMTP2Transaction smtp2Transaction;

    /**
     * Gets the value of the httpTransactions property.
     * 
     * @return
     *     possible object is
     *     {@link ProbeDefinitionHttpTransactions }
     *     
     */
    public ProbeDefinitionHttpTransactions getHttpTransactions() {
        return httpTransactions;
    }

    /**
     * Sets the value of the httpTransactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProbeDefinitionHttpTransactions }
     *     
     */
    public void setHttpTransactions(ProbeDefinitionHttpTransactions value) {
        this.httpTransactions = value;
    }

    /**
     * Gets the value of the dnsTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link DNSTransaction }
     *     
     */
    public DNSTransaction getDnsTransaction() {
        return dnsTransaction;
    }

    /**
     * Sets the value of the dnsTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link DNSTransaction }
     *     
     */
    public void setDnsTransaction(DNSTransaction value) {
        this.dnsTransaction = value;
    }

    /**
     * Gets the value of the pingTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link PINGTransaction }
     *     
     */
    public PINGTransaction getPingTransaction() {
        return pingTransaction;
    }

    /**
     * Sets the value of the pingTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link PINGTransaction }
     *     
     */
    public void setPingTransaction(PINGTransaction value) {
        this.pingTransaction = value;
    }

    /**
     * Gets the value of the tcpTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link TCPTransaction }
     *     
     */
    public TCPTransaction getTcpTransaction() {
        return tcpTransaction;
    }

    /**
     * Sets the value of the tcpTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCPTransaction }
     *     
     */
    public void setTcpTransaction(TCPTransaction value) {
        this.tcpTransaction = value;
    }

    /**
     * Gets the value of the ftpTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link FTPTransaction }
     *     
     */
    public FTPTransaction getFtpTransaction() {
        return ftpTransaction;
    }

    /**
     * Sets the value of the ftpTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link FTPTransaction }
     *     
     */
    public void setFtpTransaction(FTPTransaction value) {
        this.ftpTransaction = value;
    }

    /**
     * Gets the value of the smtpTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link SMTPTransaction }
     *     
     */
    public SMTPTransaction getSmtpTransaction() {
        return smtpTransaction;
    }

    /**
     * Sets the value of the smtpTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link SMTPTransaction }
     *     
     */
    public void setSmtpTransaction(SMTPTransaction value) {
        this.smtpTransaction = value;
    }

    /**
     * Gets the value of the smtp2Transaction property.
     * 
     * @return
     *     possible object is
     *     {@link SMTP2Transaction }
     *     
     */
    public SMTP2Transaction getSmtp2Transaction() {
        return smtp2Transaction;
    }

    /**
     * Sets the value of the smtp2Transaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link SMTP2Transaction }
     *     
     */
    public void setSmtp2Transaction(SMTP2Transaction value) {
        this.smtp2Transaction = value;
    }

}

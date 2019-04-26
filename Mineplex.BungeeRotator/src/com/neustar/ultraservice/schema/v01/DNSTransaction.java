
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DNSTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DNSTransaction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="warningCriteria" type="{http://schema.ultraservice.neustar.com/v01/}DnsCriteria" minOccurs="0"/>
 *         &lt;element name="criticalCriteria" type="{http://schema.ultraservice.neustar.com/v01/}DnsCriteria" minOccurs="0"/>
 *         &lt;element name="failCriteria" type="{http://schema.ultraservice.neustar.com/v01/}DnsCriteria" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="tcp" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="recordType" type="{http://schema.ultraservice.neustar.com/v01/}RecordType" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DNSTransaction", propOrder = {
    "warningCriteria",
    "criticalCriteria",
    "failCriteria"
})
public class DNSTransaction {

    protected DnsCriteria warningCriteria;
    protected DnsCriteria criticalCriteria;
    protected DnsCriteria failCriteria;
    @XmlAttribute(name = "port")
    protected Integer port;
    @XmlAttribute(name = "tcp")
    protected Boolean tcp;
    @XmlAttribute(name = "recordType")
    protected RecordType recordType;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the warningCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link DnsCriteria }
     *     
     */
    public DnsCriteria getWarningCriteria() {
        return warningCriteria;
    }

    /**
     * Sets the value of the warningCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link DnsCriteria }
     *     
     */
    public void setWarningCriteria(DnsCriteria value) {
        this.warningCriteria = value;
    }

    /**
     * Gets the value of the criticalCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link DnsCriteria }
     *     
     */
    public DnsCriteria getCriticalCriteria() {
        return criticalCriteria;
    }

    /**
     * Sets the value of the criticalCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link DnsCriteria }
     *     
     */
    public void setCriticalCriteria(DnsCriteria value) {
        this.criticalCriteria = value;
    }

    /**
     * Gets the value of the failCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link DnsCriteria }
     *     
     */
    public DnsCriteria getFailCriteria() {
        return failCriteria;
    }

    /**
     * Sets the value of the failCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link DnsCriteria }
     *     
     */
    public void setFailCriteria(DnsCriteria value) {
        this.failCriteria = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPort(Integer value) {
        this.port = value;
    }

    /**
     * Gets the value of the tcp property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTcp() {
        return tcp;
    }

    /**
     * Sets the value of the tcp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTcp(Boolean value) {
        this.tcp = value;
    }

    /**
     * Gets the value of the recordType property.
     * 
     * @return
     *     possible object is
     *     {@link RecordType }
     *     
     */
    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * Sets the value of the recordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordType }
     *     
     */
    public void setRecordType(RecordType value) {
        this.recordType = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}

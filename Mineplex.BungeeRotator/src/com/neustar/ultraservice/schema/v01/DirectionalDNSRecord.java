
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirectionalDNSRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectionalDNSRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InfoValues" type="{http://schema.ultraservice.neustar.com/v01/}InfoValues"/>
 *       &lt;/sequence>
 *       &lt;attribute name="recordType" use="required" type="{http://schema.ultraservice.neustar.com/v01/}DirPoolRecordType" />
 *       &lt;attribute name="TTL" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="noResponseRecord" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectionalDNSRecord", propOrder = {
    "infoValues"
})
public class DirectionalDNSRecord {

    @XmlElement(name = "InfoValues", required = true)
    protected InfoValues infoValues;
    @XmlAttribute(name = "recordType", required = true)
    protected DirPoolRecordType recordType;
    @XmlAttribute(name = "TTL")
    protected String ttl;
    @XmlAttribute(name = "noResponseRecord", required = true)
    protected boolean noResponseRecord;

    /**
     * Gets the value of the infoValues property.
     * 
     * @return
     *     possible object is
     *     {@link InfoValues }
     *     
     */
    public InfoValues getInfoValues() {
        return infoValues;
    }

    /**
     * Sets the value of the infoValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoValues }
     *     
     */
    public void setInfoValues(InfoValues value) {
        this.infoValues = value;
    }

    /**
     * Gets the value of the recordType property.
     * 
     * @return
     *     possible object is
     *     {@link DirPoolRecordType }
     *     
     */
    public DirPoolRecordType getRecordType() {
        return recordType;
    }

    /**
     * Sets the value of the recordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirPoolRecordType }
     *     
     */
    public void setRecordType(DirPoolRecordType value) {
        this.recordType = value;
    }

    /**
     * Gets the value of the ttl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTTL() {
        return ttl;
    }

    /**
     * Sets the value of the ttl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTTL(String value) {
        this.ttl = value;
    }

    /**
     * Gets the value of the noResponseRecord property.
     * 
     */
    public boolean isNoResponseRecord() {
        return noResponseRecord;
    }

    /**
     * Sets the value of the noResponseRecord property.
     * 
     */
    public void setNoResponseRecord(boolean value) {
        this.noResponseRecord = value;
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AliasedDomainInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AliasedDomainInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="domainExpired" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="registerarName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nameServer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="guid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="zoneid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="zone" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="systemid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="status" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="scandate" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="zoneHealthMask" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AliasedDomainInfo")
public class AliasedDomainInfo {

    @XmlAttribute(name = "domainExpired", required = true)
    protected String domainExpired;
    @XmlAttribute(name = "registerarName", required = true)
    protected String registerarName;
    @XmlAttribute(name = "nameServer", required = true)
    protected String nameServer;
    @XmlAttribute(name = "guid", required = true)
    protected String guid;
    @XmlAttribute(name = "zoneid", required = true)
    protected String zoneid;
    @XmlAttribute(name = "zone", required = true)
    protected String zone;
    @XmlAttribute(name = "systemid", required = true)
    protected String systemid;
    @XmlAttribute(name = "status", required = true)
    protected String status;
    @XmlAttribute(name = "scandate", required = true)
    protected String scandate;
    @XmlAttribute(name = "zoneHealthMask", required = true)
    protected String zoneHealthMask;

    /**
     * Gets the value of the domainExpired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainExpired() {
        return domainExpired;
    }

    /**
     * Sets the value of the domainExpired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainExpired(String value) {
        this.domainExpired = value;
    }

    /**
     * Gets the value of the registerarName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegisterarName() {
        return registerarName;
    }

    /**
     * Sets the value of the registerarName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegisterarName(String value) {
        this.registerarName = value;
    }

    /**
     * Gets the value of the nameServer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameServer() {
        return nameServer;
    }

    /**
     * Sets the value of the nameServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameServer(String value) {
        this.nameServer = value;
    }

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
    }

    /**
     * Gets the value of the zoneid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneid() {
        return zoneid;
    }

    /**
     * Sets the value of the zoneid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneid(String value) {
        this.zoneid = value;
    }

    /**
     * Gets the value of the zone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZone() {
        return zone;
    }

    /**
     * Sets the value of the zone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZone(String value) {
        this.zone = value;
    }

    /**
     * Gets the value of the systemid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemid() {
        return systemid;
    }

    /**
     * Sets the value of the systemid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemid(String value) {
        this.systemid = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the scandate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScandate() {
        return scandate;
    }

    /**
     * Sets the value of the scandate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScandate(String value) {
        this.scandate = value;
    }

    /**
     * Gets the value of the zoneHealthMask property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneHealthMask() {
        return zoneHealthMask;
    }

    /**
     * Sets the value of the zoneHealthMask property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneHealthMask(String value) {
        this.zoneHealthMask = value;
    }

}

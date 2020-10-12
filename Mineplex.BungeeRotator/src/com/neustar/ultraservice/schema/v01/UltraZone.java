
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UltraZone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UltraZone">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="zoneName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="zoneType" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="accountId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="primarySrc" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="owner" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="zoneId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parentZoneGuid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dnssecStatus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="suspended" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UltraZone")
public class UltraZone {

    @XmlAttribute(name = "zoneName", required = true)
    protected String zoneName;
    @XmlAttribute(name = "zoneType", required = true)
    protected int zoneType;
    @XmlAttribute(name = "accountId")
    protected String accountId;
    @XmlAttribute(name = "primarySrc")
    protected String primarySrc;
    @XmlAttribute(name = "owner")
    protected String owner;
    @XmlAttribute(name = "zoneId")
    protected String zoneId;
    @XmlAttribute(name = "parentZoneGuid")
    protected String parentZoneGuid;
    @XmlAttribute(name = "dnssecStatus")
    protected String dnssecStatus;
    @XmlAttribute(name = "suspended")
    protected String suspended;

    /**
     * Gets the value of the zoneName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * Sets the value of the zoneName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneName(String value) {
        this.zoneName = value;
    }

    /**
     * Gets the value of the zoneType property.
     * 
     */
    public int getZoneType() {
        return zoneType;
    }

    /**
     * Sets the value of the zoneType property.
     * 
     */
    public void setZoneType(int value) {
        this.zoneType = value;
    }

    /**
     * Gets the value of the accountId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the value of the accountId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountId(String value) {
        this.accountId = value;
    }

    /**
     * Gets the value of the primarySrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimarySrc() {
        return primarySrc;
    }

    /**
     * Sets the value of the primarySrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimarySrc(String value) {
        this.primarySrc = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    /**
     * Gets the value of the zoneId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneId() {
        return zoneId;
    }

    /**
     * Sets the value of the zoneId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneId(String value) {
        this.zoneId = value;
    }

    /**
     * Gets the value of the parentZoneGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentZoneGuid() {
        return parentZoneGuid;
    }

    /**
     * Sets the value of the parentZoneGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentZoneGuid(String value) {
        this.parentZoneGuid = value;
    }

    /**
     * Gets the value of the dnssecStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDnssecStatus() {
        return dnssecStatus;
    }

    /**
     * Sets the value of the dnssecStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDnssecStatus(String value) {
        this.dnssecStatus = value;
    }

    /**
     * Gets the value of the suspended property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuspended() {
        return suspended;
    }

    /**
     * Sets the value of the suspended property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuspended(String value) {
        this.suspended = value;
    }

}

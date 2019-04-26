
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirectionalPoolData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectionalPoolData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="dirpoolid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Zoneid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Poolname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Pooldname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Sponsorid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="accountid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DirPoolType" type="{http://schema.ultraservice.neustar.com/v01/}DirPoolType" />
 *       &lt;attribute name="TieBreak" type="{http://schema.ultraservice.neustar.com/v01/}TieBreak" />
 *       &lt;attribute name="Description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectionalPoolData")
public class DirectionalPoolData {

    @XmlAttribute(name = "dirpoolid")
    protected String dirpoolid;
    @XmlAttribute(name = "Zoneid")
    protected String zoneid;
    @XmlAttribute(name = "Poolname")
    protected String poolname;
    @XmlAttribute(name = "Pooldname")
    protected String pooldname;
    @XmlAttribute(name = "Sponsorid")
    protected String sponsorid;
    @XmlAttribute(name = "accountid")
    protected String accountid;
    @XmlAttribute(name = "DirPoolType")
    protected DirPoolType dirPoolType;
    @XmlAttribute(name = "TieBreak")
    protected TieBreak tieBreak;
    @XmlAttribute(name = "Description")
    protected String description;

    /**
     * Gets the value of the dirpoolid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirpoolid() {
        return dirpoolid;
    }

    /**
     * Sets the value of the dirpoolid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirpoolid(String value) {
        this.dirpoolid = value;
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
     * Gets the value of the poolname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolname() {
        return poolname;
    }

    /**
     * Sets the value of the poolname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolname(String value) {
        this.poolname = value;
    }

    /**
     * Gets the value of the pooldname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPooldname() {
        return pooldname;
    }

    /**
     * Sets the value of the pooldname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPooldname(String value) {
        this.pooldname = value;
    }

    /**
     * Gets the value of the sponsorid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSponsorid() {
        return sponsorid;
    }

    /**
     * Sets the value of the sponsorid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSponsorid(String value) {
        this.sponsorid = value;
    }

    /**
     * Gets the value of the accountid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountid() {
        return accountid;
    }

    /**
     * Sets the value of the accountid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountid(String value) {
        this.accountid = value;
    }

    /**
     * Gets the value of the dirPoolType property.
     * 
     * @return
     *     possible object is
     *     {@link DirPoolType }
     *     
     */
    public DirPoolType getDirPoolType() {
        return dirPoolType;
    }

    /**
     * Sets the value of the dirPoolType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirPoolType }
     *     
     */
    public void setDirPoolType(DirPoolType value) {
        this.dirPoolType = value;
    }

    /**
     * Gets the value of the tieBreak property.
     * 
     * @return
     *     possible object is
     *     {@link TieBreak }
     *     
     */
    public TieBreak getTieBreak() {
        return tieBreak;
    }

    /**
     * Sets the value of the tieBreak property.
     * 
     * @param value
     *     allowed object is
     *     {@link TieBreak }
     *     
     */
    public void setTieBreak(TieBreak value) {
        this.tieBreak = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}

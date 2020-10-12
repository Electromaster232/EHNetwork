
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateDirectionalPoolData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateDirectionalPoolData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dirPoolID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dirPoolType" type="{http://schema.ultraservice.neustar.com/v01/}DirPoolType" />
 *       &lt;attribute name="tieBreak" type="{http://schema.ultraservice.neustar.com/v01/}TieBreak" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateDirectionalPoolData")
public class UpdateDirectionalPoolData {

    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "dirPoolID", required = true)
    protected String dirPoolID;
    @XmlAttribute(name = "dirPoolType")
    protected DirPoolType dirPoolType;
    @XmlAttribute(name = "tieBreak")
    protected TieBreak tieBreak;

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

    /**
     * Gets the value of the dirPoolID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirPoolID() {
        return dirPoolID;
    }

    /**
     * Sets the value of the dirPoolID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirPoolID(String value) {
        this.dirPoolID = value;
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

}

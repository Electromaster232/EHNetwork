
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceRecordToCreate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceRecordToCreate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InfoValues" type="{http://schema.ultraservice.neustar.com/v01/}InfoValues"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ZoneName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="DName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TTL" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceRecordToCreate", propOrder = {
    "infoValues"
})
public class ResourceRecordToCreate {

    @XmlElement(name = "InfoValues", required = true)
    protected InfoValues infoValues;
    @XmlAttribute(name = "ZoneName", required = true)
    protected String zoneName;
    @XmlAttribute(name = "Type", required = true)
    protected int type;
    @XmlAttribute(name = "DName", required = true)
    protected String dName;
    @XmlAttribute(name = "TTL")
    protected String ttl;

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
     * Gets the value of the type property.
     * 
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     */
    public void setType(int value) {
        this.type = value;
    }

    /**
     * Gets the value of the dName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDName() {
        return dName;
    }

    /**
     * Sets the value of the dName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDName(String value) {
        this.dName = value;
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

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddDirectionalPoolData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddDirectionalPoolData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DirectionalRecordData" type="{http://schema.ultraservice.neustar.com/v01/}DirectionalRecordData"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dirPoolType" use="required" type="{http://schema.ultraservice.neustar.com/v01/}DirPoolType" />
 *       &lt;attribute name="poolRecordType" use="required" type="{http://schema.ultraservice.neustar.com/v01/}DirPoolRecordType" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="zoneName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="hostName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddDirectionalPoolData", propOrder = {
    "directionalRecordData"
})
public class AddDirectionalPoolData {

    @XmlElement(name = "DirectionalRecordData", required = true, nillable = true)
    protected DirectionalRecordData directionalRecordData;
    @XmlAttribute(name = "dirPoolType", required = true)
    protected DirPoolType dirPoolType;
    @XmlAttribute(name = "poolRecordType", required = true)
    protected DirPoolRecordType poolRecordType;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "zoneName", required = true)
    protected String zoneName;
    @XmlAttribute(name = "hostName", required = true)
    protected String hostName;

    /**
     * Gets the value of the directionalRecordData property.
     * 
     * @return
     *     possible object is
     *     {@link DirectionalRecordData }
     *     
     */
    public DirectionalRecordData getDirectionalRecordData() {
        return directionalRecordData;
    }

    /**
     * Sets the value of the directionalRecordData property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirectionalRecordData }
     *     
     */
    public void setDirectionalRecordData(DirectionalRecordData value) {
        this.directionalRecordData = value;
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
     * Gets the value of the poolRecordType property.
     * 
     * @return
     *     possible object is
     *     {@link DirPoolRecordType }
     *     
     */
    public DirPoolRecordType getPoolRecordType() {
        return poolRecordType;
    }

    /**
     * Sets the value of the poolRecordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirPoolRecordType }
     *     
     */
    public void setPoolRecordType(DirPoolRecordType value) {
        this.poolRecordType = value;
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
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

}

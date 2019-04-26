
package com.neustar.ultra.api.webservice.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for primaryZoneInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="primaryZoneInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="createType" type="{http://webservice.api.ultra.neustar.com/v01/}createType" minOccurs="0"/>
 *         &lt;element name="forceImport" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="nameServer" type="{http://webservice.api.ultra.neustar.com/v01/}nameServer" minOccurs="0"/>
 *         &lt;element name="originalZoneName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="restrictIPList" type="{http://webservice.api.ultra.neustar.com/v01/}restrictIP" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "primaryZoneInfo", propOrder = {
    "createType",
    "forceImport",
    "nameServer",
    "originalZoneName",
    "restrictIPList"
})
public class PrimaryZoneInfo {

    protected CreateType createType;
    protected Boolean forceImport;
    protected NameServer nameServer;
    protected String originalZoneName;
    @XmlElement(nillable = true)
    protected List<RestrictIP> restrictIPList;

    /**
     * Gets the value of the createType property.
     * 
     * @return
     *     possible object is
     *     {@link CreateType }
     *     
     */
    public CreateType getCreateType() {
        return createType;
    }

    /**
     * Sets the value of the createType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateType }
     *     
     */
    public void setCreateType(CreateType value) {
        this.createType = value;
    }

    /**
     * Gets the value of the forceImport property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isForceImport() {
        return forceImport;
    }

    /**
     * Sets the value of the forceImport property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setForceImport(Boolean value) {
        this.forceImport = value;
    }

    /**
     * Gets the value of the nameServer property.
     * 
     * @return
     *     possible object is
     *     {@link NameServer }
     *     
     */
    public NameServer getNameServer() {
        return nameServer;
    }

    /**
     * Sets the value of the nameServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameServer }
     *     
     */
    public void setNameServer(NameServer value) {
        this.nameServer = value;
    }

    /**
     * Gets the value of the originalZoneName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalZoneName() {
        return originalZoneName;
    }

    /**
     * Sets the value of the originalZoneName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalZoneName(String value) {
        this.originalZoneName = value;
    }

    /**
     * Gets the value of the restrictIPList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the restrictIPList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRestrictIPList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RestrictIP }
     * 
     * 
     */
    public List<RestrictIP> getRestrictIPList() {
        if (restrictIPList == null) {
            restrictIPList = new ArrayList<RestrictIP>();
        }
        return this.restrictIPList;
    }

}

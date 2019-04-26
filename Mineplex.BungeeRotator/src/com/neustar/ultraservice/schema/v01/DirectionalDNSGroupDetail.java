
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirectionalDNSGroupDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectionalDNSGroupDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DirectionalDNSRegion" type="{http://schema.ultraservice.neustar.com/v01/}RegionForNewGroupsList"/>
 *         &lt;element name="DirectionalDNSSourceIPList" type="{http://schema.ultraservice.neustar.com/v01/}SourceIpGroupDefinitionList"/>
 *       &lt;/sequence>
 *       &lt;attribute name="GroupName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectionalDNSGroupDetail", propOrder = {
    "directionalDNSRegion",
    "directionalDNSSourceIPList"
})
@XmlSeeAlso({
    GlobalDirectionalGroupUpdateDetails.class,
    GlobalDirectionalGroupDetails.class
})
public class DirectionalDNSGroupDetail {

    @XmlElement(name = "DirectionalDNSRegion", required = true)
    protected RegionForNewGroupsList directionalDNSRegion;
    @XmlElement(name = "DirectionalDNSSourceIPList", required = true)
    protected SourceIpGroupDefinitionList directionalDNSSourceIPList;
    @XmlAttribute(name = "GroupName", required = true)
    protected String groupName;
    @XmlAttribute(name = "Description")
    protected String description;

    /**
     * Gets the value of the directionalDNSRegion property.
     * 
     * @return
     *     possible object is
     *     {@link RegionForNewGroupsList }
     *     
     */
    public RegionForNewGroupsList getDirectionalDNSRegion() {
        return directionalDNSRegion;
    }

    /**
     * Sets the value of the directionalDNSRegion property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegionForNewGroupsList }
     *     
     */
    public void setDirectionalDNSRegion(RegionForNewGroupsList value) {
        this.directionalDNSRegion = value;
    }

    /**
     * Gets the value of the directionalDNSSourceIPList property.
     * 
     * @return
     *     possible object is
     *     {@link SourceIpGroupDefinitionList }
     *     
     */
    public SourceIpGroupDefinitionList getDirectionalDNSSourceIPList() {
        return directionalDNSSourceIPList;
    }

    /**
     * Sets the value of the directionalDNSSourceIPList property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceIpGroupDefinitionList }
     *     
     */
    public void setDirectionalDNSSourceIPList(SourceIpGroupDefinitionList value) {
        this.directionalDNSSourceIPList = value;
    }

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
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

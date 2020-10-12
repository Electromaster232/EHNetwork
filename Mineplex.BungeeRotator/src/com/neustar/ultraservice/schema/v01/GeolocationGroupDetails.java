
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeolocationGroupDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeolocationGroupDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GeolocationGroupDefinitionData" type="{http://schema.ultraservice.neustar.com/v01/}GeolocationGroupDefinitionData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="groupName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeolocationGroupDetails", propOrder = {
    "geolocationGroupDefinitionData"
})
@XmlSeeAlso({
    UpdateGeolocationGroupDetails.class
})
public class GeolocationGroupDetails {

    @XmlElement(name = "GeolocationGroupDefinitionData", required = true)
    protected List<GeolocationGroupDefinitionData> geolocationGroupDefinitionData;
    @XmlAttribute(name = "groupName", required = true)
    protected String groupName;
    @XmlAttribute(name = "description")
    protected String description;

    /**
     * Gets the value of the geolocationGroupDefinitionData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geolocationGroupDefinitionData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGeolocationGroupDefinitionData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GeolocationGroupDefinitionData }
     * 
     * 
     */
    public List<GeolocationGroupDefinitionData> getGeolocationGroupDefinitionData() {
        if (geolocationGroupDefinitionData == null) {
            geolocationGroupDefinitionData = new ArrayList<GeolocationGroupDefinitionData>();
        }
        return this.geolocationGroupDefinitionData;
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

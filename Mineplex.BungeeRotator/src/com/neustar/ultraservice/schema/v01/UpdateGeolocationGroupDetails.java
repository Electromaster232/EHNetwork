
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateGeolocationGroupDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateGeolocationGroupDetails">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schema.ultraservice.neustar.com/v01/}GeolocationGroupDetails">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="removeGroup" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateGeolocationGroupDetails")
public class UpdateGeolocationGroupDetails
    extends GeolocationGroupDetails
{

    @XmlAttribute(name = "removeGroup")
    protected Boolean removeGroup;

    /**
     * Gets the value of the removeGroup property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRemoveGroup() {
        return removeGroup;
    }

    /**
     * Sets the value of the removeGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRemoveGroup(Boolean value) {
        this.removeGroup = value;
    }

}

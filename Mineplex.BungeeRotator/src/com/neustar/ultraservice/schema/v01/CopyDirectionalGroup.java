
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CopyDirectionalGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CopyDirectionalGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="existingGroupID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="newGroupName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CopyDirectionalGroup")
public class CopyDirectionalGroup {

    @XmlAttribute(name = "existingGroupID", required = true)
    protected String existingGroupID;
    @XmlAttribute(name = "newGroupName", required = true)
    protected String newGroupName;

    /**
     * Gets the value of the existingGroupID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExistingGroupID() {
        return existingGroupID;
    }

    /**
     * Sets the value of the existingGroupID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExistingGroupID(String value) {
        this.existingGroupID = value;
    }

    /**
     * Gets the value of the newGroupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewGroupName() {
        return newGroupName;
    }

    /**
     * Sets the value of the newGroupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewGroupName(String value) {
        this.newGroupName = value;
    }

}

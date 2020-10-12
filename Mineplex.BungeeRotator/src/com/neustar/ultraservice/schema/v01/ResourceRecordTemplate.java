
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceRecordTemplate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceRecordTemplate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InfoTypes" type="{http://schema.ultraservice.neustar.com/v01/}InfoTypes"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceRecordTemplate", propOrder = {
    "infoTypes"
})
public class ResourceRecordTemplate {

    @XmlElement(name = "InfoTypes", required = true)
    protected InfoTypes infoTypes;
    @XmlAttribute(name = "Type", required = true)
    protected int type;

    /**
     * Gets the value of the infoTypes property.
     * 
     * @return
     *     possible object is
     *     {@link InfoTypes }
     *     
     */
    public InfoTypes getInfoTypes() {
        return infoTypes;
    }

    /**
     * Sets the value of the infoTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoTypes }
     *     
     */
    public void setInfoTypes(InfoTypes value) {
        this.infoTypes = value;
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

}

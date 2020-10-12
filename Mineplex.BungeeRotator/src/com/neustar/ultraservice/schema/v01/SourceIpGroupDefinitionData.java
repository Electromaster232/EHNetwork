
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SourceIpGroupDefinitionData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SourceIpGroupDefinitionData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="startIpAddress" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="endIpAddress" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourceIpGroupDefinitionData")
public class SourceIpGroupDefinitionData {

    @XmlAttribute(name = "startIpAddress", required = true)
    protected String startIpAddress;
    @XmlAttribute(name = "endIpAddress")
    protected String endIpAddress;

    /**
     * Gets the value of the startIpAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartIpAddress() {
        return startIpAddress;
    }

    /**
     * Sets the value of the startIpAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartIpAddress(String value) {
        this.startIpAddress = value;
    }

    /**
     * Gets the value of the endIpAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndIpAddress() {
        return endIpAddress;
    }

    /**
     * Sets the value of the endIpAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndIpAddress(String value) {
        this.endIpAddress = value;
    }

}

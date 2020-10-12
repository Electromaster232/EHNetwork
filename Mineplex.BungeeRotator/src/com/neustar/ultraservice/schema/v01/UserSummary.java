
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserSummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserSummary">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="servicePackageName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="numberOfRecords" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserSummary")
public class UserSummary {

    @XmlAttribute(name = "servicePackageName")
    protected String servicePackageName;
    @XmlAttribute(name = "numberOfRecords")
    protected String numberOfRecords;

    /**
     * Gets the value of the servicePackageName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServicePackageName() {
        return servicePackageName;
    }

    /**
     * Sets the value of the servicePackageName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServicePackageName(String value) {
        this.servicePackageName = value;
    }

    /**
     * Gets the value of the numberOfRecords property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfRecords() {
        return numberOfRecords;
    }

    /**
     * Sets the value of the numberOfRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfRecords(String value) {
        this.numberOfRecords = value;
    }

}

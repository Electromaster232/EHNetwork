
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DefaultNumberOfrecordsPreference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DefaultNumberOfrecordsPreference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="LargeTable" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SmallTable" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isDefault" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefaultNumberOfrecordsPreference")
public class DefaultNumberOfrecordsPreference {

    @XmlAttribute(name = "LargeTable")
    protected String largeTable;
    @XmlAttribute(name = "SmallTable")
    protected String smallTable;
    @XmlAttribute(name = "isDefault")
    protected Boolean isDefault;

    /**
     * Gets the value of the largeTable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLargeTable() {
        return largeTable;
    }

    /**
     * Sets the value of the largeTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLargeTable(String value) {
        this.largeTable = value;
    }

    /**
     * Gets the value of the smallTable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmallTable() {
        return smallTable;
    }

    /**
     * Sets the value of the smallTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmallTable(String value) {
        this.smallTable = value;
    }

    /**
     * Gets the value of the isDefault property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDefault() {
        return isDefault;
    }

    /**
     * Sets the value of the isDefault property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDefault(Boolean value) {
        this.isDefault = value;
    }

}

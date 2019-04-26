
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for InfoTypes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InfoTypes">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="Info1Type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info2Type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info3Type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info4Type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info5Type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info6Type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info7Type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info8Type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoTypes", propOrder = {
    "value"
})
public class InfoTypes {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "Info1Type")
    protected String info1Type;
    @XmlAttribute(name = "Info2Type")
    protected String info2Type;
    @XmlAttribute(name = "Info3Type")
    protected String info3Type;
    @XmlAttribute(name = "Info4Type")
    protected String info4Type;
    @XmlAttribute(name = "Info5Type")
    protected String info5Type;
    @XmlAttribute(name = "Info6Type")
    protected String info6Type;
    @XmlAttribute(name = "Info7Type")
    protected String info7Type;
    @XmlAttribute(name = "Info8Type")
    protected String info8Type;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the info1Type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo1Type() {
        return info1Type;
    }

    /**
     * Sets the value of the info1Type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo1Type(String value) {
        this.info1Type = value;
    }

    /**
     * Gets the value of the info2Type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo2Type() {
        return info2Type;
    }

    /**
     * Sets the value of the info2Type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo2Type(String value) {
        this.info2Type = value;
    }

    /**
     * Gets the value of the info3Type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo3Type() {
        return info3Type;
    }

    /**
     * Sets the value of the info3Type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo3Type(String value) {
        this.info3Type = value;
    }

    /**
     * Gets the value of the info4Type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo4Type() {
        return info4Type;
    }

    /**
     * Sets the value of the info4Type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo4Type(String value) {
        this.info4Type = value;
    }

    /**
     * Gets the value of the info5Type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo5Type() {
        return info5Type;
    }

    /**
     * Sets the value of the info5Type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo5Type(String value) {
        this.info5Type = value;
    }

    /**
     * Gets the value of the info6Type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo6Type() {
        return info6Type;
    }

    /**
     * Sets the value of the info6Type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo6Type(String value) {
        this.info6Type = value;
    }

    /**
     * Gets the value of the info7Type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo7Type() {
        return info7Type;
    }

    /**
     * Sets the value of the info7Type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo7Type(String value) {
        this.info7Type = value;
    }

    /**
     * Gets the value of the info8Type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo8Type() {
        return info8Type;
    }

    /**
     * Sets the value of the info8Type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo8Type(String value) {
        this.info8Type = value;
    }

}

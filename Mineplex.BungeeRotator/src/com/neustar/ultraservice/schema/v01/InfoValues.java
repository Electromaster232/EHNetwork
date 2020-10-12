
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for InfoValues complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InfoValues">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="Info1Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info2Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info3Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info4Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info5Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info6Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info7Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Info8Value" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoValues", propOrder = {
    "value"
})
public class InfoValues {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "Info1Value")
    protected String info1Value;
    @XmlAttribute(name = "Info2Value")
    protected String info2Value;
    @XmlAttribute(name = "Info3Value")
    protected String info3Value;
    @XmlAttribute(name = "Info4Value")
    protected String info4Value;
    @XmlAttribute(name = "Info5Value")
    protected String info5Value;
    @XmlAttribute(name = "Info6Value")
    protected String info6Value;
    @XmlAttribute(name = "Info7Value")
    protected String info7Value;
    @XmlAttribute(name = "Info8Value")
    protected String info8Value;

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
     * Gets the value of the info1Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo1Value() {
        return info1Value;
    }

    /**
     * Sets the value of the info1Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo1Value(String value) {
        this.info1Value = value;
    }

    /**
     * Gets the value of the info2Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo2Value() {
        return info2Value;
    }

    /**
     * Sets the value of the info2Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo2Value(String value) {
        this.info2Value = value;
    }

    /**
     * Gets the value of the info3Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo3Value() {
        return info3Value;
    }

    /**
     * Sets the value of the info3Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo3Value(String value) {
        this.info3Value = value;
    }

    /**
     * Gets the value of the info4Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo4Value() {
        return info4Value;
    }

    /**
     * Sets the value of the info4Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo4Value(String value) {
        this.info4Value = value;
    }

    /**
     * Gets the value of the info5Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo5Value() {
        return info5Value;
    }

    /**
     * Sets the value of the info5Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo5Value(String value) {
        this.info5Value = value;
    }

    /**
     * Gets the value of the info6Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo6Value() {
        return info6Value;
    }

    /**
     * Sets the value of the info6Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo6Value(String value) {
        this.info6Value = value;
    }

    /**
     * Gets the value of the info7Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo7Value() {
        return info7Value;
    }

    /**
     * Sets the value of the info7Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo7Value(String value) {
        this.info7Value = value;
    }

    /**
     * Gets the value of the info8Value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo8Value() {
        return info8Value;
    }

    /**
     * Sets the value of the info8Value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo8Value(String value) {
        this.info8Value = value;
    }

}

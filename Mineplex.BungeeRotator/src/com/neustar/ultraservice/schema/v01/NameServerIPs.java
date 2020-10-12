
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NameServerIPs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NameServerIPs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="NameServerIP1" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NameServerIP2" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NameServerIP3" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TsigEnabled" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NameServerIPs")
public class NameServerIPs {

    @XmlAttribute(name = "NameServerIP1", required = true)
    protected String nameServerIP1;
    @XmlAttribute(name = "NameServerIP2")
    protected String nameServerIP2;
    @XmlAttribute(name = "NameServerIP3")
    protected String nameServerIP3;
    @XmlAttribute(name = "TsigEnabled")
    protected String tsigEnabled;

    /**
     * Gets the value of the nameServerIP1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameServerIP1() {
        return nameServerIP1;
    }

    /**
     * Sets the value of the nameServerIP1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameServerIP1(String value) {
        this.nameServerIP1 = value;
    }

    /**
     * Gets the value of the nameServerIP2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameServerIP2() {
        return nameServerIP2;
    }

    /**
     * Sets the value of the nameServerIP2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameServerIP2(String value) {
        this.nameServerIP2 = value;
    }

    /**
     * Gets the value of the nameServerIP3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameServerIP3() {
        return nameServerIP3;
    }

    /**
     * Sets the value of the nameServerIP3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameServerIP3(String value) {
        this.nameServerIP3 = value;
    }

    /**
     * Gets the value of the tsigEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTsigEnabled() {
        return tsigEnabled;
    }

    /**
     * Sets the value of the tsigEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTsigEnabled(String value) {
        this.tsigEnabled = value;
    }

}

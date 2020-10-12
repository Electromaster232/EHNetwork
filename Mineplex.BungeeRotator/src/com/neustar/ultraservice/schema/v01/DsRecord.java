
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DsRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DsRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="dsrecordtext" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DsRecord")
public class DsRecord {

    @XmlAttribute(name = "dsrecordtext")
    protected String dsrecordtext;

    /**
     * Gets the value of the dsrecordtext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDsrecordtext() {
        return dsrecordtext;
    }

    /**
     * Sets the value of the dsrecordtext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDsrecordtext(String value) {
        this.dsrecordtext = value;
    }

}

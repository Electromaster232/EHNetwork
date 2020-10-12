
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NameServerData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NameServerData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="actualNameServer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="recomendedNameServer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NameServerData")
public class NameServerData {

    @XmlAttribute(name = "actualNameServer", required = true)
    protected String actualNameServer;
    @XmlAttribute(name = "recomendedNameServer", required = true)
    protected String recomendedNameServer;

    /**
     * Gets the value of the actualNameServer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActualNameServer() {
        return actualNameServer;
    }

    /**
     * Sets the value of the actualNameServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActualNameServer(String value) {
        this.actualNameServer = value;
    }

    /**
     * Gets the value of the recomendedNameServer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecomendedNameServer() {
        return recomendedNameServer;
    }

    /**
     * Sets the value of the recomendedNameServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecomendedNameServer(String value) {
        this.recomendedNameServer = value;
    }

}

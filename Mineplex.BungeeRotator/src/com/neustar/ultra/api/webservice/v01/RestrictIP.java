
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for restrictIP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="restrictIP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="endIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tsigKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tsigKeyValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "restrictIP", propOrder = {
    "comment",
    "endIP",
    "startIP",
    "tsigKey",
    "tsigKeyValue"
})
public class RestrictIP {

    protected String comment;
    protected String endIP;
    protected String startIP;
    protected String tsigKey;
    protected String tsigKeyValue;

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the endIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndIP() {
        return endIP;
    }

    /**
     * Sets the value of the endIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndIP(String value) {
        this.endIP = value;
    }

    /**
     * Gets the value of the startIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartIP() {
        return startIP;
    }

    /**
     * Sets the value of the startIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartIP(String value) {
        this.startIP = value;
    }

    /**
     * Gets the value of the tsigKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTsigKey() {
        return tsigKey;
    }

    /**
     * Sets the value of the tsigKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTsigKey(String value) {
        this.tsigKey = value;
    }

    /**
     * Gets the value of the tsigKeyValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTsigKeyValue() {
        return tsigKeyValue;
    }

    /**
     * Sets the value of the tsigKeyValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTsigKeyValue(String value) {
        this.tsigKeyValue = value;
    }

}

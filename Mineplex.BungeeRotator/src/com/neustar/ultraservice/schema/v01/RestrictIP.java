
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RestrictIP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RestrictIP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="guid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="startIP" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="endIP" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="keyID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="comments" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="modified" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RestrictIP")
public class RestrictIP {

    @XmlAttribute(name = "guid", required = true)
    protected String guid;
    @XmlAttribute(name = "startIP", required = true)
    protected String startIP;
    @XmlAttribute(name = "endIP", required = true)
    protected String endIP;
    @XmlAttribute(name = "keyID", required = true)
    protected String keyID;
    @XmlAttribute(name = "comments", required = true)
    protected String comments;
    @XmlAttribute(name = "modified", required = true)
    protected String modified;

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
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
     * Gets the value of the keyID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyID() {
        return keyID;
    }

    /**
     * Sets the value of the keyID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyID(String value) {
        this.keyID = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

    /**
     * Gets the value of the modified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModified() {
        return modified;
    }

    /**
     * Sets the value of the modified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModified(String value) {
        this.modified = value;
    }

}

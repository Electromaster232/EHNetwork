
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Web_Forward_Record complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Web_Forward_Record">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="ZoneName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ForwardType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="RedirectTo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="RequestTo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Advanced" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="Guid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Web_Forward_Record")
public class WebForwardRecord {

    @XmlAttribute(name = "ZoneName", required = true)
    protected String zoneName;
    @XmlAttribute(name = "ForwardType", required = true)
    protected String forwardType;
    @XmlAttribute(name = "RedirectTo", required = true)
    protected String redirectTo;
    @XmlAttribute(name = "RequestTo", required = true)
    protected String requestTo;
    @XmlAttribute(name = "Advanced", required = true)
    protected boolean advanced;
    @XmlAttribute(name = "Guid", required = true)
    protected String guid;

    /**
     * Gets the value of the zoneName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * Sets the value of the zoneName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneName(String value) {
        this.zoneName = value;
    }

    /**
     * Gets the value of the forwardType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForwardType() {
        return forwardType;
    }

    /**
     * Sets the value of the forwardType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForwardType(String value) {
        this.forwardType = value;
    }

    /**
     * Gets the value of the redirectTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRedirectTo() {
        return redirectTo;
    }

    /**
     * Sets the value of the redirectTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRedirectTo(String value) {
        this.redirectTo = value;
    }

    /**
     * Gets the value of the requestTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestTo() {
        return requestTo;
    }

    /**
     * Sets the value of the requestTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestTo(String value) {
        this.requestTo = value;
    }

    /**
     * Gets the value of the advanced property.
     * 
     */
    public boolean isAdvanced() {
        return advanced;
    }

    /**
     * Sets the value of the advanced property.
     * 
     */
    public void setAdvanced(boolean value) {
        this.advanced = value;
    }

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

}

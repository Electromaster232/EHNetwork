
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WebForwardPoolRecordData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WebForwardPoolRecordData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HeaderRule" type="{http://schema.ultraservice.neustar.com/v01/}HeaderRule" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="redirectsTo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parentGuid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="redirectType" use="required" type="{http://schema.ultraservice.neustar.com/v01/}RedirectType" />
 *       &lt;attribute name="priority" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebForwardPoolRecordData", propOrder = {
    "headerRule"
})
@XmlSeeAlso({
    WebForwardPoolRecordDataGuid.class
})
public class WebForwardPoolRecordData {

    @XmlElement(name = "HeaderRule")
    protected List<HeaderRule> headerRule;
    @XmlAttribute(name = "redirectsTo", required = true)
    protected String redirectsTo;
    @XmlAttribute(name = "parentGuid", required = true)
    protected String parentGuid;
    @XmlAttribute(name = "redirectType", required = true)
    protected RedirectType redirectType;
    @XmlAttribute(name = "priority", required = true)
    protected long priority;

    /**
     * Gets the value of the headerRule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the headerRule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHeaderRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HeaderRule }
     * 
     * 
     */
    public List<HeaderRule> getHeaderRule() {
        if (headerRule == null) {
            headerRule = new ArrayList<HeaderRule>();
        }
        return this.headerRule;
    }

    /**
     * Gets the value of the redirectsTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRedirectsTo() {
        return redirectsTo;
    }

    /**
     * Sets the value of the redirectsTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRedirectsTo(String value) {
        this.redirectsTo = value;
    }

    /**
     * Gets the value of the parentGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentGuid() {
        return parentGuid;
    }

    /**
     * Sets the value of the parentGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentGuid(String value) {
        this.parentGuid = value;
    }

    /**
     * Gets the value of the redirectType property.
     * 
     * @return
     *     possible object is
     *     {@link RedirectType }
     *     
     */
    public RedirectType getRedirectType() {
        return redirectType;
    }

    /**
     * Sets the value of the redirectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RedirectType }
     *     
     */
    public void setRedirectType(RedirectType value) {
        this.redirectType = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     */
    public long getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     */
    public void setPriority(long value) {
        this.priority = value;
    }

}

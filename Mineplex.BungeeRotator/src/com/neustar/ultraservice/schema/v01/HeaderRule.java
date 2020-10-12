
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HeaderRule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HeaderRule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="headerField" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="matchCriteria" use="required" type="{http://schema.ultraservice.neustar.com/v01/}MatchCriteria" />
 *       &lt;attribute name="headerValue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="caseInsensitive" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeaderRule")
public class HeaderRule {

    @XmlAttribute(name = "headerField", required = true)
    protected String headerField;
    @XmlAttribute(name = "matchCriteria", required = true)
    protected MatchCriteria matchCriteria;
    @XmlAttribute(name = "headerValue", required = true)
    protected String headerValue;
    @XmlAttribute(name = "caseInsensitive", required = true)
    protected boolean caseInsensitive;

    /**
     * Gets the value of the headerField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeaderField() {
        return headerField;
    }

    /**
     * Sets the value of the headerField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeaderField(String value) {
        this.headerField = value;
    }

    /**
     * Gets the value of the matchCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link MatchCriteria }
     *     
     */
    public MatchCriteria getMatchCriteria() {
        return matchCriteria;
    }

    /**
     * Sets the value of the matchCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchCriteria }
     *     
     */
    public void setMatchCriteria(MatchCriteria value) {
        this.matchCriteria = value;
    }

    /**
     * Gets the value of the headerValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeaderValue() {
        return headerValue;
    }

    /**
     * Sets the value of the headerValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeaderValue(String value) {
        this.headerValue = value;
    }

    /**
     * Gets the value of the caseInsensitive property.
     * 
     */
    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    /**
     * Sets the value of the caseInsensitive property.
     * 
     */
    public void setCaseInsensitive(boolean value) {
        this.caseInsensitive = value;
    }

}

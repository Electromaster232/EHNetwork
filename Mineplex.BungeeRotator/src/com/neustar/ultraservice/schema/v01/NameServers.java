
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NameServers complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NameServers">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NameServerRecordSet" type="{http://schema.ultraservice.neustar.com/v01/}NameServerRecordSet" maxOccurs="unbounded"/>
 *         &lt;element name="NameServerRecordSet" type="{http://schema.ultraservice.neustar.com/v01/}NameServerRecordSet" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NameServers", propOrder = {
    "content"
})
public class NameServers {

    @XmlElementRef(name = "NameServerRecordSet", namespace = "http://schema.ultraservice.neustar.com/v01/", type = JAXBElement.class)
    protected List<JAXBElement<NameServerRecordSet>> content;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "NameServerRecordSet" is used by two different parts of a schema. See: 
     * line 2394 of http://ultra-api.ultradns.com/UltraDNS_WS/v01?wsdl
     * line 2393 of http://ultra-api.ultradns.com/UltraDNS_WS/v01?wsdl
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link NameServerRecordSet }{@code >}
     * 
     * 
     */
    public List<JAXBElement<NameServerRecordSet>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<NameServerRecordSet>>();
        }
        return this.content;
    }

}

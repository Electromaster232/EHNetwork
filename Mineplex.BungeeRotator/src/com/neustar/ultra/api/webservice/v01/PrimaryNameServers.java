
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for primaryNameServers complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="primaryNameServers">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nameServerIpList" type="{http://webservice.api.ultra.neustar.com/v01/}nameServerIpList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "primaryNameServers", propOrder = {
    "nameServerIpList"
})
public class PrimaryNameServers {

    protected NameServerIpList nameServerIpList;

    /**
     * Gets the value of the nameServerIpList property.
     * 
     * @return
     *     possible object is
     *     {@link NameServerIpList }
     *     
     */
    public NameServerIpList getNameServerIpList() {
        return nameServerIpList;
    }

    /**
     * Sets the value of the nameServerIpList property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameServerIpList }
     *     
     */
    public void setNameServerIpList(NameServerIpList value) {
        this.nameServerIpList = value;
    }

}

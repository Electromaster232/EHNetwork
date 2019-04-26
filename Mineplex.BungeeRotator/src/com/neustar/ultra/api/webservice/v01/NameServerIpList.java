
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nameServerIpList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nameServerIpList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nameServerIp1" type="{http://webservice.api.ultra.neustar.com/v01/}nameServer" minOccurs="0"/>
 *         &lt;element name="nameServerIp2" type="{http://webservice.api.ultra.neustar.com/v01/}nameServer" minOccurs="0"/>
 *         &lt;element name="nameServerIp3" type="{http://webservice.api.ultra.neustar.com/v01/}nameServer" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nameServerIpList", propOrder = {
    "nameServerIp1",
    "nameServerIp2",
    "nameServerIp3"
})
public class NameServerIpList {

    protected NameServer nameServerIp1;
    protected NameServer nameServerIp2;
    protected NameServer nameServerIp3;

    /**
     * Gets the value of the nameServerIp1 property.
     * 
     * @return
     *     possible object is
     *     {@link NameServer }
     *     
     */
    public NameServer getNameServerIp1() {
        return nameServerIp1;
    }

    /**
     * Sets the value of the nameServerIp1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameServer }
     *     
     */
    public void setNameServerIp1(NameServer value) {
        this.nameServerIp1 = value;
    }

    /**
     * Gets the value of the nameServerIp2 property.
     * 
     * @return
     *     possible object is
     *     {@link NameServer }
     *     
     */
    public NameServer getNameServerIp2() {
        return nameServerIp2;
    }

    /**
     * Sets the value of the nameServerIp2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameServer }
     *     
     */
    public void setNameServerIp2(NameServer value) {
        this.nameServerIp2 = value;
    }

    /**
     * Gets the value of the nameServerIp3 property.
     * 
     * @return
     *     possible object is
     *     {@link NameServer }
     *     
     */
    public NameServer getNameServerIp3() {
        return nameServerIp3;
    }

    /**
     * Sets the value of the nameServerIp3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameServer }
     *     
     */
    public void setNameServerIp3(NameServer value) {
        this.nameServerIp3 = value;
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GlobalDirectionalGroupUpdateDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GlobalDirectionalGroupUpdateDetails">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schema.ultraservice.neustar.com/v01/}DirectionalDNSGroupDetail">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="GroupId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GlobalDirectionalGroupUpdateDetails")
public class GlobalDirectionalGroupUpdateDetails
    extends DirectionalDNSGroupDetail
{

    @XmlAttribute(name = "GroupId", required = true)
    protected String groupId;

    /**
     * Gets the value of the groupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Sets the value of the groupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupId(String value) {
        this.groupId = value;
    }

}

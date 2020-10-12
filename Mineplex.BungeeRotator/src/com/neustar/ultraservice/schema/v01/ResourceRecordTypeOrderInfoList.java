
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceRecordTypeOrderInfoList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceRecordTypeOrderInfoList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResourceRecordTypeOrderInfo" type="{http://schema.ultraservice.neustar.com/v01/}ResourceRecordTypeOrderInfo" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceRecordTypeOrderInfoList", propOrder = {
    "resourceRecordTypeOrderInfo"
})
public class ResourceRecordTypeOrderInfoList {

    @XmlElement(name = "ResourceRecordTypeOrderInfo", required = true)
    protected List<ResourceRecordTypeOrderInfo> resourceRecordTypeOrderInfo;

    /**
     * Gets the value of the resourceRecordTypeOrderInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceRecordTypeOrderInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceRecordTypeOrderInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceRecordTypeOrderInfo }
     * 
     * 
     */
    public List<ResourceRecordTypeOrderInfo> getResourceRecordTypeOrderInfo() {
        if (resourceRecordTypeOrderInfo == null) {
            resourceRecordTypeOrderInfo = new ArrayList<ResourceRecordTypeOrderInfo>();
        }
        return this.resourceRecordTypeOrderInfo;
    }

}

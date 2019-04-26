
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomHTTPHeaderDataList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomHTTPHeaderDataList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CustomHTTPHeaderData" type="{http://schema.ultraservice.neustar.com/v01/}CustomHTTPHeaderDataGUID" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomHTTPHeaderDataList", propOrder = {
    "customHTTPHeaderData"
})
public class CustomHTTPHeaderDataList {

    @XmlElement(name = "CustomHTTPHeaderData", required = true)
    protected List<CustomHTTPHeaderDataGUID> customHTTPHeaderData;

    /**
     * Gets the value of the customHTTPHeaderData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customHTTPHeaderData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomHTTPHeaderData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomHTTPHeaderDataGUID }
     * 
     * 
     */
    public List<CustomHTTPHeaderDataGUID> getCustomHTTPHeaderData() {
        if (customHTTPHeaderData == null) {
            customHTTPHeaderData = new ArrayList<CustomHTTPHeaderDataGUID>();
        }
        return this.customHTTPHeaderData;
    }

}

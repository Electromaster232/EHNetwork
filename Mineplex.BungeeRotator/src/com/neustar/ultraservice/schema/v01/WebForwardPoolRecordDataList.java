
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WebForwardPoolRecordDataList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WebForwardPoolRecordDataList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WebForwardPoolRecordData" type="{http://schema.ultraservice.neustar.com/v01/}WebForwardPoolRecordDataGuid" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebForwardPoolRecordDataList", propOrder = {
    "webForwardPoolRecordData"
})
public class WebForwardPoolRecordDataList {

    @XmlElement(name = "WebForwardPoolRecordData")
    protected List<WebForwardPoolRecordDataGuid> webForwardPoolRecordData;

    /**
     * Gets the value of the webForwardPoolRecordData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the webForwardPoolRecordData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWebForwardPoolRecordData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WebForwardPoolRecordDataGuid }
     * 
     * 
     */
    public List<WebForwardPoolRecordDataGuid> getWebForwardPoolRecordData() {
        if (webForwardPoolRecordData == null) {
            webForwardPoolRecordData = new ArrayList<WebForwardPoolRecordDataGuid>();
        }
        return this.webForwardPoolRecordData;
    }

}

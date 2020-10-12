
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WebFwdRecordsList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WebFwdRecordsList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Web_Forward_Record" type="{http://schema.ultraservice.neustar.com/v01/}Web_Forward_Record" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebFwdRecordsList", propOrder = {
    "webForwardRecord"
})
public class WebFwdRecordsList {

    @XmlElement(name = "Web_Forward_Record", required = true)
    protected List<WebForwardRecord> webForwardRecord;

    /**
     * Gets the value of the webForwardRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the webForwardRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWebForwardRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WebForwardRecord }
     * 
     * 
     */
    public List<WebForwardRecord> getWebForwardRecord() {
        if (webForwardRecord == null) {
            webForwardRecord = new ArrayList<WebForwardRecord>();
        }
        return this.webForwardRecord;
    }

}


package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DnssecKeyRecordList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DnssecKeyRecordList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DnssecKey" type="{http://schema.ultraservice.neustar.com/v01/}DnssecKey" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DnssecKeyRecordList", propOrder = {
    "dnssecKey"
})
public class DnssecKeyRecordList {

    @XmlElement(name = "DnssecKey", required = true)
    protected List<DnssecKey> dnssecKey;

    /**
     * Gets the value of the dnssecKey property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dnssecKey property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDnssecKey().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DnssecKey }
     * 
     * 
     */
    public List<DnssecKey> getDnssecKey() {
        if (dnssecKey == null) {
            dnssecKey = new ArrayList<DnssecKey>();
        }
        return this.dnssecKey;
    }

}

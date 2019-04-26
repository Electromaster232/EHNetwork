
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LBPoolList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LBPoolList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LBPoolData" type="{http://schema.ultraservice.neustar.com/v01/}LBPoolData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LBPoolList", propOrder = {
    "lbPoolData"
})
public class LBPoolList {

    @XmlElement(name = "LBPoolData", required = true)
    protected List<LBPoolData> lbPoolData;

    /**
     * Gets the value of the lbPoolData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lbPoolData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLBPoolData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LBPoolData }
     * 
     * 
     */
    public List<LBPoolData> getLBPoolData() {
        if (lbPoolData == null) {
            lbPoolData = new ArrayList<LBPoolData>();
        }
        return this.lbPoolData;
    }

}

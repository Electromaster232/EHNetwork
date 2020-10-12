
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolRecordsList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolRecordsList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PoolRecordData" type="{http://schema.ultraservice.neustar.com/v01/}PoolRecordData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolRecordsList", propOrder = {
    "poolRecordData"
})
public class PoolRecordsList {

    @XmlElement(name = "PoolRecordData", required = true)
    protected List<PoolRecordData> poolRecordData;

    /**
     * Gets the value of the poolRecordData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the poolRecordData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPoolRecordData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PoolRecordData }
     * 
     * 
     */
    public List<PoolRecordData> getPoolRecordData() {
        if (poolRecordData == null) {
            poolRecordData = new ArrayList<PoolRecordData>();
        }
        return this.poolRecordData;
    }

}

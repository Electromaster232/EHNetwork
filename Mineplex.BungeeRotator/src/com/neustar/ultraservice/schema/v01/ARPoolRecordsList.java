
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARPoolRecordsList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ARPoolRecordsList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arPoolRecords" type="{http://schema.ultraservice.neustar.com/v01/}ARPoolRecords" minOccurs="0"/>
 *         &lt;element name="total" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="offset" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="count" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ARPoolRecordsList", propOrder = {
    "arPoolRecords",
    "total",
    "offset",
    "count"
})
public class ARPoolRecordsList {

    protected ARPoolRecords arPoolRecords;
    protected int total;
    protected int offset;
    protected int count;

    /**
     * Gets the value of the arPoolRecords property.
     * 
     * @return
     *     possible object is
     *     {@link ARPoolRecords }
     *     
     */
    public ARPoolRecords getArPoolRecords() {
        return arPoolRecords;
    }

    /**
     * Sets the value of the arPoolRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link ARPoolRecords }
     *     
     */
    public void setArPoolRecords(ARPoolRecords value) {
        this.arPoolRecords = value;
    }

    /**
     * Gets the value of the total property.
     * 
     */
    public int getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     */
    public void setTotal(int value) {
        this.total = value;
    }

    /**
     * Gets the value of the offset property.
     * 
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     * 
     */
    public void setOffset(int value) {
        this.offset = value;
    }

    /**
     * Gets the value of the count property.
     * 
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     */
    public void setCount(int value) {
        this.count = value;
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for monitoredRDPools complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="monitoredRDPools">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="poolDefinitions" type="{http://schema.ultraservice.neustar.com/v01/}PoolDefinitions"/>
 *         &lt;element name="recordCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="startIndex" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "monitoredRDPools", propOrder = {
    "poolDefinitions",
    "recordCount",
    "startIndex",
    "count"
})
public class MonitoredRDPools {

    @XmlElement(required = true)
    protected PoolDefinitions poolDefinitions;
    protected int recordCount;
    protected int startIndex;
    protected int count;

    /**
     * Gets the value of the poolDefinitions property.
     * 
     * @return
     *     possible object is
     *     {@link PoolDefinitions }
     *     
     */
    public PoolDefinitions getPoolDefinitions() {
        return poolDefinitions;
    }

    /**
     * Sets the value of the poolDefinitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link PoolDefinitions }
     *     
     */
    public void setPoolDefinitions(PoolDefinitions value) {
        this.poolDefinitions = value;
    }

    /**
     * Gets the value of the recordCount property.
     * 
     */
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * Sets the value of the recordCount property.
     * 
     */
    public void setRecordCount(int value) {
        this.recordCount = value;
    }

    /**
     * Gets the value of the startIndex property.
     * 
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Sets the value of the startIndex property.
     * 
     */
    public void setStartIndex(int value) {
        this.startIndex = value;
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

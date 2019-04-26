
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolRecordId complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolRecordId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="poolName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="poolRecordType" type="{http://schema.ultraservice.neustar.com/v01/}RecordType"/>
 *         &lt;element name="poolRecordValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolRecordId", propOrder = {
    "poolName",
    "poolRecordType",
    "poolRecordValue"
})
public class PoolRecordId {

    @XmlElement(required = true)
    protected String poolName;
    @XmlElement(required = true)
    protected RecordType poolRecordType;
    @XmlElement(required = true)
    protected String poolRecordValue;

    /**
     * Gets the value of the poolName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * Sets the value of the poolName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolName(String value) {
        this.poolName = value;
    }

    /**
     * Gets the value of the poolRecordType property.
     * 
     * @return
     *     possible object is
     *     {@link RecordType }
     *     
     */
    public RecordType getPoolRecordType() {
        return poolRecordType;
    }

    /**
     * Sets the value of the poolRecordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordType }
     *     
     */
    public void setPoolRecordType(RecordType value) {
        this.poolRecordType = value;
    }

    /**
     * Gets the value of the poolRecordValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolRecordValue() {
        return poolRecordValue;
    }

    /**
     * Sets the value of the poolRecordValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolRecordValue(String value) {
        this.poolRecordValue = value;
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolRecordProbeId complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolRecordProbeId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="poolName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="poolRecordValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="probeDefinitionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolRecordProbeId", propOrder = {
    "poolName",
    "poolRecordValue",
    "probeDefinitionName"
})
public class PoolRecordProbeId {

    @XmlElement(required = true)
    protected String poolName;
    @XmlElement(required = true)
    protected String poolRecordValue;
    @XmlElement(required = true)
    protected String probeDefinitionName;

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

    /**
     * Gets the value of the probeDefinitionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeDefinitionName() {
        return probeDefinitionName;
    }

    /**
     * Sets the value of the probeDefinitionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeDefinitionName(String value) {
        this.probeDefinitionName = value;
    }

}

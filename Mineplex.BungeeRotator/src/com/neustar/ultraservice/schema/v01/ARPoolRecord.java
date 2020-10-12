
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARPoolRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ARPoolRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="poolName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="poolRecordType" type="{http://schema.ultraservice.neustar.com/v01/}RecordType"/>
 *         &lt;element name="poolRecordValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="forcedState" type="{http://schema.ultraservice.neustar.com/v01/}ForcedState"/>
 *         &lt;element name="probesEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="allFail" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ARPoolRecord", propOrder = {
    "poolName",
    "description",
    "poolRecordType",
    "poolRecordValue",
    "forcedState",
    "probesEnabled",
    "weight",
    "allFail"
})
public class ARPoolRecord {

    @XmlElement(required = true)
    protected String poolName;
    protected String description;
    @XmlElement(required = true)
    protected RecordType poolRecordType;
    @XmlElement(required = true)
    protected String poolRecordValue;
    @XmlElement(required = true)
    protected ForcedState forcedState;
    protected boolean probesEnabled;
    protected Integer weight;
    protected Boolean allFail;

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
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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

    /**
     * Gets the value of the forcedState property.
     * 
     * @return
     *     possible object is
     *     {@link ForcedState }
     *     
     */
    public ForcedState getForcedState() {
        return forcedState;
    }

    /**
     * Sets the value of the forcedState property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForcedState }
     *     
     */
    public void setForcedState(ForcedState value) {
        this.forcedState = value;
    }

    /**
     * Gets the value of the probesEnabled property.
     * 
     */
    public boolean isProbesEnabled() {
        return probesEnabled;
    }

    /**
     * Sets the value of the probesEnabled property.
     * 
     */
    public void setProbesEnabled(boolean value) {
        this.probesEnabled = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWeight(Integer value) {
        this.weight = value;
    }

    /**
     * Gets the value of the allFail property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAllFail() {
        return allFail;
    }

    /**
     * Sets the value of the allFail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAllFail(Boolean value) {
        this.allFail = value;
    }

}

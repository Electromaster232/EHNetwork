
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Record" type="{http://schema.ultraservice.neustar.com/v01/}Record"/>
 *       &lt;/sequence>
 *       &lt;attribute name="hostName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="configurationName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="weight" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="allFail" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="forcedState" use="required" type="{http://schema.ultraservice.neustar.com/v01/}ForcedState" />
 *       &lt;attribute name="recordState" type="{http://schema.ultraservice.neustar.com/v01/}RecordState" />
 *       &lt;attribute name="probesEnabled" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="recordStatus" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolRecord", propOrder = {
    "record"
})
public class PoolRecord {

    @XmlElement(name = "Record", required = true)
    protected Record record;
    @XmlAttribute(name = "hostName", required = true)
    protected String hostName;
    @XmlAttribute(name = "configurationName")
    protected String configurationName;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "weight")
    protected Integer weight;
    @XmlAttribute(name = "allFail")
    protected Boolean allFail;
    @XmlAttribute(name = "forcedState", required = true)
    protected ForcedState forcedState;
    @XmlAttribute(name = "recordState")
    protected RecordState recordState;
    @XmlAttribute(name = "probesEnabled", required = true)
    protected boolean probesEnabled;
    @XmlAttribute(name = "recordStatus")
    protected String recordStatus;

    /**
     * Gets the value of the record property.
     * 
     * @return
     *     possible object is
     *     {@link Record }
     *     
     */
    public Record getRecord() {
        return record;
    }

    /**
     * Sets the value of the record property.
     * 
     * @param value
     *     allowed object is
     *     {@link Record }
     *     
     */
    public void setRecord(Record value) {
        this.record = value;
    }

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the configurationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigurationName() {
        return configurationName;
    }

    /**
     * Sets the value of the configurationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigurationName(String value) {
        this.configurationName = value;
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
     * Gets the value of the recordState property.
     * 
     * @return
     *     possible object is
     *     {@link RecordState }
     *     
     */
    public RecordState getRecordState() {
        return recordState;
    }

    /**
     * Sets the value of the recordState property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordState }
     *     
     */
    public void setRecordState(RecordState value) {
        this.recordState = value;
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
     * Gets the value of the recordStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordStatus() {
        return recordStatus;
    }

    /**
     * Sets the value of the recordStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordStatus(String value) {
        this.recordStatus = value;
    }

}

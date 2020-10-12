
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolAlertDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolAlertDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="alertTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pool" type="{http://schema.ultraservice.neustar.com/v01/}AlertPoolDetails"/>
 *         &lt;element name="poolRecord" type="{http://schema.ultraservice.neustar.com/v01/}AlertRecord"/>
 *         &lt;element name="probeDetails" type="{http://schema.ultraservice.neustar.com/v01/}AlertProbeDetails"/>
 *         &lt;element name="ruleTransitionDetails" type="{http://schema.ultraservice.neustar.com/v01/}ARAlertRuleDetails"/>
 *         &lt;element name="stateTransitionDetails" type="{http://schema.ultraservice.neustar.com/v01/}ARAlertStateDetails"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolAlertDetails", propOrder = {
    "alertTime",
    "pool",
    "poolRecord",
    "probeDetails",
    "ruleTransitionDetails",
    "stateTransitionDetails"
})
public class PoolAlertDetails {

    @XmlElement(required = true)
    protected String alertTime;
    @XmlElement(required = true)
    protected AlertPoolDetails pool;
    @XmlElement(required = true)
    protected AlertRecord poolRecord;
    @XmlElement(required = true)
    protected AlertProbeDetails probeDetails;
    @XmlElement(required = true)
    protected ARAlertRuleDetails ruleTransitionDetails;
    @XmlElement(required = true)
    protected ARAlertStateDetails stateTransitionDetails;

    /**
     * Gets the value of the alertTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlertTime() {
        return alertTime;
    }

    /**
     * Sets the value of the alertTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlertTime(String value) {
        this.alertTime = value;
    }

    /**
     * Gets the value of the pool property.
     * 
     * @return
     *     possible object is
     *     {@link AlertPoolDetails }
     *     
     */
    public AlertPoolDetails getPool() {
        return pool;
    }

    /**
     * Sets the value of the pool property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlertPoolDetails }
     *     
     */
    public void setPool(AlertPoolDetails value) {
        this.pool = value;
    }

    /**
     * Gets the value of the poolRecord property.
     * 
     * @return
     *     possible object is
     *     {@link AlertRecord }
     *     
     */
    public AlertRecord getPoolRecord() {
        return poolRecord;
    }

    /**
     * Sets the value of the poolRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlertRecord }
     *     
     */
    public void setPoolRecord(AlertRecord value) {
        this.poolRecord = value;
    }

    /**
     * Gets the value of the probeDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AlertProbeDetails }
     *     
     */
    public AlertProbeDetails getProbeDetails() {
        return probeDetails;
    }

    /**
     * Sets the value of the probeDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlertProbeDetails }
     *     
     */
    public void setProbeDetails(AlertProbeDetails value) {
        this.probeDetails = value;
    }

    /**
     * Gets the value of the ruleTransitionDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ARAlertRuleDetails }
     *     
     */
    public ARAlertRuleDetails getRuleTransitionDetails() {
        return ruleTransitionDetails;
    }

    /**
     * Sets the value of the ruleTransitionDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ARAlertRuleDetails }
     *     
     */
    public void setRuleTransitionDetails(ARAlertRuleDetails value) {
        this.ruleTransitionDetails = value;
    }

    /**
     * Gets the value of the stateTransitionDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ARAlertStateDetails }
     *     
     */
    public ARAlertStateDetails getStateTransitionDetails() {
        return stateTransitionDetails;
    }

    /**
     * Sets the value of the stateTransitionDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ARAlertStateDetails }
     *     
     */
    public void setStateTransitionDetails(ARAlertStateDetails value) {
        this.stateTransitionDetails = value;
    }

}

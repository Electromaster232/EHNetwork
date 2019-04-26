
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Rule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Rule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ruleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="type" type="{http://schema.ultraservice.neustar.com/v01/}RuleType" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://schema.ultraservice.neustar.com/v01/}StatusEnum" minOccurs="0"/>
 *         &lt;element name="attachNewProbes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="occurrences" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="timespan" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="changeState" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="stateDelay" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="weightPercentage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="maxActiveAdjustment" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="maxResponseAdjustment" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="notifications" type="{http://schema.ultraservice.neustar.com/v01/}Notifications" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rule", propOrder = {
    "ruleName",
    "priority",
    "type",
    "description",
    "status",
    "attachNewProbes",
    "occurrences",
    "timespan",
    "changeState",
    "stateDelay",
    "weightPercentage",
    "maxActiveAdjustment",
    "maxResponseAdjustment",
    "notifications"
})
public class Rule {

    @XmlElement(required = true)
    protected String ruleName;
    protected int priority;
    protected RuleType type;
    protected String description;
    protected StatusEnum status;
    protected boolean attachNewProbes;
    protected int occurrences;
    protected int timespan;
    protected boolean changeState;
    protected int stateDelay;
    protected int weightPercentage;
    protected int maxActiveAdjustment;
    protected int maxResponseAdjustment;
    protected Notifications notifications;

    /**
     * Gets the value of the ruleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Sets the value of the ruleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleName(String value) {
        this.ruleName = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     */
    public void setPriority(int value) {
        this.priority = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link RuleType }
     *     
     */
    public RuleType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link RuleType }
     *     
     */
    public void setType(RuleType value) {
        this.type = value;
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
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link StatusEnum }
     *     
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusEnum }
     *     
     */
    public void setStatus(StatusEnum value) {
        this.status = value;
    }

    /**
     * Gets the value of the attachNewProbes property.
     * 
     */
    public boolean isAttachNewProbes() {
        return attachNewProbes;
    }

    /**
     * Sets the value of the attachNewProbes property.
     * 
     */
    public void setAttachNewProbes(boolean value) {
        this.attachNewProbes = value;
    }

    /**
     * Gets the value of the occurrences property.
     * 
     */
    public int getOccurrences() {
        return occurrences;
    }

    /**
     * Sets the value of the occurrences property.
     * 
     */
    public void setOccurrences(int value) {
        this.occurrences = value;
    }

    /**
     * Gets the value of the timespan property.
     * 
     */
    public int getTimespan() {
        return timespan;
    }

    /**
     * Sets the value of the timespan property.
     * 
     */
    public void setTimespan(int value) {
        this.timespan = value;
    }

    /**
     * Gets the value of the changeState property.
     * 
     */
    public boolean isChangeState() {
        return changeState;
    }

    /**
     * Sets the value of the changeState property.
     * 
     */
    public void setChangeState(boolean value) {
        this.changeState = value;
    }

    /**
     * Gets the value of the stateDelay property.
     * 
     */
    public int getStateDelay() {
        return stateDelay;
    }

    /**
     * Sets the value of the stateDelay property.
     * 
     */
    public void setStateDelay(int value) {
        this.stateDelay = value;
    }

    /**
     * Gets the value of the weightPercentage property.
     * 
     */
    public int getWeightPercentage() {
        return weightPercentage;
    }

    /**
     * Sets the value of the weightPercentage property.
     * 
     */
    public void setWeightPercentage(int value) {
        this.weightPercentage = value;
    }

    /**
     * Gets the value of the maxActiveAdjustment property.
     * 
     */
    public int getMaxActiveAdjustment() {
        return maxActiveAdjustment;
    }

    /**
     * Sets the value of the maxActiveAdjustment property.
     * 
     */
    public void setMaxActiveAdjustment(int value) {
        this.maxActiveAdjustment = value;
    }

    /**
     * Gets the value of the maxResponseAdjustment property.
     * 
     */
    public int getMaxResponseAdjustment() {
        return maxResponseAdjustment;
    }

    /**
     * Sets the value of the maxResponseAdjustment property.
     * 
     */
    public void setMaxResponseAdjustment(int value) {
        this.maxResponseAdjustment = value;
    }

    /**
     * Gets the value of the notifications property.
     * 
     * @return
     *     possible object is
     *     {@link Notifications }
     *     
     */
    public Notifications getNotifications() {
        return notifications;
    }

    /**
     * Sets the value of the notifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link Notifications }
     *     
     */
    public void setNotifications(Notifications value) {
        this.notifications = value;
    }

}

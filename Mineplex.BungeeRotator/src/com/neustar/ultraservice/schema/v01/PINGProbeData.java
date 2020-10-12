
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PINGProbeData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PINGProbeData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="numberOfPackets" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sizeOfPackets" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="percentageOfLostPackets" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="roundTripTimeInMs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="runtime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failPercentageOfLostPackets" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalPercentageOfLostPackets" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningPercentageOfLostPackets" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failRoundTripTimeInMs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalRoundTripTimeInMs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningRoundTripTimeInMs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failAverageRTTInMs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalAverageRTTInMs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningAverageRTTInMs" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="failAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticalAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="warningAverageRunTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PINGProbeData")
public class PINGProbeData {

    @XmlAttribute(name = "numberOfPackets")
    protected String numberOfPackets;
    @XmlAttribute(name = "sizeOfPackets")
    protected String sizeOfPackets;
    @XmlAttribute(name = "percentageOfLostPackets")
    protected String percentageOfLostPackets;
    @XmlAttribute(name = "roundTripTimeInMs")
    protected String roundTripTimeInMs;
    @XmlAttribute(name = "runtime")
    protected String runtime;
    @XmlAttribute(name = "failPercentageOfLostPackets")
    protected String failPercentageOfLostPackets;
    @XmlAttribute(name = "criticalPercentageOfLostPackets")
    protected String criticalPercentageOfLostPackets;
    @XmlAttribute(name = "warningPercentageOfLostPackets")
    protected String warningPercentageOfLostPackets;
    @XmlAttribute(name = "failRoundTripTimeInMs")
    protected String failRoundTripTimeInMs;
    @XmlAttribute(name = "criticalRoundTripTimeInMs")
    protected String criticalRoundTripTimeInMs;
    @XmlAttribute(name = "warningRoundTripTimeInMs")
    protected String warningRoundTripTimeInMs;
    @XmlAttribute(name = "failRuntime")
    protected String failRuntime;
    @XmlAttribute(name = "criticalRuntime")
    protected String criticalRuntime;
    @XmlAttribute(name = "warningRuntime")
    protected String warningRuntime;
    @XmlAttribute(name = "failAverageRTTInMs")
    protected String failAverageRTTInMs;
    @XmlAttribute(name = "criticalAverageRTTInMs")
    protected String criticalAverageRTTInMs;
    @XmlAttribute(name = "warningAverageRTTInMs")
    protected String warningAverageRTTInMs;
    @XmlAttribute(name = "failAverageRunTime")
    protected String failAverageRunTime;
    @XmlAttribute(name = "criticalAverageRunTime")
    protected String criticalAverageRunTime;
    @XmlAttribute(name = "warningAverageRunTime")
    protected String warningAverageRunTime;

    /**
     * Gets the value of the numberOfPackets property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfPackets() {
        return numberOfPackets;
    }

    /**
     * Sets the value of the numberOfPackets property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfPackets(String value) {
        this.numberOfPackets = value;
    }

    /**
     * Gets the value of the sizeOfPackets property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSizeOfPackets() {
        return sizeOfPackets;
    }

    /**
     * Sets the value of the sizeOfPackets property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSizeOfPackets(String value) {
        this.sizeOfPackets = value;
    }

    /**
     * Gets the value of the percentageOfLostPackets property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPercentageOfLostPackets() {
        return percentageOfLostPackets;
    }

    /**
     * Sets the value of the percentageOfLostPackets property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPercentageOfLostPackets(String value) {
        this.percentageOfLostPackets = value;
    }

    /**
     * Gets the value of the roundTripTimeInMs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoundTripTimeInMs() {
        return roundTripTimeInMs;
    }

    /**
     * Sets the value of the roundTripTimeInMs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoundTripTimeInMs(String value) {
        this.roundTripTimeInMs = value;
    }

    /**
     * Gets the value of the runtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     * Sets the value of the runtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuntime(String value) {
        this.runtime = value;
    }

    /**
     * Gets the value of the failPercentageOfLostPackets property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailPercentageOfLostPackets() {
        return failPercentageOfLostPackets;
    }

    /**
     * Sets the value of the failPercentageOfLostPackets property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailPercentageOfLostPackets(String value) {
        this.failPercentageOfLostPackets = value;
    }

    /**
     * Gets the value of the criticalPercentageOfLostPackets property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalPercentageOfLostPackets() {
        return criticalPercentageOfLostPackets;
    }

    /**
     * Sets the value of the criticalPercentageOfLostPackets property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalPercentageOfLostPackets(String value) {
        this.criticalPercentageOfLostPackets = value;
    }

    /**
     * Gets the value of the warningPercentageOfLostPackets property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningPercentageOfLostPackets() {
        return warningPercentageOfLostPackets;
    }

    /**
     * Sets the value of the warningPercentageOfLostPackets property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningPercentageOfLostPackets(String value) {
        this.warningPercentageOfLostPackets = value;
    }

    /**
     * Gets the value of the failRoundTripTimeInMs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailRoundTripTimeInMs() {
        return failRoundTripTimeInMs;
    }

    /**
     * Sets the value of the failRoundTripTimeInMs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailRoundTripTimeInMs(String value) {
        this.failRoundTripTimeInMs = value;
    }

    /**
     * Gets the value of the criticalRoundTripTimeInMs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalRoundTripTimeInMs() {
        return criticalRoundTripTimeInMs;
    }

    /**
     * Sets the value of the criticalRoundTripTimeInMs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalRoundTripTimeInMs(String value) {
        this.criticalRoundTripTimeInMs = value;
    }

    /**
     * Gets the value of the warningRoundTripTimeInMs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningRoundTripTimeInMs() {
        return warningRoundTripTimeInMs;
    }

    /**
     * Sets the value of the warningRoundTripTimeInMs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningRoundTripTimeInMs(String value) {
        this.warningRoundTripTimeInMs = value;
    }

    /**
     * Gets the value of the failRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailRuntime() {
        return failRuntime;
    }

    /**
     * Sets the value of the failRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailRuntime(String value) {
        this.failRuntime = value;
    }

    /**
     * Gets the value of the criticalRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalRuntime() {
        return criticalRuntime;
    }

    /**
     * Sets the value of the criticalRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalRuntime(String value) {
        this.criticalRuntime = value;
    }

    /**
     * Gets the value of the warningRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningRuntime() {
        return warningRuntime;
    }

    /**
     * Sets the value of the warningRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningRuntime(String value) {
        this.warningRuntime = value;
    }

    /**
     * Gets the value of the failAverageRTTInMs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailAverageRTTInMs() {
        return failAverageRTTInMs;
    }

    /**
     * Sets the value of the failAverageRTTInMs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailAverageRTTInMs(String value) {
        this.failAverageRTTInMs = value;
    }

    /**
     * Gets the value of the criticalAverageRTTInMs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalAverageRTTInMs() {
        return criticalAverageRTTInMs;
    }

    /**
     * Sets the value of the criticalAverageRTTInMs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalAverageRTTInMs(String value) {
        this.criticalAverageRTTInMs = value;
    }

    /**
     * Gets the value of the warningAverageRTTInMs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningAverageRTTInMs() {
        return warningAverageRTTInMs;
    }

    /**
     * Sets the value of the warningAverageRTTInMs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningAverageRTTInMs(String value) {
        this.warningAverageRTTInMs = value;
    }

    /**
     * Gets the value of the failAverageRunTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailAverageRunTime() {
        return failAverageRunTime;
    }

    /**
     * Sets the value of the failAverageRunTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailAverageRunTime(String value) {
        this.failAverageRunTime = value;
    }

    /**
     * Gets the value of the criticalAverageRunTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalAverageRunTime() {
        return criticalAverageRunTime;
    }

    /**
     * Sets the value of the criticalAverageRunTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalAverageRunTime(String value) {
        this.criticalAverageRunTime = value;
    }

    /**
     * Gets the value of the warningAverageRunTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarningAverageRunTime() {
        return warningAverageRunTime;
    }

    /**
     * Sets the value of the warningAverageRunTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarningAverageRunTime(String value) {
        this.warningAverageRunTime = value;
    }

}

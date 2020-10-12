
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProbeData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProbeData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="PoolProbeId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PoolId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SbprecordId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Active" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ProbeDataType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ProbeID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ProbeName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Bleid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ProbeWeight" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="probedata" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="probefailspecs" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="agentfailspecs" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="threshold" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="interval" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProbeData")
public class ProbeData {

    @XmlAttribute(name = "PoolProbeId", required = true)
    protected String poolProbeId;
    @XmlAttribute(name = "PoolId", required = true)
    protected String poolId;
    @XmlAttribute(name = "SbprecordId", required = true)
    protected String sbprecordId;
    @XmlAttribute(name = "Active", required = true)
    protected String active;
    @XmlAttribute(name = "ProbeDataType", required = true)
    protected String probeDataType;
    @XmlAttribute(name = "ProbeID", required = true)
    protected String probeID;
    @XmlAttribute(name = "ProbeName", required = true)
    protected String probeName;
    @XmlAttribute(name = "Bleid", required = true)
    protected String bleid;
    @XmlAttribute(name = "ProbeWeight", required = true)
    protected String probeWeight;
    @XmlAttribute(name = "probedata", required = true)
    protected String probedata;
    @XmlAttribute(name = "probefailspecs", required = true)
    protected String probefailspecs;
    @XmlAttribute(name = "agentfailspecs", required = true)
    protected String agentfailspecs;
    @XmlAttribute(name = "threshold", required = true)
    protected String threshold;
    @XmlAttribute(name = "interval", required = true)
    protected String interval;

    /**
     * Gets the value of the poolProbeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolProbeId() {
        return poolProbeId;
    }

    /**
     * Sets the value of the poolProbeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolProbeId(String value) {
        this.poolProbeId = value;
    }

    /**
     * Gets the value of the poolId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolId() {
        return poolId;
    }

    /**
     * Sets the value of the poolId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolId(String value) {
        this.poolId = value;
    }

    /**
     * Gets the value of the sbprecordId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSbprecordId() {
        return sbprecordId;
    }

    /**
     * Sets the value of the sbprecordId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSbprecordId(String value) {
        this.sbprecordId = value;
    }

    /**
     * Gets the value of the active property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActive(String value) {
        this.active = value;
    }

    /**
     * Gets the value of the probeDataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeDataType() {
        return probeDataType;
    }

    /**
     * Sets the value of the probeDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeDataType(String value) {
        this.probeDataType = value;
    }

    /**
     * Gets the value of the probeID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeID() {
        return probeID;
    }

    /**
     * Sets the value of the probeID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeID(String value) {
        this.probeID = value;
    }

    /**
     * Gets the value of the probeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeName() {
        return probeName;
    }

    /**
     * Sets the value of the probeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeName(String value) {
        this.probeName = value;
    }

    /**
     * Gets the value of the bleid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBleid() {
        return bleid;
    }

    /**
     * Sets the value of the bleid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBleid(String value) {
        this.bleid = value;
    }

    /**
     * Gets the value of the probeWeight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeWeight() {
        return probeWeight;
    }

    /**
     * Sets the value of the probeWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeWeight(String value) {
        this.probeWeight = value;
    }

    /**
     * Gets the value of the probedata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbedata() {
        return probedata;
    }

    /**
     * Sets the value of the probedata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbedata(String value) {
        this.probedata = value;
    }

    /**
     * Gets the value of the probefailspecs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbefailspecs() {
        return probefailspecs;
    }

    /**
     * Sets the value of the probefailspecs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbefailspecs(String value) {
        this.probefailspecs = value;
    }

    /**
     * Gets the value of the agentfailspecs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentfailspecs() {
        return agentfailspecs;
    }

    /**
     * Sets the value of the agentfailspecs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentfailspecs(String value) {
        this.agentfailspecs = value;
    }

    /**
     * Gets the value of the threshold property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThreshold() {
        return threshold;
    }

    /**
     * Sets the value of the threshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThreshold(String value) {
        this.threshold = value;
    }

    /**
     * Gets the value of the interval property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterval() {
        return interval;
    }

    /**
     * Sets the value of the interval property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterval(String value) {
        this.interval = value;
    }

}

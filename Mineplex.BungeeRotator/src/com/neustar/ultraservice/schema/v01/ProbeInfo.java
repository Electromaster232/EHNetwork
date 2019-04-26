
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProbeInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProbeInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProbeData" type="{http://schema.ultraservice.neustar.com/v01/}ProbeData"/>
 *         &lt;element name="SBAgentsList" type="{http://schema.ultraservice.neustar.com/v01/}SBAgentsList"/>
 *         &lt;element name="DNSProbeMaster" type="{http://schema.ultraservice.neustar.com/v01/}DNSProbeMaster"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProbeInfo", propOrder = {
    "probeData",
    "sbAgentsList",
    "dnsProbeMaster"
})
public class ProbeInfo {

    @XmlElement(name = "ProbeData", required = true)
    protected ProbeData probeData;
    @XmlElement(name = "SBAgentsList", required = true)
    protected SBAgentsList sbAgentsList;
    @XmlElement(name = "DNSProbeMaster", required = true)
    protected DNSProbeMaster dnsProbeMaster;

    /**
     * Gets the value of the probeData property.
     * 
     * @return
     *     possible object is
     *     {@link ProbeData }
     *     
     */
    public ProbeData getProbeData() {
        return probeData;
    }

    /**
     * Sets the value of the probeData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProbeData }
     *     
     */
    public void setProbeData(ProbeData value) {
        this.probeData = value;
    }

    /**
     * Gets the value of the sbAgentsList property.
     * 
     * @return
     *     possible object is
     *     {@link SBAgentsList }
     *     
     */
    public SBAgentsList getSBAgentsList() {
        return sbAgentsList;
    }

    /**
     * Sets the value of the sbAgentsList property.
     * 
     * @param value
     *     allowed object is
     *     {@link SBAgentsList }
     *     
     */
    public void setSBAgentsList(SBAgentsList value) {
        this.sbAgentsList = value;
    }

    /**
     * Gets the value of the dnsProbeMaster property.
     * 
     * @return
     *     possible object is
     *     {@link DNSProbeMaster }
     *     
     */
    public DNSProbeMaster getDNSProbeMaster() {
        return dnsProbeMaster;
    }

    /**
     * Sets the value of the dnsProbeMaster property.
     * 
     * @param value
     *     allowed object is
     *     {@link DNSProbeMaster }
     *     
     */
    public void setDNSProbeMaster(DNSProbeMaster value) {
        this.dnsProbeMaster = value;
    }

}

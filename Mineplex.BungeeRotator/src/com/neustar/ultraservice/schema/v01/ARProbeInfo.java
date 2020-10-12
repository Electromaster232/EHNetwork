
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARProbeInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ARProbeInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="probeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="probeType" type="{http://schema.ultraservice.neustar.com/v01/}ProbeTypeEnum" minOccurs="0"/>
 *         &lt;element name="probeLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interval" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="regionThreshold" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="regions" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="recordData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ARProbeInfo", propOrder = {
    "probeName",
    "probeType",
    "probeLevel",
    "interval",
    "regionThreshold",
    "regions",
    "recordData"
})
public class ARProbeInfo {

    protected String probeName;
    protected ProbeTypeEnum probeType;
    protected String probeLevel;
    protected int interval;
    protected int regionThreshold;
    protected List<String> regions;
    protected String recordData;

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
     * Gets the value of the probeType property.
     * 
     * @return
     *     possible object is
     *     {@link ProbeTypeEnum }
     *     
     */
    public ProbeTypeEnum getProbeType() {
        return probeType;
    }

    /**
     * Sets the value of the probeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProbeTypeEnum }
     *     
     */
    public void setProbeType(ProbeTypeEnum value) {
        this.probeType = value;
    }

    /**
     * Gets the value of the probeLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbeLevel() {
        return probeLevel;
    }

    /**
     * Sets the value of the probeLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbeLevel(String value) {
        this.probeLevel = value;
    }

    /**
     * Gets the value of the interval property.
     * 
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Sets the value of the interval property.
     * 
     */
    public void setInterval(int value) {
        this.interval = value;
    }

    /**
     * Gets the value of the regionThreshold property.
     * 
     */
    public int getRegionThreshold() {
        return regionThreshold;
    }

    /**
     * Sets the value of the regionThreshold property.
     * 
     */
    public void setRegionThreshold(int value) {
        this.regionThreshold = value;
    }

    /**
     * Gets the value of the regions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRegions() {
        if (regions == null) {
            regions = new ArrayList<String>();
        }
        return this.regions;
    }

    /**
     * Gets the value of the recordData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordData() {
        return recordData;
    }

    /**
     * Sets the value of the recordData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordData(String value) {
        this.recordData = value;
    }

}

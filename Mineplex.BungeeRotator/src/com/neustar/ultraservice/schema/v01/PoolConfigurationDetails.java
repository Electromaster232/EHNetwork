
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolConfigurationDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolConfigurationDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PoolConfiguration" type="{http://schema.ultraservice.neustar.com/v01/}PoolConfiguration"/>
 *         &lt;element name="PoolRecord" type="{http://schema.ultraservice.neustar.com/v01/}PoolRecord" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Probe" type="{http://schema.ultraservice.neustar.com/v01/}Probe" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="isActive" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="maxActive" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolConfigurationDetails", propOrder = {
    "poolConfiguration",
    "poolRecord",
    "probe"
})
public class PoolConfigurationDetails {

    @XmlElement(name = "PoolConfiguration", required = true)
    protected PoolConfiguration poolConfiguration;
    @XmlElement(name = "PoolRecord", nillable = true)
    protected List<PoolRecord> poolRecord;
    @XmlElement(name = "Probe", nillable = true)
    protected List<Probe> probe;
    @XmlAttribute(name = "isActive", required = true)
    protected boolean isActive;
    @XmlAttribute(name = "maxActive", required = true)
    protected int maxActive;

    /**
     * Gets the value of the poolConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link PoolConfiguration }
     *     
     */
    public PoolConfiguration getPoolConfiguration() {
        return poolConfiguration;
    }

    /**
     * Sets the value of the poolConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link PoolConfiguration }
     *     
     */
    public void setPoolConfiguration(PoolConfiguration value) {
        this.poolConfiguration = value;
    }

    /**
     * Gets the value of the poolRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the poolRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPoolRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PoolRecord }
     * 
     * 
     */
    public List<PoolRecord> getPoolRecord() {
        if (poolRecord == null) {
            poolRecord = new ArrayList<PoolRecord>();
        }
        return this.poolRecord;
    }

    /**
     * Gets the value of the probe property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the probe property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProbe().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Probe }
     * 
     * 
     */
    public List<Probe> getProbe() {
        if (probe == null) {
            probe = new ArrayList<Probe>();
        }
        return this.probe;
    }

    /**
     * Gets the value of the isActive property.
     * 
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * Sets the value of the isActive property.
     * 
     */
    public void setIsActive(boolean value) {
        this.isActive = value;
    }

    /**
     * Gets the value of the maxActive property.
     * 
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * Sets the value of the maxActive property.
     * 
     */
    public void setMaxActive(int value) {
        this.maxActive = value;
    }

}

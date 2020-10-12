
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Pool" type="{http://schema.ultraservice.neustar.com/v01/}Pool"/>
 *         &lt;element name="PoolConfigurationDetails" type="{http://schema.ultraservice.neustar.com/v01/}PoolConfigurationDetails" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolDetails", propOrder = {
    "pool",
    "poolConfigurationDetails"
})
public class PoolDetails {

    @XmlElement(name = "Pool", required = true, nillable = true)
    protected Pool pool;
    @XmlElement(name = "PoolConfigurationDetails", nillable = true)
    protected List<PoolConfigurationDetails> poolConfigurationDetails;

    /**
     * Gets the value of the pool property.
     * 
     * @return
     *     possible object is
     *     {@link Pool }
     *     
     */
    public Pool getPool() {
        return pool;
    }

    /**
     * Sets the value of the pool property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pool }
     *     
     */
    public void setPool(Pool value) {
        this.pool = value;
    }

    /**
     * Gets the value of the poolConfigurationDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the poolConfigurationDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPoolConfigurationDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PoolConfigurationDetails }
     * 
     * 
     */
    public List<PoolConfigurationDetails> getPoolConfigurationDetails() {
        if (poolConfigurationDetails == null) {
            poolConfigurationDetails = new ArrayList<PoolConfigurationDetails>();
        }
        return this.poolConfigurationDetails;
    }

}

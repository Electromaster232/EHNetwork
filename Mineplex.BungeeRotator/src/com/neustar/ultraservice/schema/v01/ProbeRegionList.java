
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProbeRegionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProbeRegionList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProbeRegion" type="{http://schema.ultraservice.neustar.com/v01/}ProbeRegion" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProbeRegionList", propOrder = {
    "probeRegion"
})
public class ProbeRegionList {

    @XmlElement(name = "ProbeRegion", required = true)
    protected List<ProbeRegion> probeRegion;

    /**
     * Gets the value of the probeRegion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the probeRegion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProbeRegion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProbeRegion }
     * 
     * 
     */
    public List<ProbeRegion> getProbeRegion() {
        if (probeRegion == null) {
            probeRegion = new ArrayList<ProbeRegion>();
        }
        return this.probeRegion;
    }

}

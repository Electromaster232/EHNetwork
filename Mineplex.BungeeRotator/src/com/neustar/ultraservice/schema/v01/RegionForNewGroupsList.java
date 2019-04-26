
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegionForNewGroupsList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegionForNewGroupsList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RegionForNewGroups" type="{http://schema.ultraservice.neustar.com/v01/}RegionForNewGroups" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegionForNewGroupsList", propOrder = {
    "regionForNewGroups"
})
public class RegionForNewGroupsList {

    @XmlElement(name = "RegionForNewGroups", required = true)
    protected List<RegionForNewGroups> regionForNewGroups;

    /**
     * Gets the value of the regionForNewGroups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the regionForNewGroups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegionForNewGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegionForNewGroups }
     * 
     * 
     */
    public List<RegionForNewGroups> getRegionForNewGroups() {
        if (regionForNewGroups == null) {
            regionForNewGroups = new ArrayList<RegionForNewGroups>();
        }
        return this.regionForNewGroups;
    }

}

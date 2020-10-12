
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SBAgentsList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SBAgentsList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SBAgentsData" type="{http://schema.ultraservice.neustar.com/v01/}SBAgentsData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SBAgentsList", propOrder = {
    "sbAgentsData"
})
public class SBAgentsList {

    @XmlElement(name = "SBAgentsData", required = true)
    protected List<SBAgentsData> sbAgentsData;

    /**
     * Gets the value of the sbAgentsData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sbAgentsData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSBAgentsData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SBAgentsData }
     * 
     * 
     */
    public List<SBAgentsData> getSBAgentsData() {
        if (sbAgentsData == null) {
            sbAgentsData = new ArrayList<SBAgentsData>();
        }
        return this.sbAgentsData;
    }

}

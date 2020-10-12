
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SourceIpGroupDefinitionList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SourceIpGroupDefinitionList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SourceIpGroupDefinitionData" type="{http://schema.ultraservice.neustar.com/v01/}SourceIpGroupDefinitionData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourceIpGroupDefinitionList", propOrder = {
    "sourceIpGroupDefinitionData"
})
public class SourceIpGroupDefinitionList {

    @XmlElement(name = "SourceIpGroupDefinitionData", required = true)
    protected List<SourceIpGroupDefinitionData> sourceIpGroupDefinitionData;

    /**
     * Gets the value of the sourceIpGroupDefinitionData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourceIpGroupDefinitionData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceIpGroupDefinitionData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SourceIpGroupDefinitionData }
     * 
     * 
     */
    public List<SourceIpGroupDefinitionData> getSourceIpGroupDefinitionData() {
        if (sourceIpGroupDefinitionData == null) {
            sourceIpGroupDefinitionData = new ArrayList<SourceIpGroupDefinitionData>();
        }
        return this.sourceIpGroupDefinitionData;
    }

}

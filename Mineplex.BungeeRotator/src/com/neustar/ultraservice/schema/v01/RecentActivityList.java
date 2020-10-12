
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RecentActivityList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RecentActivityList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RecentActivity" type="{http://schema.ultraservice.neustar.com/v01/}RecentActivity" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecentActivityList", propOrder = {
    "recentActivity"
})
public class RecentActivityList {

    @XmlElement(name = "RecentActivity", required = true)
    protected List<RecentActivity> recentActivity;

    /**
     * Gets the value of the recentActivity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recentActivity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecentActivity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RecentActivity }
     * 
     * 
     */
    public List<RecentActivity> getRecentActivity() {
        if (recentActivity == null) {
            recentActivity = new ArrayList<RecentActivity>();
        }
        return this.recentActivity;
    }

}

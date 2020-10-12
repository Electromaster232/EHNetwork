
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlertSummaryList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AlertSummaryList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DomainAlertData" type="{http://schema.ultraservice.neustar.com/v01/}DomainAlertData" maxOccurs="unbounded"/>
 *         &lt;element name="ProbeAlertsData" type="{http://schema.ultraservice.neustar.com/v01/}ProbeAlertsData" maxOccurs="unbounded"/>
 *         &lt;element name="MaintenanceAlertsData" type="{http://schema.ultraservice.neustar.com/v01/}MaintenanceAlertsData" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlertSummaryList", propOrder = {
    "domainAlertData",
    "probeAlertsData",
    "maintenanceAlertsData"
})
public class AlertSummaryList {

    @XmlElement(name = "DomainAlertData", required = true)
    protected List<DomainAlertData> domainAlertData;
    @XmlElement(name = "ProbeAlertsData", required = true)
    protected List<ProbeAlertsData> probeAlertsData;
    @XmlElement(name = "MaintenanceAlertsData", required = true)
    protected List<MaintenanceAlertsData> maintenanceAlertsData;

    /**
     * Gets the value of the domainAlertData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domainAlertData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomainAlertData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DomainAlertData }
     * 
     * 
     */
    public List<DomainAlertData> getDomainAlertData() {
        if (domainAlertData == null) {
            domainAlertData = new ArrayList<DomainAlertData>();
        }
        return this.domainAlertData;
    }

    /**
     * Gets the value of the probeAlertsData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the probeAlertsData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProbeAlertsData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProbeAlertsData }
     * 
     * 
     */
    public List<ProbeAlertsData> getProbeAlertsData() {
        if (probeAlertsData == null) {
            probeAlertsData = new ArrayList<ProbeAlertsData>();
        }
        return this.probeAlertsData;
    }

    /**
     * Gets the value of the maintenanceAlertsData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the maintenanceAlertsData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMaintenanceAlertsData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MaintenanceAlertsData }
     * 
     * 
     */
    public List<MaintenanceAlertsData> getMaintenanceAlertsData() {
        if (maintenanceAlertsData == null) {
            maintenanceAlertsData = new ArrayList<MaintenanceAlertsData>();
        }
        return this.maintenanceAlertsData;
    }

}

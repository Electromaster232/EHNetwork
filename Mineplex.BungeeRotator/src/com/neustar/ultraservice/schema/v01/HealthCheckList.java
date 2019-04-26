
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HealthCheckList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HealthCheckList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="currentAPIVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dbConnectionCheck" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="services" type="{http://schema.ultraservice.neustar.com/v01/}ServiceList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealthCheckList", propOrder = {
    "currentAPIVersion",
    "dbConnectionCheck",
    "services"
})
public class HealthCheckList {

    protected String currentAPIVersion;
    protected String dbConnectionCheck;
    protected ServiceList services;

    /**
     * Gets the value of the currentAPIVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentAPIVersion() {
        return currentAPIVersion;
    }

    /**
     * Sets the value of the currentAPIVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentAPIVersion(String value) {
        this.currentAPIVersion = value;
    }

    /**
     * Gets the value of the dbConnectionCheck property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbConnectionCheck() {
        return dbConnectionCheck;
    }

    /**
     * Sets the value of the dbConnectionCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbConnectionCheck(String value) {
        this.dbConnectionCheck = value;
    }

    /**
     * Gets the value of the services property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceList }
     *     
     */
    public ServiceList getServices() {
        return services;
    }

    /**
     * Sets the value of the services property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceList }
     *     
     */
    public void setServices(ServiceList value) {
        this.services = value;
    }

}

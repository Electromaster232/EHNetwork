
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserDefaultPreferences complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserDefaultPreferences">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DefaultAccountPreference" type="{http://schema.ultraservice.neustar.com/v01/}DefaultAccountPreference"/>
 *         &lt;element name="DefaultDateAndTimePreference" type="{http://schema.ultraservice.neustar.com/v01/}DefaultDateAndTimePreference"/>
 *         &lt;element name="DefaultNumberOfrecordsPreference" type="{http://schema.ultraservice.neustar.com/v01/}DefaultNumberOfrecordsPreference"/>
 *         &lt;element name="DeleteConfirmPreference" type="{http://schema.ultraservice.neustar.com/v01/}DeleteConfirmPreference"/>
 *         &lt;element name="AutomaticPointerPreference" type="{http://schema.ultraservice.neustar.com/v01/}AutomaticPointerPreference"/>
 *         &lt;element name="DefaultReportPreference" type="{http://schema.ultraservice.neustar.com/v01/}DefaultReportPrefPreference"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserDefaultPreferences", propOrder = {
    "defaultAccountPreference",
    "defaultDateAndTimePreference",
    "defaultNumberOfrecordsPreference",
    "deleteConfirmPreference",
    "automaticPointerPreference",
    "defaultReportPreference"
})
public class UserDefaultPreferences {

    @XmlElement(name = "DefaultAccountPreference", required = true)
    protected DefaultAccountPreference defaultAccountPreference;
    @XmlElement(name = "DefaultDateAndTimePreference", required = true)
    protected DefaultDateAndTimePreference defaultDateAndTimePreference;
    @XmlElement(name = "DefaultNumberOfrecordsPreference", required = true)
    protected DefaultNumberOfrecordsPreference defaultNumberOfrecordsPreference;
    @XmlElement(name = "DeleteConfirmPreference", required = true)
    protected DeleteConfirmPreference deleteConfirmPreference;
    @XmlElement(name = "AutomaticPointerPreference", required = true)
    protected AutomaticPointerPreference automaticPointerPreference;
    @XmlElement(name = "DefaultReportPreference", required = true)
    protected DefaultReportPrefPreference defaultReportPreference;

    /**
     * Gets the value of the defaultAccountPreference property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultAccountPreference }
     *     
     */
    public DefaultAccountPreference getDefaultAccountPreference() {
        return defaultAccountPreference;
    }

    /**
     * Sets the value of the defaultAccountPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultAccountPreference }
     *     
     */
    public void setDefaultAccountPreference(DefaultAccountPreference value) {
        this.defaultAccountPreference = value;
    }

    /**
     * Gets the value of the defaultDateAndTimePreference property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultDateAndTimePreference }
     *     
     */
    public DefaultDateAndTimePreference getDefaultDateAndTimePreference() {
        return defaultDateAndTimePreference;
    }

    /**
     * Sets the value of the defaultDateAndTimePreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultDateAndTimePreference }
     *     
     */
    public void setDefaultDateAndTimePreference(DefaultDateAndTimePreference value) {
        this.defaultDateAndTimePreference = value;
    }

    /**
     * Gets the value of the defaultNumberOfrecordsPreference property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultNumberOfrecordsPreference }
     *     
     */
    public DefaultNumberOfrecordsPreference getDefaultNumberOfrecordsPreference() {
        return defaultNumberOfrecordsPreference;
    }

    /**
     * Sets the value of the defaultNumberOfrecordsPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultNumberOfrecordsPreference }
     *     
     */
    public void setDefaultNumberOfrecordsPreference(DefaultNumberOfrecordsPreference value) {
        this.defaultNumberOfrecordsPreference = value;
    }

    /**
     * Gets the value of the deleteConfirmPreference property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteConfirmPreference }
     *     
     */
    public DeleteConfirmPreference getDeleteConfirmPreference() {
        return deleteConfirmPreference;
    }

    /**
     * Sets the value of the deleteConfirmPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteConfirmPreference }
     *     
     */
    public void setDeleteConfirmPreference(DeleteConfirmPreference value) {
        this.deleteConfirmPreference = value;
    }

    /**
     * Gets the value of the automaticPointerPreference property.
     * 
     * @return
     *     possible object is
     *     {@link AutomaticPointerPreference }
     *     
     */
    public AutomaticPointerPreference getAutomaticPointerPreference() {
        return automaticPointerPreference;
    }

    /**
     * Sets the value of the automaticPointerPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link AutomaticPointerPreference }
     *     
     */
    public void setAutomaticPointerPreference(AutomaticPointerPreference value) {
        this.automaticPointerPreference = value;
    }

    /**
     * Gets the value of the defaultReportPreference property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultReportPrefPreference }
     *     
     */
    public DefaultReportPrefPreference getDefaultReportPreference() {
        return defaultReportPreference;
    }

    /**
     * Sets the value of the defaultReportPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultReportPrefPreference }
     *     
     */
    public void setDefaultReportPreference(DefaultReportPrefPreference value) {
        this.defaultReportPreference = value;
    }

}

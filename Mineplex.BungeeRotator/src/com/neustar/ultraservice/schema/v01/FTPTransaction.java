
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FTPTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FTPTransaction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="warningCriteria" type="{http://schema.ultraservice.neustar.com/v01/}FTPCriteria" minOccurs="0"/>
 *         &lt;element name="criticalCriteria" type="{http://schema.ultraservice.neustar.com/v01/}FTPCriteria" minOccurs="0"/>
 *         &lt;element name="failCriteria" type="{http://schema.ultraservice.neustar.com/v01/}FTPCriteria" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="port" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="passive" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="username" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="password" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="path" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FTPTransaction", propOrder = {
    "warningCriteria",
    "criticalCriteria",
    "failCriteria"
})
public class FTPTransaction {

    protected FTPCriteria warningCriteria;
    protected FTPCriteria criticalCriteria;
    protected FTPCriteria failCriteria;
    @XmlAttribute(name = "port", required = true)
    protected int port;
    @XmlAttribute(name = "passive", required = true)
    protected boolean passive;
    @XmlAttribute(name = "username")
    protected String username;
    @XmlAttribute(name = "password")
    protected String password;
    @XmlAttribute(name = "path", required = true)
    protected String path;

    /**
     * Gets the value of the warningCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link FTPCriteria }
     *     
     */
    public FTPCriteria getWarningCriteria() {
        return warningCriteria;
    }

    /**
     * Sets the value of the warningCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link FTPCriteria }
     *     
     */
    public void setWarningCriteria(FTPCriteria value) {
        this.warningCriteria = value;
    }

    /**
     * Gets the value of the criticalCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link FTPCriteria }
     *     
     */
    public FTPCriteria getCriticalCriteria() {
        return criticalCriteria;
    }

    /**
     * Sets the value of the criticalCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link FTPCriteria }
     *     
     */
    public void setCriticalCriteria(FTPCriteria value) {
        this.criticalCriteria = value;
    }

    /**
     * Gets the value of the failCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link FTPCriteria }
     *     
     */
    public FTPCriteria getFailCriteria() {
        return failCriteria;
    }

    /**
     * Sets the value of the failCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link FTPCriteria }
     *     
     */
    public void setFailCriteria(FTPCriteria value) {
        this.failCriteria = value;
    }

    /**
     * Gets the value of the port property.
     * 
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     */
    public void setPort(int value) {
        this.port = value;
    }

    /**
     * Gets the value of the passive property.
     * 
     */
    public boolean isPassive() {
        return passive;
    }

    /**
     * Sets the value of the passive property.
     * 
     */
    public void setPassive(boolean value) {
        this.passive = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

}

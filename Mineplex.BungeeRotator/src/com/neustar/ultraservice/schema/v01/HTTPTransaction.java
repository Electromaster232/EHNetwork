
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HTTPTransaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HTTPTransaction">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="warningCriteria" type="{http://schema.ultraservice.neustar.com/v01/}HttpCriteria" minOccurs="0"/>
 *         &lt;element name="criticalCriteria" type="{http://schema.ultraservice.neustar.com/v01/}HttpCriteria" minOccurs="0"/>
 *         &lt;element name="failCriteria" type="{http://schema.ultraservice.neustar.com/v01/}HttpCriteria" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="port" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="hostName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="webPage" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="protocol" type="{http://schema.ultraservice.neustar.com/v01/}protocol" />
 *       &lt;attribute name="method" type="{http://schema.ultraservice.neustar.com/v01/}method" />
 *       &lt;attribute name="transmittedData" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="followRedirect" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HTTPTransaction", propOrder = {
    "warningCriteria",
    "criticalCriteria",
    "failCriteria"
})
public class HTTPTransaction {

    protected HttpCriteria warningCriteria;
    protected HttpCriteria criticalCriteria;
    protected HttpCriteria failCriteria;
    @XmlAttribute(name = "port")
    protected Integer port;
    @XmlAttribute(name = "hostName")
    protected String hostName;
    @XmlAttribute(name = "webPage")
    protected String webPage;
    @XmlAttribute(name = "protocol")
    protected Protocol protocol;
    @XmlAttribute(name = "method")
    protected Method method;
    @XmlAttribute(name = "transmittedData")
    protected String transmittedData;
    @XmlAttribute(name = "followRedirect")
    protected Boolean followRedirect;

    /**
     * Gets the value of the warningCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link HttpCriteria }
     *     
     */
    public HttpCriteria getWarningCriteria() {
        return warningCriteria;
    }

    /**
     * Sets the value of the warningCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link HttpCriteria }
     *     
     */
    public void setWarningCriteria(HttpCriteria value) {
        this.warningCriteria = value;
    }

    /**
     * Gets the value of the criticalCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link HttpCriteria }
     *     
     */
    public HttpCriteria getCriticalCriteria() {
        return criticalCriteria;
    }

    /**
     * Sets the value of the criticalCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link HttpCriteria }
     *     
     */
    public void setCriticalCriteria(HttpCriteria value) {
        this.criticalCriteria = value;
    }

    /**
     * Gets the value of the failCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link HttpCriteria }
     *     
     */
    public HttpCriteria getFailCriteria() {
        return failCriteria;
    }

    /**
     * Sets the value of the failCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link HttpCriteria }
     *     
     */
    public void setFailCriteria(HttpCriteria value) {
        this.failCriteria = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPort(Integer value) {
        this.port = value;
    }

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the webPage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebPage() {
        return webPage;
    }

    /**
     * Sets the value of the webPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebPage(String value) {
        this.webPage = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link Protocol }
     *     
     */
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link Protocol }
     *     
     */
    public void setProtocol(Protocol value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link Method }
     *     
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link Method }
     *     
     */
    public void setMethod(Method value) {
        this.method = value;
    }

    /**
     * Gets the value of the transmittedData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmittedData() {
        return transmittedData;
    }

    /**
     * Sets the value of the transmittedData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmittedData(String value) {
        this.transmittedData = value;
    }

    /**
     * Gets the value of the followRedirect property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFollowRedirect() {
        return followRedirect;
    }

    /**
     * Sets the value of the followRedirect property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFollowRedirect(Boolean value) {
        this.followRedirect = value;
    }

}

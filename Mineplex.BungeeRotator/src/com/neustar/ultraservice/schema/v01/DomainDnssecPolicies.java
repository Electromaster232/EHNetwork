
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DomainDnssecPolicies complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DomainDnssecPolicies">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="cryptoAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nextSecure" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rrsigSignatureDurationInDays" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="kskBitLength" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="zskBitLength" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="kskRollOverPeriodInDays" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="zskRollOverPeriodInDays" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="nsec3Param" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="hashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="optoutFlag" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="iterations" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="salt" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomainDnssecPolicies")
public class DomainDnssecPolicies {

    @XmlAttribute(name = "cryptoAlgorithm")
    protected String cryptoAlgorithm;
    @XmlAttribute(name = "nextSecure")
    protected String nextSecure;
    @XmlAttribute(name = "rrsigSignatureDurationInDays")
    protected Long rrsigSignatureDurationInDays;
    @XmlAttribute(name = "kskBitLength")
    protected Integer kskBitLength;
    @XmlAttribute(name = "zskBitLength")
    protected Integer zskBitLength;
    @XmlAttribute(name = "kskRollOverPeriodInDays")
    protected Integer kskRollOverPeriodInDays;
    @XmlAttribute(name = "zskRollOverPeriodInDays")
    protected Integer zskRollOverPeriodInDays;
    @XmlAttribute(name = "nsec3Param")
    protected String nsec3Param;
    @XmlAttribute(name = "hashAlgorithm")
    protected String hashAlgorithm;
    @XmlAttribute(name = "optoutFlag")
    protected Integer optoutFlag;
    @XmlAttribute(name = "iterations")
    protected Integer iterations;
    @XmlAttribute(name = "salt")
    protected String salt;

    /**
     * Gets the value of the cryptoAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCryptoAlgorithm() {
        return cryptoAlgorithm;
    }

    /**
     * Sets the value of the cryptoAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCryptoAlgorithm(String value) {
        this.cryptoAlgorithm = value;
    }

    /**
     * Gets the value of the nextSecure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextSecure() {
        return nextSecure;
    }

    /**
     * Sets the value of the nextSecure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextSecure(String value) {
        this.nextSecure = value;
    }

    /**
     * Gets the value of the rrsigSignatureDurationInDays property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRrsigSignatureDurationInDays() {
        return rrsigSignatureDurationInDays;
    }

    /**
     * Sets the value of the rrsigSignatureDurationInDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRrsigSignatureDurationInDays(Long value) {
        this.rrsigSignatureDurationInDays = value;
    }

    /**
     * Gets the value of the kskBitLength property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getKskBitLength() {
        return kskBitLength;
    }

    /**
     * Sets the value of the kskBitLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setKskBitLength(Integer value) {
        this.kskBitLength = value;
    }

    /**
     * Gets the value of the zskBitLength property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getZskBitLength() {
        return zskBitLength;
    }

    /**
     * Sets the value of the zskBitLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setZskBitLength(Integer value) {
        this.zskBitLength = value;
    }

    /**
     * Gets the value of the kskRollOverPeriodInDays property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getKskRollOverPeriodInDays() {
        return kskRollOverPeriodInDays;
    }

    /**
     * Sets the value of the kskRollOverPeriodInDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setKskRollOverPeriodInDays(Integer value) {
        this.kskRollOverPeriodInDays = value;
    }

    /**
     * Gets the value of the zskRollOverPeriodInDays property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getZskRollOverPeriodInDays() {
        return zskRollOverPeriodInDays;
    }

    /**
     * Sets the value of the zskRollOverPeriodInDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setZskRollOverPeriodInDays(Integer value) {
        this.zskRollOverPeriodInDays = value;
    }

    /**
     * Gets the value of the nsec3Param property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNsec3Param() {
        return nsec3Param;
    }

    /**
     * Sets the value of the nsec3Param property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNsec3Param(String value) {
        this.nsec3Param = value;
    }

    /**
     * Gets the value of the hashAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * Sets the value of the hashAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashAlgorithm(String value) {
        this.hashAlgorithm = value;
    }

    /**
     * Gets the value of the optoutFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOptoutFlag() {
        return optoutFlag;
    }

    /**
     * Sets the value of the optoutFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOptoutFlag(Integer value) {
        this.optoutFlag = value;
    }

    /**
     * Gets the value of the iterations property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIterations() {
        return iterations;
    }

    /**
     * Sets the value of the iterations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIterations(Integer value) {
        this.iterations = value;
    }

    /**
     * Gets the value of the salt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the value of the salt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalt(String value) {
        this.salt = value;
    }

}

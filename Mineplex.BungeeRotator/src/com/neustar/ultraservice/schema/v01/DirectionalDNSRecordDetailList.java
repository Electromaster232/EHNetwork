
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirectionalDNSRecordDetailList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DirectionalDNSRecordDetailList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DirectionalDNSRecordDetail" type="{http://schema.ultraservice.neustar.com/v01/}DirectionalDNSRecordDetail" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ZoneName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectionalDNSRecordDetailList", propOrder = {
    "directionalDNSRecordDetail"
})
public class DirectionalDNSRecordDetailList {

    @XmlElement(name = "DirectionalDNSRecordDetail", required = true)
    protected List<DirectionalDNSRecordDetail> directionalDNSRecordDetail;
    @XmlAttribute(name = "ZoneName", required = true)
    protected String zoneName;
    @XmlAttribute(name = "DName", required = true)
    protected String dName;

    /**
     * Gets the value of the directionalDNSRecordDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the directionalDNSRecordDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDirectionalDNSRecordDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DirectionalDNSRecordDetail }
     * 
     * 
     */
    public List<DirectionalDNSRecordDetail> getDirectionalDNSRecordDetail() {
        if (directionalDNSRecordDetail == null) {
            directionalDNSRecordDetail = new ArrayList<DirectionalDNSRecordDetail>();
        }
        return this.directionalDNSRecordDetail;
    }

    /**
     * Gets the value of the zoneName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoneName() {
        return zoneName;
    }

    /**
     * Sets the value of the zoneName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoneName(String value) {
        this.zoneName = value;
    }

    /**
     * Gets the value of the dName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDName() {
        return dName;
    }

    /**
     * Sets the value of the dName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDName(String value) {
        this.dName = value;
    }

}

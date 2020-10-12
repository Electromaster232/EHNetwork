
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.neustar.ultraservice.schema.AccountSegmentMapStatusType;


/**
 * <p>Java class for NameServerRecordSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NameServerRecordSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NameServerRecord" type="{http://schema.ultraservice.neustar.com/v01/}NameServerRecord" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required" type="{http://schema.ultraservice.neustar.com/}AccountSegmentMapStatusType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NameServerRecordSet", propOrder = {
    "nameServerRecord"
})
public class NameServerRecordSet {

    @XmlElement(name = "NameServerRecord", required = true)
    protected List<NameServerRecord> nameServerRecord;
    @XmlAttribute(name = "type", required = true)
    protected AccountSegmentMapStatusType type;

    /**
     * Gets the value of the nameServerRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nameServerRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNameServerRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NameServerRecord }
     * 
     * 
     */
    public List<NameServerRecord> getNameServerRecord() {
        if (nameServerRecord == null) {
            nameServerRecord = new ArrayList<NameServerRecord>();
        }
        return this.nameServerRecord;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link AccountSegmentMapStatusType }
     *     
     */
    public AccountSegmentMapStatusType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountSegmentMapStatusType }
     *     
     */
    public void setType(AccountSegmentMapStatusType value) {
        this.type = value;
    }

}

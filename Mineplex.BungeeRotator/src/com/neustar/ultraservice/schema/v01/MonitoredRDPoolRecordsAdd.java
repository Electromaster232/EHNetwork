
package com.neustar.ultraservice.schema.v01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MonitoredRDPoolRecordsAdd complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MonitoredRDPoolRecordsAdd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="liveRecord" type="{http://schema.ultraservice.neustar.com/v01/}MonitoredRDPoolRecordAdd" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="responseMethod" use="required" type="{http://schema.ultraservice.neustar.com/v01/}ResponseMethod" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonitoredRDPoolRecordsAdd", propOrder = {
    "liveRecord"
})
public class MonitoredRDPoolRecordsAdd {

    @XmlElement(required = true)
    protected List<MonitoredRDPoolRecordAdd> liveRecord;
    @XmlAttribute(name = "responseMethod", required = true)
    protected ResponseMethod responseMethod;

    /**
     * Gets the value of the liveRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the liveRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLiveRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MonitoredRDPoolRecordAdd }
     * 
     * 
     */
    public List<MonitoredRDPoolRecordAdd> getLiveRecord() {
        if (liveRecord == null) {
            liveRecord = new ArrayList<MonitoredRDPoolRecordAdd>();
        }
        return this.liveRecord;
    }

    /**
     * Gets the value of the responseMethod property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseMethod }
     *     
     */
    public ResponseMethod getResponseMethod() {
        return responseMethod;
    }

    /**
     * Sets the value of the responseMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseMethod }
     *     
     */
    public void setResponseMethod(ResponseMethod value) {
        this.responseMethod = value;
    }

}

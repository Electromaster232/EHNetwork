
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transferStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="transferStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="QUEUED"/>
 *     &lt;enumeration value="IN_PROGRESS"/>
 *     &lt;enumeration value="COMPLETED"/>
 *     &lt;enumeration value="FAILED"/>
 *     &lt;enumeration value="NOT_REQUESTED"/>
 *     &lt;enumeration value="PENDING"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "transferStatusType")
@XmlEnum
public enum TransferStatusType {

    QUEUED,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    NOT_REQUESTED,
    PENDING;

    public String value() {
        return name();
    }

    public static TransferStatusType fromValue(String v) {
        return valueOf(v);
    }

}

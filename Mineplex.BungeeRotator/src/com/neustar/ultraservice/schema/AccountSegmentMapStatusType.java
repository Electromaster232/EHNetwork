
package com.neustar.ultraservice.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountSegmentMapStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AccountSegmentMapStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Pending"/>
 *     &lt;enumeration value="Active"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AccountSegmentMapStatusType", namespace = "http://schema.ultraservice.neustar.com/")
@XmlEnum
public enum AccountSegmentMapStatusType {

    @XmlEnumValue("Pending")
    PENDING("Pending"),
    @XmlEnumValue("Active")
    ACTIVE("Active");
    private final String value;

    AccountSegmentMapStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AccountSegmentMapStatusType fromValue(String v) {
        for (AccountSegmentMapStatusType c: AccountSegmentMapStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

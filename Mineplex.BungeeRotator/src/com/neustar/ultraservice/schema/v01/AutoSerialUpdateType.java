
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for autoSerialUpdateType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="autoSerialUpdateType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="enable"/>
 *     &lt;enumeration value="disable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "autoSerialUpdateType")
@XmlEnum
public enum AutoSerialUpdateType {

    @XmlEnumValue("enable")
    ENABLE("enable"),
    @XmlEnumValue("disable")
    DISABLE("disable");
    private final String value;

    AutoSerialUpdateType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AutoSerialUpdateType fromValue(String v) {
        for (AutoSerialUpdateType c: AutoSerialUpdateType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

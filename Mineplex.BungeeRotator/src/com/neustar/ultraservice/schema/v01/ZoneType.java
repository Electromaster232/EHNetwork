
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for zoneType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="zoneType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="primary"/>
 *     &lt;enumeration value="secondary"/>
 *     &lt;enumeration value="alias"/>
 *     &lt;enumeration value="all"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "zoneType")
@XmlEnum
public enum ZoneType {

    @XmlEnumValue("primary")
    PRIMARY("primary"),
    @XmlEnumValue("secondary")
    SECONDARY("secondary"),
    @XmlEnumValue("alias")
    ALIAS("alias"),
    @XmlEnumValue("all")
    ALL("all");
    private final String value;

    ZoneType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ZoneType fromValue(String v) {
        for (ZoneType c: ZoneType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

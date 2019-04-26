
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for probeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="probeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="global"/>
 *     &lt;enumeration value="record"/>
 *     &lt;enumeration value="all"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "probeType")
@XmlEnum
public enum ProbeType {

    @XmlEnumValue("global")
    GLOBAL("global"),
    @XmlEnumValue("record")
    RECORD("record"),
    @XmlEnumValue("all")
    ALL("all");
    private final String value;

    ProbeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProbeType fromValue(String v) {
        for (ProbeType c: ProbeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

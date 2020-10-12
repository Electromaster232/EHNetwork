
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for graphType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="graphType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Text"/>
 *     &lt;enumeration value="Bar"/>
 *     &lt;enumeration value="Step"/>
 *     &lt;enumeration value="Line"/>
 *     &lt;enumeration value="Area"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "graphType")
@XmlEnum
public enum GraphType {

    @XmlEnumValue("Text")
    TEXT("Text"),
    @XmlEnumValue("Bar")
    BAR("Bar"),
    @XmlEnumValue("Step")
    STEP("Step"),
    @XmlEnumValue("Line")
    LINE("Line"),
    @XmlEnumValue("Area")
    AREA("Area");
    private final String value;

    GraphType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GraphType fromValue(String v) {
        for (GraphType c: GraphType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

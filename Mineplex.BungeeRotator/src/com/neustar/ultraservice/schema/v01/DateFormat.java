
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DateFormat.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DateFormat">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MM/DD/YYYY"/>
 *     &lt;enumeration value="MM/DD/YY"/>
 *     &lt;enumeration value="DD/MM/YYYY"/>
 *     &lt;enumeration value="DD/MM/YY"/>
 *     &lt;enumeration value="YYYY/MM/DD"/>
 *     &lt;enumeration value="YY/MM/DD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DateFormat")
@XmlEnum
public enum DateFormat {

    @XmlEnumValue("MM/DD/YYYY")
    MM_DD_YYYY("MM/DD/YYYY"),
    @XmlEnumValue("MM/DD/YY")
    MM_DD_YY("MM/DD/YY"),
    @XmlEnumValue("DD/MM/YYYY")
    DD_MM_YYYY("DD/MM/YYYY"),
    @XmlEnumValue("DD/MM/YY")
    DD_MM_YY("DD/MM/YY"),
    @XmlEnumValue("YYYY/MM/DD")
    YYYY_MM_DD("YYYY/MM/DD"),
    @XmlEnumValue("YY/MM/DD")
    YY_MM_DD("YY/MM/DD");
    private final String value;

    DateFormat(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DateFormat fromValue(String v) {
        for (DateFormat c: DateFormat.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RedirectType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RedirectType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Framed"/>
 *     &lt;enumeration value="HTTP_301_REDIRECT"/>
 *     &lt;enumeration value="HTTP_302_REDIRECT"/>
 *     &lt;enumeration value="HTTP_303_REDIRECT"/>
 *     &lt;enumeration value="HTTP_307_REDIRECT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RedirectType")
@XmlEnum
public enum RedirectType {

    @XmlEnumValue("Framed")
    FRAMED("Framed"),
    HTTP_301_REDIRECT("HTTP_301_REDIRECT"),
    HTTP_302_REDIRECT("HTTP_302_REDIRECT"),
    HTTP_303_REDIRECT("HTTP_303_REDIRECT"),
    HTTP_307_REDIRECT("HTTP_307_REDIRECT");
    private final String value;

    RedirectType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RedirectType fromValue(String v) {
        for (RedirectType c: RedirectType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

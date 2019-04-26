
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for simpleFailoverConversion.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="simpleFailoverConversion">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="RD"/>
 *     &lt;enumeration value="ADAPTIVE_RESPONSE"/>
 *     &lt;enumeration value="MRD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "simpleFailoverConversion")
@XmlEnum
public enum SimpleFailoverConversion {

    A,
    RD,
    ADAPTIVE_RESPONSE,
    MRD;

    public String value() {
        return name();
    }

    public static SimpleFailoverConversion fromValue(String v) {
        return valueOf(v);
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResponseMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResponseMethod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FIXED"/>
 *     &lt;enumeration value="RANDOM"/>
 *     &lt;enumeration value="ROUND_ROBIN"/>
 *     &lt;enumeration value="WEIGHTED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResponseMethod")
@XmlEnum
public enum ResponseMethod {

    FIXED,
    RANDOM,
    ROUND_ROBIN,
    WEIGHTED;

    public String value() {
        return name();
    }

    public static ResponseMethod fromValue(String v) {
        return valueOf(v);
    }

}

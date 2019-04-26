
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for conversionTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="conversionTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="RD"/>
 *     &lt;enumeration value="SIMPLE_FAILOVER"/>
 *     &lt;enumeration value="ADAPTIVE_RESPONSE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "conversionTypeEnum")
@XmlEnum
public enum ConversionTypeEnum {

    A,
    RD,
    SIMPLE_FAILOVER,
    ADAPTIVE_RESPONSE;

    public String value() {
        return name();
    }

    public static ConversionTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}

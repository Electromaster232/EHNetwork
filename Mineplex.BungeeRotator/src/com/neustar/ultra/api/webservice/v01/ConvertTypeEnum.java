
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for convertTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="convertTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="MRD"/>
 *     &lt;enumeration value="ADAPTIVE_RESPONSE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "convertTypeEnum")
@XmlEnum
public enum ConvertTypeEnum {

    A,
    MRD,
    ADAPTIVE_RESPONSE;

    public String value() {
        return name();
    }

    public static ConvertTypeEnum fromValue(String v) {
        return valueOf(v);
    }

}

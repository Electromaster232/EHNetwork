
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ARStatusEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ARStatusEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OK"/>
 *     &lt;enumeration value="FAIL"/>
 *     &lt;enumeration value="MANUAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ARStatusEnum")
@XmlEnum
public enum ARStatusEnum {

    OK,
    FAIL,
    MANUAL;

    public String value() {
        return name();
    }

    public static ARStatusEnum fromValue(String v) {
        return valueOf(v);
    }

}

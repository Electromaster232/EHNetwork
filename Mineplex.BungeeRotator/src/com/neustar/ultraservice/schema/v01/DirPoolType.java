
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirPoolType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DirPoolType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GEOLOCATION"/>
 *     &lt;enumeration value="SOURCEIP"/>
 *     &lt;enumeration value="MIXED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DirPoolType")
@XmlEnum
public enum DirPoolType {

    GEOLOCATION,
    SOURCEIP,
    MIXED;

    public String value() {
        return name();
    }

    public static DirPoolType fromValue(String v) {
        return valueOf(v);
    }

}

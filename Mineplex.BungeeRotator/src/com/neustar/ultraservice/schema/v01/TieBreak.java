
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TieBreak.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TieBreak">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GEOLOCATION"/>
 *     &lt;enumeration value="SOURCEIP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TieBreak")
@XmlEnum
public enum TieBreak {

    GEOLOCATION,
    SOURCEIP;

    public String value() {
        return name();
    }

    public static TieBreak fromValue(String v) {
        return valueOf(v);
    }

}

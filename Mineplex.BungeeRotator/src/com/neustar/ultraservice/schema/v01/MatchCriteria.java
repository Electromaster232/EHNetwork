
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MatchCriteria.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MatchCriteria">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="BEGINS_WITH"/>
 *     &lt;enumeration value="CONTAINS"/>
 *     &lt;enumeration value="ENDS_WITH"/>
 *     &lt;enumeration value="MATCHES"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MatchCriteria")
@XmlEnum
public enum MatchCriteria {

    BEGINS_WITH,
    CONTAINS,
    ENDS_WITH,
    MATCHES;

    public String value() {
        return name();
    }

    public static MatchCriteria fromValue(String v) {
        return valueOf(v);
    }

}

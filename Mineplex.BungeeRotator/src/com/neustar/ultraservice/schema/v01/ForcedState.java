
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ForcedState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ForcedState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FORCED_ACTIVE"/>
 *     &lt;enumeration value="FORCED_INACTIVE"/>
 *     &lt;enumeration value="NOT_FORCED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ForcedState")
@XmlEnum
public enum ForcedState {

    FORCED_ACTIVE,
    FORCED_INACTIVE,
    NOT_FORCED;

    public String value() {
        return name();
    }

    public static ForcedState fromValue(String v) {
        return valueOf(v);
    }

}

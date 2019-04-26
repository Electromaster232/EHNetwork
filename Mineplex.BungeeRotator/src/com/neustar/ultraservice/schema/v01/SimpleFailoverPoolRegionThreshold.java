
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for simpleFailoverPoolRegionThreshold.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="simpleFailoverPoolRegionThreshold">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LOW"/>
 *     &lt;enumeration value="HIGH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "simpleFailoverPoolRegionThreshold")
@XmlEnum
public enum SimpleFailoverPoolRegionThreshold {

    LOW,
    HIGH;

    public String value() {
        return name();
    }

    public static SimpleFailoverPoolRegionThreshold fromValue(String v) {
        return valueOf(v);
    }

}

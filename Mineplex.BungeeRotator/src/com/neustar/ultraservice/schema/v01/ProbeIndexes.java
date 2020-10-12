
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for probeIndexes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="probeIndexes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PROBE_LEVEL"/>
 *     &lt;enumeration value="NAME"/>
 *     &lt;enumeration value="TYPE"/>
 *     &lt;enumeration value="INTERVAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "probeIndexes")
@XmlEnum
public enum ProbeIndexes {

    PROBE_LEVEL,
    NAME,
    TYPE,
    INTERVAL;

    public String value() {
        return name();
    }

    public static ProbeIndexes fromValue(String v) {
        return valueOf(v);
    }

}

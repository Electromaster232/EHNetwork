
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for failoverMonitorMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="failoverMonitorMethod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GET"/>
 *     &lt;enumeration value="POST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "failoverMonitorMethod")
@XmlEnum
public enum FailoverMonitorMethod {

    GET,
    POST;

    public String value() {
        return name();
    }

    public static FailoverMonitorMethod fromValue(String v) {
        return valueOf(v);
    }

}

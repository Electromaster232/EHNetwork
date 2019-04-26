
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for code.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PENDING"/>
 *     &lt;enumeration value="IN_PROCESS"/>
 *     &lt;enumeration value="COMPLETE"/>
 *     &lt;enumeration value="ERROR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "code")
@XmlEnum
public enum Code {

    PENDING,
    IN_PROCESS,
    COMPLETE,
    ERROR;

    public String value() {
        return name();
    }

    public static Code fromValue(String v) {
        return valueOf(v);
    }

}

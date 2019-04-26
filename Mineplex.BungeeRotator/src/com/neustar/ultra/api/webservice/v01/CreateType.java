
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="createType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NEW"/>
 *     &lt;enumeration value="COPY"/>
 *     &lt;enumeration value="TRANSFER"/>
 *     &lt;enumeration value="UPLOAD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "createType")
@XmlEnum
public enum CreateType {

    NEW,
    COPY,
    TRANSFER,
    UPLOAD;

    public String value() {
        return name();
    }

    public static CreateType fromValue(String v) {
        return valueOf(v);
    }

}

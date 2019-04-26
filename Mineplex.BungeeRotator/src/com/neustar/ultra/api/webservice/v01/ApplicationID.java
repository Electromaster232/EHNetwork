
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for applicationID.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="applicationID">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="JAVA_UI"/>
 *     &lt;enumeration value="SOAP_API"/>
 *     &lt;enumeration value="MASQ_UI"/>
 *     &lt;enumeration value="UA"/>
 *     &lt;enumeration value="PHP_UI"/>
 *     &lt;enumeration value="XML_API"/>
 *     &lt;enumeration value="RPS_TIMER"/>
 *     &lt;enumeration value="SMADS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "applicationID")
@XmlEnum
public enum ApplicationID {

    JAVA_UI,
    SOAP_API,
    MASQ_UI,
    UA,
    PHP_UI,
    XML_API,
    RPS_TIMER,
    SMADS;

    public String value() {
        return name();
    }

    public static ApplicationID fromValue(String v) {
        return valueOf(v);
    }

}

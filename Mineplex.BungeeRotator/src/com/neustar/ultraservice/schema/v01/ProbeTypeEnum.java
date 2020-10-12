
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProbeTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ProbeTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HTTP"/>
 *     &lt;enumeration value="PING"/>
 *     &lt;enumeration value="FTP"/>
 *     &lt;enumeration value="TCP"/>
 *     &lt;enumeration value="SMTP"/>
 *     &lt;enumeration value="SMTP2"/>
 *     &lt;enumeration value="DNS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ProbeTypeEnum")
@XmlEnum
public enum ProbeTypeEnum {

    HTTP("HTTP"),
    PING("PING"),
    FTP("FTP"),
    TCP("TCP"),
    SMTP("SMTP"),
    @XmlEnumValue("SMTP2")
    SMTP_2("SMTP2"),
    DNS("DNS");
    private final String value;

    ProbeTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProbeTypeEnum fromValue(String v) {
        for (ProbeTypeEnum c: ProbeTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

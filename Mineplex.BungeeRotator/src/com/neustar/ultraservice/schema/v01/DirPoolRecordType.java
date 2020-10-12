
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DirPoolRecordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DirPoolRecordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="AAAA"/>
 *     &lt;enumeration value="TXT"/>
 *     &lt;enumeration value="SRV"/>
 *     &lt;enumeration value="PTR"/>
 *     &lt;enumeration value="HINFO"/>
 *     &lt;enumeration value="RP"/>
 *     &lt;enumeration value="NAPTR"/>
 *     &lt;enumeration value="MX"/>
 *     &lt;enumeration value="CNAME"/>
 *     &lt;enumeration value="SPF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DirPoolRecordType")
@XmlEnum
public enum DirPoolRecordType {

    A,
    AAAA,
    TXT,
    SRV,
    PTR,
    HINFO,
    RP,
    NAPTR,
    MX,
    CNAME,
    SPF;

    public String value() {
        return name();
    }

    public static DirPoolRecordType fromValue(String v) {
        return valueOf(v);
    }

}

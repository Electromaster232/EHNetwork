
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PoolRecordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PoolRecordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="AAAA"/>
 *     &lt;enumeration value="SUBPOOL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PoolRecordType")
@XmlEnum
public enum PoolRecordType {

    A,
    AAAA,
    SUBPOOL;

    public String value() {
        return name();
    }

    public static PoolRecordType fromValue(String v) {
        return valueOf(v);
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for poolIndexes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="poolIndexes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NAME"/>
 *     &lt;enumeration value="TYPE"/>
 *     &lt;enumeration value="DESCRIPTION"/>
 *     &lt;enumeration value="RECORDSCOUNT"/>
 *     &lt;enumeration value="RESPONSEMETHOD"/>
 *     &lt;enumeration value="REQUEST"/>
 *     &lt;enumeration value="LBPOOLTYPE"/>
 *     &lt;enumeration value="STATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "poolIndexes")
@XmlEnum
public enum PoolIndexes {

    NAME,
    TYPE,
    DESCRIPTION,
    RECORDSCOUNT,
    RESPONSEMETHOD,
    REQUEST,
    LBPOOLTYPE,
    STATE;

    public String value() {
        return name();
    }

    public static PoolIndexes fromValue(String v) {
        return valueOf(v);
    }

}


package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GroupingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GroupingType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DEFINE_NEW_GROUP"/>
 *     &lt;enumeration value="COPY_EXISTING_GROUP"/>
 *     &lt;enumeration value="ASSIGN_EXISTING_GROUP"/>
 *     &lt;enumeration value="ASSIGN_GLOBAL_GROUP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GroupingType")
@XmlEnum
public enum GroupingType {

    DEFINE_NEW_GROUP,
    COPY_EXISTING_GROUP,
    ASSIGN_EXISTING_GROUP,
    ASSIGN_GLOBAL_GROUP;

    public String value() {
        return name();
    }

    public static GroupingType fromValue(String v) {
        return valueOf(v);
    }

}

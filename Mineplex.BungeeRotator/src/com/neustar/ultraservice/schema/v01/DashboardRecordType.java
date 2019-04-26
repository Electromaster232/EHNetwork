
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DashboardRecordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DashboardRecordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Recent Activity"/>
 *     &lt;enumeration value="Alerts"/>
 *     &lt;enumeration value="My Reports"/>
 *     &lt;enumeration value="News"/>
 *     &lt;enumeration value="Open Service Requests"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DashboardRecordType")
@XmlEnum
public enum DashboardRecordType {

    @XmlEnumValue("Recent Activity")
    RECENT_ACTIVITY("Recent Activity"),
    @XmlEnumValue("Alerts")
    ALERTS("Alerts"),
    @XmlEnumValue("My Reports")
    MY_REPORTS("My Reports"),
    @XmlEnumValue("News")
    NEWS("News"),
    @XmlEnumValue("Open Service Requests")
    OPEN_SERVICE_REQUESTS("Open Service Requests");
    private final String value;

    DashboardRecordType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DashboardRecordType fromValue(String v) {
        for (DashboardRecordType c: DashboardRecordType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

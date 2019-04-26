
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reportType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="reportType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Domains"/>
 *     &lt;enumeration value="Records"/>
 *     &lt;enumeration value="Queries"/>
 *     &lt;enumeration value="Region"/>
 *     &lt;enumeration value="PoolRecord"/>
 *     &lt;enumeration value="FailoverProbe"/>
 *     &lt;enumeration value="Failover"/>
 *     &lt;enumeration value="Overall Activity - Domains"/>
 *     &lt;enumeration value="Overall Activity - Records"/>
 *     &lt;enumeration value="Overall Activity - Queries"/>
 *     &lt;enumeration value="Activity by Region"/>
 *     &lt;enumeration value="Failover - Pool Record"/>
 *     &lt;enumeration value="Failover - Probe Statistics"/>
 *     &lt;enumeration value="Failover - Failover"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "reportType")
@XmlEnum
public enum ReportType {

    @XmlEnumValue("Domains")
    DOMAINS("Domains"),
    @XmlEnumValue("Records")
    RECORDS("Records"),
    @XmlEnumValue("Queries")
    QUERIES("Queries"),
    @XmlEnumValue("Region")
    REGION("Region"),
    @XmlEnumValue("PoolRecord")
    POOL_RECORD("PoolRecord"),
    @XmlEnumValue("FailoverProbe")
    FAILOVER_PROBE("FailoverProbe"),
    @XmlEnumValue("Failover")
    FAILOVER("Failover"),
    @XmlEnumValue("Overall Activity - Domains")
    OVERALL_ACTIVITY_DOMAINS("Overall Activity - Domains"),
    @XmlEnumValue("Overall Activity - Records")
    OVERALL_ACTIVITY_RECORDS("Overall Activity - Records"),
    @XmlEnumValue("Overall Activity - Queries")
    OVERALL_ACTIVITY_QUERIES("Overall Activity - Queries"),
    @XmlEnumValue("Activity by Region")
    ACTIVITY_BY_REGION("Activity by Region"),
    @XmlEnumValue("Failover - Pool Record")
    FAILOVER_POOL_RECORD("Failover - Pool Record"),
    @XmlEnumValue("Failover - Probe Statistics")
    FAILOVER_PROBE_STATISTICS("Failover - Probe Statistics"),
    @XmlEnumValue("Failover - Failover")
    FAILOVER_FAILOVER("Failover - Failover");
    private final String value;

    ReportType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReportType fromValue(String v) {
        for (ReportType c: ReportType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

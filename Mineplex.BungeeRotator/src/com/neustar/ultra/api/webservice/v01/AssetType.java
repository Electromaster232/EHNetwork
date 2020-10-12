
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for assetType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="assetType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACCOUNT"/>
 *     &lt;enumeration value="EXTERNAL_SERVICES"/>
 *     &lt;enumeration value="RECURSIVE"/>
 *     &lt;enumeration value="ZONE"/>
 *     &lt;enumeration value="RESOURCE_RECORD"/>
 *     &lt;enumeration value="USER"/>
 *     &lt;enumeration value="APPLIANCE"/>
 *     &lt;enumeration value="DHCP"/>
 *     &lt;enumeration value="REPORT"/>
 *     &lt;enumeration value="SERVICE_PACKAGE"/>
 *     &lt;enumeration value="DIRECTIONALDNS"/>
 *     &lt;enumeration value="DIRECTIONALDNS_RECORD"/>
 *     &lt;enumeration value="WEB_FORWARD"/>
 *     &lt;enumeration value="MAIL_FORWARD"/>
 *     &lt;enumeration value="TRAFFICCONTROLLER_POOL"/>
 *     &lt;enumeration value="TC_POOL_RECORD"/>
 *     &lt;enumeration value="ROUNDROBIN_POOL"/>
 *     &lt;enumeration value="RD_POOL_RECORD"/>
 *     &lt;enumeration value="PROPERTIES"/>
 *     &lt;enumeration value="MAIL_MXBACKER"/>
 *     &lt;enumeration value="ACCOUNT_PERMISSIONS"/>
 *     &lt;enumeration value="ACCOUNT_PREFERENCES"/>
 *     &lt;enumeration value="BILLING"/>
 *     &lt;enumeration value="GROUPS_AND_USERS"/>
 *     &lt;enumeration value="A"/>
 *     &lt;enumeration value="AAAA"/>
 *     &lt;enumeration value="CNAME"/>
 *     &lt;enumeration value="TXT"/>
 *     &lt;enumeration value="SRV"/>
 *     &lt;enumeration value="NS"/>
 *     &lt;enumeration value="PTR"/>
 *     &lt;enumeration value="RP"/>
 *     &lt;enumeration value="HINFO"/>
 *     &lt;enumeration value="NAPTR"/>
 *     &lt;enumeration value="MX"/>
 *     &lt;enumeration value="OTHER_RR"/>
 *     &lt;enumeration value="SITEBACKER_POOL"/>
 *     &lt;enumeration value="SCHEDULER_TASK"/>
 *     &lt;enumeration value="SITEBACKER_DISTRIBUTOR"/>
 *     &lt;enumeration value="SITEBACKER_AGENT"/>
 *     &lt;enumeration value="SITEBACKER_POOL_RECORD"/>
 *     &lt;enumeration value="SITEBACKER_POOL_PROBE"/>
 *     &lt;enumeration value="SOA"/>
 *     &lt;enumeration value="ACCOUNT_LEVEL_DIRECTIONAL_GROUP"/>
 *     &lt;enumeration value="RESOURCE_DISTRIBUTION_POOL"/>
 *     &lt;enumeration value="ADAPTIVERESPONSE_POOL"/>
 *     &lt;enumeration value="ADAPTIVERESPONSE_POOL_CONFIGURATION"/>
 *     &lt;enumeration value="ADAPTIVERESPONSE_POOL_RECORD"/>
 *     &lt;enumeration value="ADAPTIVERESPONSE_POOL_PROBE"/>
 *     &lt;enumeration value="ADAPTIVERESPONSE_RECORD_PROBE"/>
 *     &lt;enumeration value="ADAPTIVERESPONSE_PROBE_DEFINITION"/>
 *     &lt;enumeration value="SIMPLEFAILOVER_POOL"/>
 *     &lt;enumeration value="MONITORED_RD_POOL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "assetType")
@XmlEnum
public enum AssetType {

    ACCOUNT,
    EXTERNAL_SERVICES,
    RECURSIVE,
    ZONE,
    RESOURCE_RECORD,
    USER,
    APPLIANCE,
    DHCP,
    REPORT,
    SERVICE_PACKAGE,
    DIRECTIONALDNS,
    DIRECTIONALDNS_RECORD,
    WEB_FORWARD,
    MAIL_FORWARD,
    TRAFFICCONTROLLER_POOL,
    TC_POOL_RECORD,
    ROUNDROBIN_POOL,
    RD_POOL_RECORD,
    PROPERTIES,
    MAIL_MXBACKER,
    ACCOUNT_PERMISSIONS,
    ACCOUNT_PREFERENCES,
    BILLING,
    GROUPS_AND_USERS,
    A,
    AAAA,
    CNAME,
    TXT,
    SRV,
    NS,
    PTR,
    RP,
    HINFO,
    NAPTR,
    MX,
    OTHER_RR,
    SITEBACKER_POOL,
    SCHEDULER_TASK,
    SITEBACKER_DISTRIBUTOR,
    SITEBACKER_AGENT,
    SITEBACKER_POOL_RECORD,
    SITEBACKER_POOL_PROBE,
    SOA,
    ACCOUNT_LEVEL_DIRECTIONAL_GROUP,
    RESOURCE_DISTRIBUTION_POOL,
    ADAPTIVERESPONSE_POOL,
    ADAPTIVERESPONSE_POOL_CONFIGURATION,
    ADAPTIVERESPONSE_POOL_RECORD,
    ADAPTIVERESPONSE_POOL_PROBE,
    ADAPTIVERESPONSE_RECORD_PROBE,
    ADAPTIVERESPONSE_PROBE_DEFINITION,
    SIMPLEFAILOVER_POOL,
    MONITORED_RD_POOL;

    public String value() {
        return name();
    }

    public static AssetType fromValue(String v) {
        return valueOf(v);
    }

}

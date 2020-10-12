
package com.neustar.ultraservice.schema.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceRecordType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResourceRecordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Directional"/>
 *     &lt;enumeration value="Load Balancing"/>
 *     &lt;enumeration value="A (IPv4 Host)"/>
 *     &lt;enumeration value="AAAA (IPv6 Host)"/>
 *     &lt;enumeration value="CNAME (Alias)"/>
 *     &lt;enumeration value="Web Forwarding"/>
 *     &lt;enumeration value="MX (Mail Exchange)"/>
 *     &lt;enumeration value="TXT (Text)"/>
 *     &lt;enumeration value="SRV (Service Locator)"/>
 *     &lt;enumeration value="NS (Nameserver)"/>
 *     &lt;enumeration value="PTR (Pointer)"/>
 *     &lt;enumeration value="RP (Responsible Person)"/>
 *     &lt;enumeration value="HINFO (Host Info)"/>
 *     &lt;enumeration value="NAPTR (Naming Authority Pointer)"/>
 *     &lt;enumeration value="SPF (Sender Policy Framework)"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResourceRecordType")
@XmlEnum
public enum ResourceRecordType {

    @XmlEnumValue("Directional")
    DIRECTIONAL("Directional"),
    @XmlEnumValue("Load Balancing")
    LOAD_BALANCING("Load Balancing"),
    @XmlEnumValue("A (IPv4 Host)")
    A_I_PV_4_HOST("A (IPv4 Host)"),
    @XmlEnumValue("AAAA (IPv6 Host)")
    AAAA_I_PV_6_HOST("AAAA (IPv6 Host)"),
    @XmlEnumValue("CNAME (Alias)")
    CNAME_ALIAS("CNAME (Alias)"),
    @XmlEnumValue("Web Forwarding")
    WEB_FORWARDING("Web Forwarding"),
    @XmlEnumValue("MX (Mail Exchange)")
    MX_MAIL_EXCHANGE("MX (Mail Exchange)"),
    @XmlEnumValue("TXT (Text)")
    TXT_TEXT("TXT (Text)"),
    @XmlEnumValue("SRV (Service Locator)")
    SRV_SERVICE_LOCATOR("SRV (Service Locator)"),
    @XmlEnumValue("NS (Nameserver)")
    NS_NAMESERVER("NS (Nameserver)"),
    @XmlEnumValue("PTR (Pointer)")
    PTR_POINTER("PTR (Pointer)"),
    @XmlEnumValue("RP (Responsible Person)")
    RP_RESPONSIBLE_PERSON("RP (Responsible Person)"),
    @XmlEnumValue("HINFO (Host Info)")
    HINFO_HOST_INFO("HINFO (Host Info)"),
    @XmlEnumValue("NAPTR (Naming Authority Pointer)")
    NAPTR_NAMING_AUTHORITY_POINTER("NAPTR (Naming Authority Pointer)"),
    @XmlEnumValue("SPF (Sender Policy Framework)")
    SPF_SENDER_POLICY_FRAMEWORK("SPF (Sender Policy Framework)");
    private final String value;

    ResourceRecordType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResourceRecordType fromValue(String v) {
        for (ResourceRecordType c: ResourceRecordType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

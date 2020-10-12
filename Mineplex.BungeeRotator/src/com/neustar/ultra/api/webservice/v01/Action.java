
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for action.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="action">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ADD"/>
 *     &lt;enumeration value="MODIFY"/>
 *     &lt;enumeration value="DELETE"/>
 *     &lt;enumeration value="BULK_ADD"/>
 *     &lt;enumeration value="BULK_MODIFY"/>
 *     &lt;enumeration value="BULK_DELETE"/>
 *     &lt;enumeration value="LOGIN"/>
 *     &lt;enumeration value="SIGN"/>
 *     &lt;enumeration value="UNSIGN"/>
 *     &lt;enumeration value="FAILOVER"/>
 *     &lt;enumeration value="FAILBACK"/>
 *     &lt;enumeration value="MIGRATE_SBTC_TO_AR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "action")
@XmlEnum
public enum Action {

    ADD,
    MODIFY,
    DELETE,
    BULK_ADD,
    BULK_MODIFY,
    BULK_DELETE,
    LOGIN,
    SIGN,
    UNSIGN,
    FAILOVER,
    FAILBACK,
    MIGRATE_SBTC_TO_AR;

    public String value() {
        return name();
    }

    public static Action fromValue(String v) {
        return valueOf(v);
    }

}

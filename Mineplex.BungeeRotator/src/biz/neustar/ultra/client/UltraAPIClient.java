package biz.neustar.ultra.client;

/*
 * User: jbodner
 * Date: 1/28/13
 * Time: 4:07 PM
 *
 * Copyright 2000-2013 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

import com.neustar.ultraservice.schema.v01.AccountDetailsList;
import com.neustar.ultraservice.schema.v01.ZoneList;

public interface UltraAPIClient {
    String createARecord(String zoneName, String domainName, String ipAddress, int ttl);

    String createTXTRecord(String zoneName, String domainName, String value, int ttl);

    String createCNAMERecord(String zoneName, String domainName, String name, int ttl);

    String createRecord(String zoneName, String domainName, ResourceRecordTypes recordType, int ttl, String... infoValues);

    String createPrimaryZone(String accountId, String zoneName);

    String deleteZone(String zoneName);

    AccountDetailsList getAccountDetailsForUser();

    String getNeustarNetworkStatus();

    ZoneList getSecondaryZonesOfAccount(String accountId);

    ZoneList getPrimaryZonesOfAccount(String accountId);

    ZoneList getAliasZonesOfAccount(String accountId);

    ZoneList getZonesOfAccount(String accountId);
}

package biz.neustar.ultra.client;

import com.neustar.ultraservice.schema.v01.AccountDetailsList;
import org.apache.commons.lang.RandomStringUtils;

/*
 * User: jbodner
 * Date: 1/30/13
 * Time: 2:39 PM
 *
 * Copyright 2000-2013 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */
public class SampleMain {
    /*
    arg pos     value
    0           wsdl url
    1           username
    2           password
    3           account id
     */
    public static void main(String[] args) {

        if (args.length < 3 || args.length > 4) {
            System.err.println("Required params: wsdlUrl userName password {accountId}");
            System.exit(1);
        }
        String wsdlUrl = args[0];
        String username = args[1];
        String password = args[2];
        String accountId = null;
        if(args.length == 4) {
            accountId = args[3];
        }

        System.out.println("url = " + wsdlUrl);
        try {
            UltraAPIClient ultraAPIClient = new UltraAPIClientImpl(username, password, wsdlUrl);
            System.out.println(ultraAPIClient.getNeustarNetworkStatus());
            AccountDetailsList accountDetailsForUser = ultraAPIClient.getAccountDetailsForUser();
            System.out.println(accountDetailsForUser.getAccountDetailsData().get(0).getAccountID());
            if (accountId == null) {
                accountId = accountDetailsForUser.getAccountDetailsData().get(0).getAccountID();
            }
            String zoneName = RandomStringUtils.randomAlphanumeric(16).toLowerCase()+".com.";
            try {
                System.out.println(ultraAPIClient.deleteZone(zoneName));
            } catch (UltraAPIException e) {
                e.printStackTrace();
                if (e.getCode() != 1801) {
                    System.exit(1);
                }
            }
            System.out.println(ultraAPIClient.createPrimaryZone(accountId, zoneName));
            System.out.println(ultraAPIClient.getSecondaryZonesOfAccount(accountId));
            System.out.println(ultraAPIClient.createARecord(zoneName, "foo."+zoneName, "1.2.3.4", 86400));
            System.out.println(ultraAPIClient.deleteZone(zoneName));
        } catch (UltraAPIException e) {
            e.printStackTrace();
        }
    }
}

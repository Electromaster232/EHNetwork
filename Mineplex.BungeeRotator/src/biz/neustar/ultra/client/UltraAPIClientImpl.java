package biz.neustar.ultra.client;

import com.neustar.ultra.api.webservice.v01.UltraDNS1;
import com.neustar.ultra.api.webservice.v01.UltraWSException_Exception;
import com.neustar.ultra.api.webservice.v01.UltraWebServiceV01Service;
import com.neustar.ultraservice.schema.v01.*;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.handler.WSHandlerConstants;

import javax.xml.ws.Service;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
 * User: jbodner
 * Date: 1/28/13
 * Time: 12:32 PM
 *
 * Copyright 2000-2013 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */
public class UltraAPIClientImpl implements UltraAPIClient {
    private final UltraDNS1 _ultraDNS1;

    public UltraAPIClientImpl(String username, String password) {
        this(username, password, UltraWebServiceV01Service.WSDL_LOCATION.toString());
    }

    public UltraAPIClientImpl(String username, String password, String url) {
        try {
            Service service = UltraWebServiceV01Service.create(new URL(url), UltraWebServiceV01Service.SERVICE);
            _ultraDNS1 = service.getPort(UltraDNS1.class);
            Client cxfClient = ClientProxy.getClient(_ultraDNS1);
            WSS4JOutInterceptor wss4JOutInterceptor = new WSS4JOutInterceptor();
            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(WSHandlerConstants.ACTION, "UsernameToken");
            properties.put(WSHandlerConstants.USER, "dummy");
            properties.put(WSHandlerConstants.PASSWORD_TYPE, "PasswordText");
            properties.put(WSHandlerConstants.PW_CALLBACK_REF, new ClientPasswordCallback(username, password));
            wss4JOutInterceptor.setProperties(properties);

            cxfClient.getOutInterceptors().add(new org.apache.cxf.interceptor.LoggingOutInterceptor());
            cxfClient.getOutInterceptors().add(wss4JOutInterceptor);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new UltraAPIException(9999, e.getMessage());
        }
    }

    @Override
    public String createARecord(String zoneName, String domainName, String ipAddress, int ttl) {
        return createRecord(zoneName, domainName, ResourceRecordTypes.A, ttl, ipAddress);
    }

    @Override
    public String createTXTRecord(String zoneName, String domainName, String value, int ttl) {
        return createRecord(zoneName, domainName, ResourceRecordTypes.TXT, ttl, value);
    }

    @Override
    public String createCNAMERecord(String zoneName, String domainName, String name, int ttl) {
        return createRecord(zoneName, domainName, ResourceRecordTypes.CNAME, ttl, name);
    }

    @Override
    public String createRecord(String zoneName, String domainName, ResourceRecordTypes recordType, int ttl, String... infoValues) {
        try {
            ResourceRecordToCreate resourceRecord = new ResourceRecordToCreate();
            resourceRecord.setDName(domainName);
            resourceRecord.setType(recordType.recType());
            resourceRecord.setZoneName(zoneName);
            resourceRecord.setTTL(Integer.toString(ttl));
            InfoValues infoValuesWrapper = new InfoValues();
            for (int i = 0; i < infoValues.length; i++) {
                setInfoValue(i, infoValues[i], infoValuesWrapper);
            }
            resourceRecord.setInfoValues(infoValuesWrapper);
            return _ultraDNS1.createResourceRecord("", resourceRecord);
        } catch (UltraWSException_Exception e) {
            throw new UltraAPIException(e.getFaultInfo());
        }
    }

    private void setInfoValue(int i, String value, InfoValues infoValuesWrapper) {
        try {
            Method m = infoValuesWrapper.getClass().getMethod("setInfo"+(i+1)+"Value", String.class);
            m.invoke(infoValuesWrapper,value);
        } catch (Exception e) {
            throw new UltraAPIException(9999, e.getMessage());
        }
    }

    @Override
    public String createPrimaryZone(String accountId, String zoneName) {
        try {
            return _ultraDNS1.createPrimaryZone("", accountId, zoneName, true);
        } catch (UltraWSException_Exception e) {
            throw new UltraAPIException(e.getFaultInfo());
        }
    }

    @Override
    public String deleteZone(String zoneName) {
        try {
            return _ultraDNS1.deleteZone("", zoneName);
        } catch (UltraWSException_Exception e) {
            throw new UltraAPIException(e.getFaultInfo());
        }
    }

    @Override
    public AccountDetailsList getAccountDetailsForUser() {
        try {
            return _ultraDNS1.getAccountDetailsOfUser("", "");
        } catch (UltraWSException_Exception e) {
            throw new UltraAPIException(e.getFaultInfo());
        }
    }

    @Override
    public String getNeustarNetworkStatus() {
        try {
            return _ultraDNS1.getNeustarNetworkStatus();
        } catch (UltraWSException_Exception e) {
            throw new UltraAPIException(e.getFaultInfo());
        }
    }

    @Override
    public ZoneList getSecondaryZonesOfAccount(String accountId) {
        return getZonesOfAccount(accountId, ZoneType.SECONDARY);
    }

    @Override
    public ZoneList getPrimaryZonesOfAccount(String accountId) {
        return getZonesOfAccount(accountId, ZoneType.PRIMARY);
    }

    @Override
    public ZoneList getAliasZonesOfAccount(String accountId) {
        return getZonesOfAccount(accountId, ZoneType.ALIAS);
    }

    @Override
    public ZoneList getZonesOfAccount(String accountId) {
        return getZonesOfAccount(accountId, ZoneType.ALL);
    }

    private ZoneList getZonesOfAccount(String accountId, ZoneType zoneType) {
        try {
            return _ultraDNS1.getZonesOfAccount(accountId, zoneType);
        } catch (UltraWSException_Exception e) {
            throw new UltraAPIException(e.getFaultInfo());
        }
    }
}

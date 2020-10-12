
package com.neustar.ultra.api.webservice.v01;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.neustar.ultra.api.webservice.v01 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UltraWSException_QNAME = new QName("http://webservice.api.ultra.neustar.com/v01/", "UltraWSException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.neustar.ultra.api.webservice.v01
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UltraWSException }
     * 
     */
    public UltraWSException createUltraWSException() {
        return new UltraWSException();
    }

    /**
     * Create an instance of {@link RestrictIP }
     * 
     */
    public RestrictIP createRestrictIP() {
        return new RestrictIP();
    }

    /**
     * Create an instance of {@link DeleteZoneStatus }
     * 
     */
    public DeleteZoneStatus createDeleteZoneStatus() {
        return new DeleteZoneStatus();
    }

    /**
     * Create an instance of {@link SecondaryZoneInfo }
     * 
     */
    public SecondaryZoneInfo createSecondaryZoneInfo() {
        return new SecondaryZoneInfo();
    }

    /**
     * Create an instance of {@link NameServerIpList }
     * 
     */
    public NameServerIpList createNameServerIpList() {
        return new NameServerIpList();
    }

    /**
     * Create an instance of {@link NameServer }
     * 
     */
    public NameServer createNameServer() {
        return new NameServer();
    }

    /**
     * Create an instance of {@link PrimaryZoneInfo }
     * 
     */
    public PrimaryZoneInfo createPrimaryZoneInfo() {
        return new PrimaryZoneInfo();
    }

    /**
     * Create an instance of {@link TransferInfo }
     * 
     */
    public TransferInfo createTransferInfo() {
        return new TransferInfo();
    }

    /**
     * Create an instance of {@link TransferStatus }
     * 
     */
    public TransferStatus createTransferStatus() {
        return new TransferStatus();
    }

    /**
     * Create an instance of {@link ZoneProperties }
     * 
     */
    public ZoneProperties createZoneProperties() {
        return new ZoneProperties();
    }

    /**
     * Create an instance of {@link AuditSummary }
     * 
     */
    public AuditSummary createAuditSummary() {
        return new AuditSummary();
    }

    /**
     * Create an instance of {@link InternalZone }
     * 
     */
    public InternalZone createInternalZone() {
        return new InternalZone();
    }

    /**
     * Create an instance of {@link PrimaryNameServers }
     * 
     */
    public PrimaryNameServers createPrimaryNameServers() {
        return new PrimaryNameServers();
    }

    /**
     * Create an instance of {@link AliasZoneInfo }
     * 
     */
    public AliasZoneInfo createAliasZoneInfo() {
        return new AliasZoneInfo();
    }

    /**
     * Create an instance of {@link Zone }
     * 
     */
    public Zone createZone() {
        return new Zone();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UltraWSException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservice.api.ultra.neustar.com/v01/", name = "UltraWSException")
    public JAXBElement<UltraWSException> createUltraWSException(UltraWSException value) {
        return new JAXBElement<UltraWSException>(_UltraWSException_QNAME, UltraWSException.class, null, value);
    }

}

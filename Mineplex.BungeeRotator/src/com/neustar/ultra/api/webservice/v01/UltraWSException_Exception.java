
package com.neustar.ultra.api.webservice.v01;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.7.7
 * 2014-01-15T10:08:40.557-05:00
 * Generated source version: 2.7.7
 */

@WebFault(name = "UltraWSException", targetNamespace = "http://webservice.api.ultra.neustar.com/v01/")
public class UltraWSException_Exception extends Exception {
    
    private com.neustar.ultra.api.webservice.v01.UltraWSException ultraWSException;

    public UltraWSException_Exception() {
        super();
    }
    
    public UltraWSException_Exception(String message) {
        super(message);
    }
    
    public UltraWSException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public UltraWSException_Exception(String message, com.neustar.ultra.api.webservice.v01.UltraWSException ultraWSException) {
        super(message);
        this.ultraWSException = ultraWSException;
    }

    public UltraWSException_Exception(String message, com.neustar.ultra.api.webservice.v01.UltraWSException ultraWSException, Throwable cause) {
        super(message, cause);
        this.ultraWSException = ultraWSException;
    }

    public com.neustar.ultra.api.webservice.v01.UltraWSException getFaultInfo() {
        return this.ultraWSException;
    }
}

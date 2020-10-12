package biz.neustar.ultra.client;

import com.neustar.ultra.api.webservice.v01.UltraWSException;

/*
 * User: jbodner
 * Date: 1/28/13
 * Time: 1:11 PM
 *
 * Copyright 2000-2013 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */
public class UltraAPIException extends RuntimeException {
    private final int _code;

    public UltraAPIException(int code, String message) {
        super(message);
        _code = code;
    }

    public UltraAPIException(UltraWSException e) {
        super(e.getErrorDescription());
        _code = e.getErrorCode();
    }

    public int getCode() {
        return _code;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", _code, getMessage());
    }
}

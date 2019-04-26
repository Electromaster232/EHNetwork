package biz.neustar.ultra.client;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

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
public class ClientPasswordCallback implements CallbackHandler {
    private final String _username;
    private final String _password;

    public ClientPasswordCallback(String username, String password) {
        _username = username;
        _password = password;
    }

    public void handle(Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {

        for (Callback callback : callbacks) {

            WSPasswordCallback pc = (WSPasswordCallback) callback;

            if (pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN) {

                //you can source the username and password from
                //other sources like login context, LDAP, DB etc

                pc.setIdentifier(_username);
                pc.setPassword(_password);
            }
        }

    }
}

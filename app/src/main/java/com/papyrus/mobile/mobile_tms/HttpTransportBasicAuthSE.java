package com.papyrus.mobile.mobile_tms;

import org.ksoap2.transport.ServiceConnectionSE;

import java.io.IOException;
import org.ksoap2.transport.HttpTransportSE;



public class HttpTransportBasicAuthSE extends HttpTransportSE {
    private String username;
    private String password;


    /**
     * Constructor with username and password
     *
     * @param url
     *            The url address of the webservice endpoint
     * @param username
     *            Username for the Basic Authentication challenge RFC 2617
     * @param password
     *            Password for the Basic Authentication challenge RFC 2617
     */
    public HttpTransportBasicAuthSE(String url, String username, String password) {
        super(url);
        this.username = username;
        this.password = password;
    }

    public ServiceConnectionSE getServiceConnection() throws IOException {
        ServiceConnectionSE midpConnection = new ServiceConnectionSE(url, 60000);
        addBasicAuthentication(midpConnection);
        return midpConnection;
    }

    protected void addBasicAuthentication(ServiceConnectionSE midpConnection) throws IOException {
        if (username != null && password != null) {
            StringBuffer buf = new StringBuffer(username);
            buf.append(':').append(password);
            byte[] raw = buf.toString().getBytes();
            buf.setLength(0);
            buf.append("Basic ");
            org.kobjects.base64.Base64.encode(raw, 0, raw.length, buf);
            midpConnection.setRequestProperty("Authorization", buf.toString());
        }
    }
}

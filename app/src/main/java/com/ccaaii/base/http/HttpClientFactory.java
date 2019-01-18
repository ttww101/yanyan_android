package com.ccaaii.base.http;

import com.ccaaii.base.log.LogLevel;
import com.ccaaii.base.log.MarketLog;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientFactory {

    private static final String TAG = "[CCAAII] HttpClientFactory";

    private static HttpClient httpClient;
    private static ClientConnectionManager connectionManager;
    
    /**
     * Defines:
     *  - the timeout until a connection is established
     *  - the timeout in milliseconds used when retrieving a ManagedClientConnection from the ClientConnectionManager.
     */
    private static final int CONNECTION_TIMEOUT = 30 * 1000;  // 30 sec
    
    /**
     * Defines the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.
     */
    private static final int DATA_WAITING_TIMEOUT = 3 * 60 * 1000; // 3 min
    
    private static final long IDLE_CONNECTIONS_CLEAN_UP = 10 * 60; // 10 minutes
    
    private static final int DEFAULT_MAX_CONNECTIONS = 30;
    
    /**
     * Return HttpClient instance that controlled via ThreadSafeClientConnManager.
     * Singleton class implemented.
     *
     * @return HttpClient thread-safe instance
     */
    public static HttpClient getHttpClient() {
    	
        if (httpClient != null) {
            connectionManager.closeExpiredConnections();
            connectionManager.closeIdleConnections(IDLE_CONNECTIONS_CLEAN_UP, TimeUnit.SECONDS);
            return httpClient;
        }
        
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, DEFAULT_MAX_CONNECTIONS);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, DATA_WAITING_TIMEOUT);
        ConnManagerParams.setTimeout(params, CONNECTION_TIMEOUT);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        try { 
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType()); 
            trustStore.load(null, null); 
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore); 
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
            schemeRegistry.register(new Scheme("https", sf, 443)); 
        } catch (Exception ex) { 
        } 
        
        connectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);
        httpClient = new DefaultHttpClient(connectionManager, params);

        return httpClient;
    }

    /**
     * Shutdown all connections (instances at HttpClient) and release memory.
     */
    public static void shutdown() {
        if (LogLevel.MARKET) {
            MarketLog.d(TAG, "shutdown()");
        }
        if (connectionManager != null) {
            connectionManager.shutdown();
            httpClient = null;
        }
    }

    /**
     * Try to close expired connections and force close idle connections to prepare for the call.
     */
    public static void prepareForCall() {
        if (LogLevel.MARKET) {
            MarketLog.d(TAG, "prepareForCall(): try to close all expired and idle connections.");
        }
        if (connectionManager != null) {
            connectionManager.closeExpiredConnections();
            connectionManager.closeIdleConnections(IDLE_CONNECTIONS_CLEAN_UP, TimeUnit.MILLISECONDS);
        }
    }
    
    public static class MySSLSocketFactory extends SSLSocketFactory {  
        SSLContext sslContext = SSLContext.getInstance("TLS");  
  
        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,  
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {  
            super(truststore);  
  
            TrustManager tm = new X509TrustManager() {  
                public void checkClientTrusted(X509Certificate[] chain, String authType)  
                        throws CertificateException {  
                }  
  
                public void checkServerTrusted(X509Certificate[] chain, String authType)  
                        throws CertificateException {  
                }  
  
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
            };  
  
            sslContext.init(null, new TrustManager[] { tm }, null);  
        }  
  
        @Override  
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)  
                throws IOException, UnknownHostException {  
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);  
        }  
  
        @Override  
        public Socket createSocket() throws IOException {  
            return sslContext.getSocketFactory().createSocket();  
        }  
    }  
}

/*
 *  Copyright 2015 The WebRTC Project Authors. All rights reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package scut.luluteam.gutils.utils.http;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Asynchronous http requests implementation.
 */
public class AsyncHttpURLConnection {
    private static final int HTTP_TIMEOUT_MS = 8000;
    private static final String HTTP_ORIGIN = ""; //AppRTC_Common.HTTP_ORIGIN;
    private final String method;
    private final String url;
    private final String message;
    private final AsyncHttpEvents events;
    private String contentType;


    private String TAG = "AsyncHttp";

    /**
     * 定义回调的接口
     * Http requests callbacks.
     */
    public interface AsyncHttpEvents {
        void onHttpError(String errorMessage);

        void onHttpComplete(String response);
    }

    static {
        disableSslVerification();
    }


    /**
     * 构造函数
     *
     * @param method
     * @param url
     * @param message
     * @param events
     */
    public AsyncHttpURLConnection(String method, String url, String message, AsyncHttpEvents events) {
        this.method = method;
        this.url = url;
        this.message = message;
        this.events = events;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     *
     */
    public void send() {
        Runnable runHttp = new Runnable() {
            public void run() {
                sendHttpMessage();
            }
        };
        new Thread(runHttp).start();
    }

    private void sendHttpMessage() {
        try {
            HttpURLConnection connection; //= (HttpURLConnection) new URL(url).openConnection();

            URL Url = new URL(url);


            connection = (HttpURLConnection) Url.openConnection();


            byte[] postData = new byte[0];
            if (message != null) {
                postData = message.getBytes("UTF-8");
            }
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setConnectTimeout(HTTP_TIMEOUT_MS);
            connection.setReadTimeout(HTTP_TIMEOUT_MS);
            // TODO(glaznev) - query request origin from pref_room_server_url_key preferences.
            connection.addRequestProperty("origin", HTTP_ORIGIN);
            /**
             * 为了防止CSRF的攻击，我们建议修改浏览器在发送POST请求的时候加上一个Origin字段.
             * 这个Origin字段主要是用来标识出最初请求是从哪里发起的。
             * 如果浏览器不能确定源在哪里，那么在发送的请求里面Origin字段的值就为空。
             */
            boolean doOutput = false;
            if (method.equals("POST")) {
                doOutput = true;
                connection.setDoOutput(true);
                connection.setFixedLengthStreamingMode(postData.length);
            }
            if (contentType == null) {
                connection.setRequestProperty("content-Type", "text/plain; charset=utf-8");
            } else {
                connection.setRequestProperty("content-Type", contentType);
            }

            // Send POST request.
            if (doOutput && postData.length > 0) {
                OutputStream outStream = connection.getOutputStream();
                outStream.write(postData);
                outStream.close();
            }

            /**
             * APM的http监控
             * */
            //HttpProxy.httpCallOnStart(url,method);
            // Get response.
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                events.onHttpError("Non-200 response to " + method + " to URL: " + url + " : "
                        + connection.getHeaderField(null));
                connection.disconnect();
                return;
            }

            InputStream responseStream = connection.getInputStream();
            String response = drainStream(responseStream);
            responseStream.close();
            connection.disconnect();
            /**
             * APM的http监控
             */
            //HttpProxy.httpCallOnFinish(url,responseCode);
            events.onHttpComplete(response);
        } catch (SocketTimeoutException e) {
            events.onHttpError("HTTP " + method + " to " + url + " timeout");
        } catch (IOException e) {
            events.onHttpError("HTTP " + method + " to " + url + " error: " + e.getMessage());
        }
    }

    // Return the contents of an InputStream as a String.
    private static String drainStream(InputStream in) {
        Scanner s = new Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    /**
     * 忽略安全证书
     */
    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String method = "POST";
        String url = "www.baidu.com";
        AsyncHttpURLConnection connection = new AsyncHttpURLConnection(method, url, "", new AsyncHttpEvents() {
            @Override
            public void onHttpError(String errorMessage) {
                System.out.println("ERROR:" + errorMessage);
            }

            @Override
            public void onHttpComplete(String response) {
                System.out.println(response);
            }
        });

    }

}

package com.uc.jtest.httpmock;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.uc.jtest.utils.JTestCollectionUtil;
import com.uc.jtest.utils.PrintUtil;

public class HttpMocker {
    private String url;
    private int port;
    private String body;
    private String contentType = "application/json";

    private Map<String, String> urlAndResponseMap;
    private HashMapDispatcher dispatcher = new HashMapDispatcher();
    private MockWebServer server = new MockWebServer();
    @Deprecated
    public static HttpMocker startJsonServer(int port, Map<String, String> urlAndResponseMap) {
        return startServer(null, port, null, null, urlAndResponseMap);
    }

    @Deprecated
    public static HttpMocker startJsonServer(String url, int port, String body) {
        return startServer(url, port, body, null, null);
    }

    @Deprecated
    public static HttpMocker startServer(int port, String body, String contentType) {
        return startServer(null, port, body, contentType, null);
    }

    @Deprecated
    public static HttpMocker startServer(int port, Map<String, String> urlAndResponseMap,
            String contentType) {
        return startServer(null, port, null, contentType, urlAndResponseMap);
    }

    @Deprecated
    public static HttpMocker startServer(String url, int port, String body, String contentType) {
        return startServer(url, port, body, contentType, null);
    }

    @Deprecated
    private static HttpMocker startServer(String url, int port, String body, String contentType,
            Map<String, String> urlAndResponseMap) {
        return new HttpMocker(url, port, body, contentType, urlAndResponseMap).startServer();
    }
    
    public static HttpMocker HttpMockerInit(){
        return new HttpMocker();
    }

    public HttpMocker port(int port) {
        this.port = port;
        return this;
    }

    public HttpMocker uri(String url) {
        this.url = url;
        return this;
    }

    public HttpMocker response(String body) {
        this.body = body;
        return this;
    }

    public HttpMocker response(String url, String body) {
        this.url = url;
        this.body = body;
        urlAndResponseMap.put(url, body);
        return this;
    }
    

    public String getUrl() {
        return url;
    }

    public int getPort() {
        return port;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getUrlAndResponseMap() {
        if(urlAndResponseMap ==null){
            urlAndResponseMap = new HashMap<String,String>();
        }
        return urlAndResponseMap;
    }

    public HttpMocker() {
    }

    public HttpMocker(String url, int port, String body, String contentType,
            Map<String, String> urlAndResponseMap) {
        this.url = url;
        this.port = port;
        this.body = body;
        if (contentType != null) {
            this.contentType = contentType;
        }
        this.urlAndResponseMap = urlAndResponseMap;
    }
    
    public static HttpMocker startServerWithMultipleResponseSequencely(int port,
            byte[]... responseBodies) {
        return startServerWithMultipleResponseSequencely(port,
                JTestCollectionUtil.getByteList(responseBodies));
    }

    public static HttpMocker startServerWithMultipleResponseSequencely(int port,
            List<byte[]> responseBodies) {
        HttpMocker mocker = new HttpMocker();
        for (int i = 0; i < responseBodies.size(); i++) {
            MockResponse login_resp = new MockResponse().setBody(responseBodies.get(i));
            mocker.server.enqueue(login_resp);
        }
        try {
            mocker.server.play(port);
        } catch (IOException e) {
            PrintUtil.print(e.getMessage(), Level.SEVERE);
        }
        return mocker;
    }

    public HttpMocker startServer() {
        if (urlAndResponseMap != null && urlAndResponseMap.size() > 0) {
            for (String url : urlAndResponseMap.keySet()) {
                dispatcher.putResponse(url, getResponse(urlAndResponseMap.get(url), contentType));
            }
        } else {
            dispatcher.putResponse(url, getResponse(body, contentType));
        }
        server.setDispatcher(dispatcher);
        try {
            server.play(port);
        } catch (Exception e) {
            PrintUtil.print("exception throws when startServer" + e.getMessage(), Level.SEVERE);
        }
        return this;
    }

    private MockResponse getResponse(String body, String contentType) {
        MockResponse response = new MockResponse().setBody(body);
        response.addHeader("Content-Type", contentType);
        return response;
    }

    public HttpMocker stopServer() {
        try {
            server.shutdown();
        } catch (Exception e) {
            PrintUtil.print("exception throws when startServer" + e.getMessage(), Level.SEVERE);
        }
        return this;
    }

}

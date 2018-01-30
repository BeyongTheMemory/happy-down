package com.top.mini.happy.down.http;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

/**
 * @author xugang
 */
@Slf4j
public class HttpClientImpl implements HttpClient {
    private Integer maxRouteCount = 10;

    private Integer maxConnectionCount = 1000;

    private Integer socketTimeout = 500000;

    private Integer connectionTimeout = 1000000;

    private CloseableHttpClient client = null;

    private static HttpClient httpClient = new HttpClientImpl();

    public HttpClientImpl() {
        init();
    }

    public static HttpClient getInstance() {
        return httpClient;
    }

    public void init() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        this.client = HttpClients.custom().
                setMaxConnPerRoute(maxRouteCount).
                setMaxConnTotal(maxConnectionCount).
                setConnectionManager(poolingHttpClientConnectionManager).
                setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build())
                .build();

    }

    @Override
    public String get(String uri) {
        Preconditions.checkArgument(uri != null, "Uri cannot be null");
        HttpGet httpGet = new HttpGet(uri);
        try {
            CloseableHttpResponse response = this.client.execute(httpGet);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String get(String uri, Params params) {
        Preconditions.checkArgument(uri != null, "Uri cannot be null");
        Preconditions.checkArgument(params != null, "Params cannot be null");

        HttpGet httpGet = getGet(uri, params);

        try {
            CloseableHttpResponse response = this.client.execute(httpGet);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String post(String uri, Params params) {
        Preconditions.checkArgument(uri != null, "Uri cannot be null");
        Preconditions.checkArgument(params != null, "Params cannot be null");

        HttpPost httpPost = getPost(uri, params);

        try {
            CloseableHttpResponse response = this.client.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CloseableHttpResponse post(String uri, Params params, byte[] content, String command) {
        Preconditions.checkArgument(uri != null, "Uri cannot be null");
        Preconditions.checkArgument(content != null, "content cannot be null");

        HttpPost httpPost = getPost(uri, content);
        httpPost.addHeader(new BasicHeader("Content-Type", String.format("application/x-%s-request", command)));
        httpPost.addHeader(new BasicHeader("Accept", String.format("application/x-%s-result", command)));
        httpPost.addHeader(new BasicHeader("Accept-Encoding", "gzip"));
        httpPost.addHeader(new BasicHeader("Host", "10.9.150.26"));
        params.getHeaders().forEach(httpPost::addHeader);
        try {
            CloseableHttpResponse response = this.client.execute(httpPost);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String post(String uri, String content) {
        Preconditions.checkArgument(uri != null, "Uri cannot be null");
        Preconditions.checkArgument(content != null, "content cannot be null");

        HttpPost httpPost = getPost(uri, content);
        httpPost.addHeader(new BasicHeader("Content-Type", "application/json;charset=utf-8"));
        try {
            CloseableHttpResponse response = this.client.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String put(String uri, Params params) {
        Preconditions.checkArgument(uri != null, "Uri cannot be null");
        Preconditions.checkArgument(params != null, "Params cannot be null");

        HttpPut httpPut = getPut(uri, params);

        try {
            CloseableHttpResponse response = this.client.execute(httpPut);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String delete(String uri) {
        Preconditions.checkArgument(uri != null, "Uri cannot be null");
        HttpDelete httpDelete = new HttpDelete(uri);

        try {
            CloseableHttpResponse response = this.client.execute(httpDelete);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String delete(String uri, Params params) {
        Preconditions.checkArgument(uri != null, "Uri cannot be null");
        Preconditions.checkArgument(params != null, "Params cannot be null");

        HttpDelete httpDelete = getDelete(uri, params);

        try {
            CloseableHttpResponse response = this.client.execute(httpDelete);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpGet getGet(String uri, Params params) {
        HttpGet httpGet = new HttpGet(uri);
        for (Header header : params.getHeaders()) {
            httpGet.setHeader(header);
        }
        String query = URI.create(uri).getQuery();
        List<NameValuePair> pairs = Lists.newArrayList();
        if (query != null) {
            pairs = URLEncodedUtils.parse(query, Charsets.UTF_8);
        }

        if (pairs.size() == 0) {
            uri += "?";
        } else {
            uri += "&";
        }
        String data = params.toString();
        if (data.length() > 0) {
            uri += data;
        }
        httpGet.setURI(URI.create(uri));
        return httpGet;
    }

    private HttpPost getPost(String uri, Params params) {
        HttpPost httpPost = new HttpPost(uri);
        for (Header header : params.getHeaders()) {
            httpPost.setHeader(header);
        }
        if (params.getContentType() == null || (
                params.getContentType() != null && params.getContentType().equals(ContentType.DEFAULT_TEXT)
        )) {
            params.setContentType(ContentType.APPLICATION_FORM_URLENCODED);
        }
        httpPost.setEntity(params.toEntity());
        return httpPost;
    }

    private HttpPost getPost(String uri, byte[] content) {
        HttpPost httpPost = new HttpPost(uri);
        HttpEntity entity;

        try {
            entity = new ByteArrayEntity(content);
            httpPost.setEntity(entity);
        } catch (Exception e) {
            log.error("http getPost error",e);
        }
        return httpPost;
    }

    private HttpPost getPost(String uri, String content) {
        HttpPost httpPost = new HttpPost(uri);
        HttpEntity entity;

        try {
            entity = new ByteArrayEntity(content.getBytes("UTF-8"));
            httpPost.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            log.error("http getPost UnsupportedEncodingException error",e);
        }
        return httpPost;
    }

    private HttpPut getPut(String uri, Params params) {
        HttpPut httpPut = new HttpPut(uri);
        for (Header header : params.getHeaders()) {
            httpPut.setHeader(header);
        }
        if (params.getContentType() == null || (
                params.getContentType() != null && params.getContentType().equals(ContentType.DEFAULT_TEXT)
        )) {
            params.setContentType(ContentType.APPLICATION_FORM_URLENCODED);
        }
        httpPut.setEntity(params.toEntity());
        return httpPut;
    }

    private HttpDelete getDelete(String uri, Params params) {
        HttpDelete httpDelete = new HttpDelete(uri);
        for (Header header : params.getHeaders()) {
            httpDelete.setHeader(header);
        }
        if (params.getContentType() == null || (
                params.getContentType() != null && params.getContentType().equals(ContentType.DEFAULT_TEXT)
        )) {
            params.setContentType(ContentType.APPLICATION_FORM_URLENCODED);
        }
        return httpDelete;
    }

    public void close() {
        try {
            this.client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

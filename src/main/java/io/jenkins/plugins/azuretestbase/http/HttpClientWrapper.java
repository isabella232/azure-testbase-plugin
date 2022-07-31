package io.jenkins.plugins.azuretestbase.http;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.AbstractHttpMessage;

import edu.umd.cs.findbugs.annotations.NonNull;
import lombok.Getter;
import lombok.Setter;

public class HttpClientWrapper {
    @NonNull
    private HttpClient client;

    // charset
    @NonNull
    @Getter
    @Setter
    private Charset charset = StandardCharsets.UTF_8;


    // empty initialize
    public HttpClientWrapper() {
        client = HttpClients.createDefault();
    }

    // specified charset
    public HttpClientWrapper(@NonNull Charset charset) {
        this();
        this.charset = charset;
    }


    // add params to url
    private String addParamsToUrl(@NonNull String url, Map<String, String> params) {
        if(params != null && params.size() > 0) {
            StringJoiner sj = new StringJoiner("&", "?", "");
            for(Map.Entry<String, String> entry : params.entrySet())
                sj.add(entry.getKey() + "=" + entry.getValue());
            url += sj.toString();
        }
        return url;
    }


    // add headers to http request
    private AbstractHttpMessage addHeaders(AbstractHttpMessage request, Map<String, String> headers) {
        if(headers != null) {
            for(Map.Entry<String, String> entry : headers.entrySet())
                request.addHeader(entry.getKey(), entry.getValue());
        }
        return request;
    }


    // get specified url and return httpresponse
    public HttpResponse get(@NonNull String url, Map<String, String> params, Map<String, String> headers) 
        throws ClientProtocolException, IOException {
        
        // add params to url
        url = addParamsToUrl(url, params);

        // construct http get request
        HttpGet httpGet = new HttpGet(url);
        httpGet = (HttpGet)addHeaders(httpGet, headers);

        // execute and return
        HttpResponse response;
        response = client.execute(httpGet);
        return response;
    }


    // post specific url with string payload and return httpresponse
    public HttpResponse post(@NonNull String url, Map<String, String> params, Map<String, String> headers, String payload) 
        throws ClientProtocolException, IOException {

        // add params to url
        url = addParamsToUrl(url, params);

        // construct http get request
        HttpPost httpPost = new HttpPost(url);
        httpPost = (HttpPost)addHeaders(httpPost, headers);

        // add body
        if(payload != null) {
            HttpEntity entity = new StringEntity(payload, charset);
            httpPost.setEntity(entity);
        }

        // execute and return
        HttpResponse response;
        response = client.execute(httpPost);
        return response;
    }


    // post specific url with List<NameValuePair> and return httpresponse
    public HttpResponse post(@NonNull String url, Map<String, String> params, Map<String, String> headers, List<? extends NameValuePair> payload) 
        throws ClientProtocolException, IOException {

        // add params to url
        url = addParamsToUrl(url, params);

        // construct http get request
        HttpPost httpPost = new HttpPost(url);
        httpPost = (HttpPost)addHeaders(httpPost, headers);

        // add body
        if(payload != null) {
            HttpEntity entity = new UrlEncodedFormEntity(payload, charset);
            httpPost.setEntity(entity);
        }

        // execute and return
        HttpResponse response;
        response = client.execute(httpPost);
        return response;
    }


    // put a file to specifed url
    public HttpResponse put(@NonNull String url, Map<String, String> params, Map<String, String> headers, File file)
        throws ClientProtocolException, IOException {

        // add params to url
        url = addParamsToUrl(url, params);

        // construct http get request
        HttpPut httpPut = new HttpPut(url);
        httpPut = (HttpPut)addHeaders(httpPut, headers);

        // add body
        if(file != null) {
            HttpEntity entity = new FileEntity(file);
            httpPut.setEntity(entity);
        }

        // execute and return
        HttpResponse response;
        response = client.execute(httpPut);
        return response;
    }


    // post specific url with string payload and return httpresponse
    public HttpResponse put(@NonNull String url, Map<String, String> params, Map<String, String> headers, String payload)
        throws ClientProtocolException, IOException {

        // add params to url
        url = addParamsToUrl(url, params);

        // construct http get request
        HttpPut httpPut = new HttpPut(url);
        httpPut = (HttpPut)addHeaders(httpPut, headers);

        // add body
        if(payload != null) {
            HttpEntity entity = new StringEntity(payload, charset);
            httpPut.setEntity(entity);
        }

        // execute and return
        HttpResponse response;
        response = client.execute(httpPut);
        return response;
    }


    // put bytes stream to specifed url
    public HttpResponse put(@NonNull String url, Map<String, String> params, Map<String, String> headers, byte[] bytes)
        throws ClientProtocolException, IOException {

        // add params to url
        url = addParamsToUrl(url, params);

        // construct http get request
        HttpPut httpPut = new HttpPut(url);
        httpPut = (HttpPut)addHeaders(httpPut, headers);

        // add body
        if(bytes != null) {
            HttpEntity entity = new ByteArrayEntity(bytes);
            httpPut.setEntity(entity);
        }

        // execute and return
        HttpResponse response;
        response = client.execute(httpPut);
        return response;
    }
}

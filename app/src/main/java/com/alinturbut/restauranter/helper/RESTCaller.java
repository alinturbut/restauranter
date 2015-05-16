package com.alinturbut.restauranter.helper;

import android.util.Log;

import com.alinturbut.restauranter.model.HttpRequestMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.alinturbut.restauranter.helper.HttpConstants.CONNECTION_TIMEOUT;
import static com.alinturbut.restauranter.helper.HttpConstants.READ_TIMEOUT;

/**
 * @author alinturbut.
 */
public class RESTCaller {
    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> header;
    private String url;
    private HttpRequestMethod reguestMethod;

    public static RESTCaller create() {
        return new RESTCaller();
    }

    public RESTCaller() {
        this.params = new ArrayList<>();
        this.header = new ArrayList<>();
    }

    public void addParam(String name, String value) {
        this.params.add(new BasicNameValuePair(name, value));
    }

    public void addHeader(String name, String value) {
        this.header.add(new BasicNameValuePair(name, value));
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHttpRequestMethod(HttpRequestMethod method) {
        this.reguestMethod = method;
    }

    /**
     * Static method to call a simple(with no params) HTTP_GET in order to get a JSON back
     * @param url
     * @return {@link org.json.JSONObject}
     */
    public static JSONObject callGetByUrl(String url) {
        HttpURLConnection httpClient = null;
        try {
            URL urlToCall = new URL(url);
            httpClient = (HttpURLConnection) urlToCall.openConnection();
            httpClient.setConnectTimeout(CONNECTION_TIMEOUT);
            httpClient.setReadTimeout(READ_TIMEOUT);
            httpClient.setRequestMethod(HttpRequestMethod.GET.getValue());
            httpClient.connect();

            JSONObject responseObject = new JSONObject();
            int statusCode = httpClient.getResponseCode();
            responseObject.put("responseCode", statusCode);
            if(statusCode != HttpURLConnection.HTTP_OK) {
                Log.i("RESTCaller", "Response at url:" + url + " was not OK");
                return responseObject;
            }

            InputStream in = new BufferedInputStream(httpClient.getInputStream());
            responseObject = new JSONObject(getResponseText(in));
            in.close();

            return responseObject;

        } catch (MalformedURLException e) {
            Log.e("RESTCaller", "Url:"+ url + " is not valid");
        } catch (IOException e) {
            Log.e("RESTCaller", "Could not open connection for the url:" + url);
        } catch (JSONException e) {
            Log.e("RESTCaller", "Error at creating the JSON object");
        }

        return null;
    }

    public JSONObject executeCall() {
        if(this.url != null && !this.url.isEmpty() && this.reguestMethod != null) {
            return this.executeCall(url, reguestMethod);
        }

        return null;
    }

    public JSONObject executeCall(String url, HttpRequestMethod requestMethod) {
        HttpURLConnection httpClient = null;
        try {
            URL urlToCall;

            if(HttpRequestMethod.GET.equals(requestMethod)) {
                url += "?" + getParamsQuery(params);
                urlToCall = new URL(url);
                httpClient = (HttpURLConnection) urlToCall.openConnection();
                httpClient.setConnectTimeout(CONNECTION_TIMEOUT);
                httpClient.setReadTimeout(READ_TIMEOUT);
                httpClient.setRequestMethod(requestMethod.getValue());
            } else {
                urlToCall = new URL(url);
                httpClient = (HttpURLConnection) urlToCall.openConnection();
                httpClient.setConnectTimeout(CONNECTION_TIMEOUT);
                httpClient.setReadTimeout(READ_TIMEOUT);
                httpClient.setRequestMethod(requestMethod.getValue());
                httpClient.setDoInput(true);
                httpClient.setDoOutput(true);
                if (!params.isEmpty()) {
                    OutputStream os = httpClient.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getParamsQuery(params));
                    writer.flush();
                    writer.close();
                    os.close();
                }
            }
            httpClient.connect();

            JSONObject responseObject = new JSONObject();
            int statusCode = httpClient.getResponseCode();
            responseObject.put("responseCode", statusCode);
            if(statusCode != HttpURLConnection.HTTP_OK) {
                Log.i("RESTCaller", "Response at url:" + url + " was not OK");
                return responseObject;
            }

            InputStream in = new BufferedInputStream(httpClient.getInputStream());
            responseObject = new JSONObject(getResponseText(in));
            responseObject.put("responseCode", statusCode);
            in.close();

            return responseObject;

        } catch (MalformedURLException e) {
            Log.e("RESTCaller", "Url:"+ url + " is not valid");
        } catch (IOException e) {
            Log.e("RESTCaller", "Could not open connection for the url:" + url);
        } catch (JSONException e) {
            Log.e("RESTCaller", "Error at creating the JSON object");
        }

        //TODO: verify if this is generic enough
        return null;
    }

    private static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }

    private String getParamsQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first) {
                first = false;
            }
            else {
                result.append("&");
            }

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

package com.raulete.androidweaponry;


import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import mobi.kinetica.boltio.app.app.AppConfig;

public class HttpUtil {

    private static HttpClient httpclient;

    private static String TAG = "HttpUtil";

    public static String EMPTY_REPONSE = "empty";

    public static HttpClient getHttpClient(){
        if (HttpUtil.httpclient == null)
            HttpUtil.httpclient = new DefaultHttpClient();
        return HttpUtil.httpclient;
    }

    public static String httpGetRequest(String paramString) throws IOException{
        HttpClient localHttpClient = getHttpClient();
        String responseString = EMPTY_REPONSE;
        try{
            HttpResponse localHttpResponse = localHttpClient.execute(new HttpGet(paramString));
            responseString = response2String(localHttpResponse);
        } catch (ClientProtocolException localClientProtocolException){
            AppConfig.log(TAG, "ClientProtocolException : " + localClientProtocolException.getMessage());
        } catch (IOException localIOException){
            AppConfig.log(TAG, "IOException : " + localIOException.getMessage());
            throw localIOException;
        } catch (Exception localException){
            AppConfig.log(TAG, "Exception : " + localException.toString());
            AppConfig.log(TAG, "Exception Message : " + localException.getMessage());
        }
        return responseString;
    }

    public static String httpPostRequest(String paramString, List<NameValuePair> paramList){
        HttpClient localHttpClient = getHttpClient();
        String responseString = EMPTY_REPONSE;
        try{
            HttpPost localHttpPost = new HttpPost(paramString);
            localHttpPost.setEntity(new UrlEncodedFormEntity(paramList));
            HttpResponse localHttpResponse = localHttpClient.execute(localHttpPost);
            responseString = response2String(localHttpResponse);
        } catch (ClientProtocolException localClientProtocolException){
            AppConfig.log(TAG, "ClientProtocolException : " + localClientProtocolException.getMessage());
        } catch (IOException localIOException){
            AppConfig.log(TAG, "IOException : " + localIOException.getMessage());
        } catch (Exception localException){
            AppConfig.log(TAG, "Exception : " + localException.toString());
            AppConfig.log(TAG, "Exception Message : " + localException.getMessage());
        }
        return responseString;
    }

    public static String httpPutRequest(String paramString, List<NameValuePair> paramList){
        HttpClient localHttpClient = getHttpClient();
        String responseString = EMPTY_REPONSE;
        try{
            HttpPut localHttpPut = new HttpPut(paramString);
            localHttpPut.setEntity(new UrlEncodedFormEntity(paramList));
            HttpResponse localHttpResponse = localHttpClient.execute(localHttpPut);
            responseString = response2String(localHttpResponse);
        } catch (ClientProtocolException localClientProtocolException){
            AppConfig.log(TAG, "ClientProtocolException : " + localClientProtocolException.getMessage());
        } catch (IOException localIOException){
            AppConfig.log(TAG, "IOException : " + localIOException.getMessage());
        } catch (Exception localException){
            AppConfig.log(TAG, "Exception : " + localException.toString());
            AppConfig.log(TAG, "Exception Message : " + localException.getMessage());
        }
        return responseString;
    }

    public static String response2String(HttpResponse httpResponse){
        HttpEntity r_entity = httpResponse.getEntity();
        String xmlString = null;
        try{
            xmlString = new String(EntityUtils.toString(r_entity));
        }catch(Exception localException){
            AppConfig.log(TAG, "Exception : " + localException.getMessage());
        }
        return xmlString;
    }

    public static boolean saveBitmap(String url, String folder, String filename){
        String folderPath = AppConfig.getAppExternalStorageDirectory();
        String filePath = folderPath + filename;
        File f = new File(filePath);
        if(f.exists() && f.length() != 0) { return true; }
        Bitmap image = downloadBitmap(url);
        if(image == null){ return false; }
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            image.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e){}
        image.recycle();
        return true;
    }

    public static Bitmap downloadBitmap(String url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    //final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return null;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url, e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }

    public static InputStream downloadVideo(String url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    return inputStream;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving video from " + url, e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }


}

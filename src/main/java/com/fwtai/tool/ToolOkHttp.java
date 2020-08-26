package com.fwtai.tool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 请求处理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-01-08 18:28
 * @QQ号码 444141300
 * @Email service@fwtai.com
 * @官网 http://www.fwtai.com
 */
public final class ToolOkHttp {

    /**GET异步请求,无参数或参数在url里*/
    public final static void ajaxGet(final String url,final Callback callback){
        final OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(6000,TimeUnit.MILLISECONDS).connectTimeout(6000,TimeUnit.MILLISECONDS).build();
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    /**GET同步请求,无参数或参数在url里*/
    public final static Response ajaxGet(final String url) throws IOException{
        final OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(6000,TimeUnit.MILLISECONDS).connectTimeout(6000,TimeUnit.MILLISECONDS).build();
        final Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute();
    }

    /**POST异步请求,无参数或参数在url里*/
    public final static void ajaxPost(final String url,final Callback callback){
        final Request request = postRequest(url);
        final OkHttpClient client = new OkHttpClient.Builder().build();
        client.newCall(request).enqueue(callback);
    }

    /**POST同步请求,无参数或参数在url里*/
    public final static Response ajaxPost(final String url)throws IOException{
        final Request request = postRequest(url);
        final OkHttpClient client = new OkHttpClient.Builder().build();
        return client.newCall(request).execute();
    }

    /**POST同步请求,有参数请求*/
    public final static Response ajaxPost(final String url,final HashMap<String,String> params)throws IOException{
        final Request request = postRequest(url,params);
        final OkHttpClient client = new OkHttpClient.Builder().build();
        return client.newCall(request).execute();
    }

    /**POST异步请求,有参数请求-推荐*/
    public final static void ajaxPost(final String url,final HashMap<String,String> params,final Callback callback){
        final Request request = postRequest(url,params);
        final OkHttpClient client = new OkHttpClient.Builder().build();
        client.newCall(request).enqueue(callback);
    }

    /**组装POST请求参数-不带请求参数*/
    private final static Request postRequest(final String url){
        return new Request.Builder().url(url).post(new FormBody.Builder().build()).build();
    }

    /**组装POST请求参数,适用于form表单提交,有带请求参数*/
    private final static Request postRequest(final String url,final HashMap<String,String> params){
        final FormBody.Builder builder = new FormBody.Builder();
        if(params != null && params.size() > 0){
            for (final Entry<String,String> entry : params.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        return new Request.Builder().url(url).post(builder.build()).build();
    }

    /**解析请求返回的数据体*/
    public final static String parseResponse(final Response response)throws Exception{
        return response.body().string();
    }

    /**
     * 构造通用的get-request,带请求头的
     * @param url 请求路径
     * @param headerMap 请求头key-value
    */
    public final static Request getRequestHeader(final String url,final HashMap<String,String> headerMap){
        final Request.Builder builder = new Request.Builder();
        if(headerMap != null && headerMap.size() > 0){
            final Headers headers = Headers.of(headerMap);
            return builder.get().url(url).headers(headers).build();
        }else{
            return builder.get().url(url).build();
        }
    }

    /**
     * 组装POST请求参数,适用于form表单提交,有带请求头和请求参数
     * @param url 请求的路径
     * @param params 请求的参数
     * @param headerMap 请求头key-value
    */
    protected final static Request postRequestHeader(final String url,final HashMap<String,String> params,final HashMap<String,String> headerMap){
        final FormBody.Builder builder = new FormBody.Builder();
        if(params != null && params.size() > 0){
            for (final Entry<String,String> entry : params.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        if(headerMap != null && headerMap.size() > 0){
            final Headers headers = Headers.of(headerMap);
            return new Request.Builder().url(url).post(builder.build()).headers(headers).build();
        } else {
            return new Request.Builder().url(url).post(builder.build()).build();
        }
    }

    /**
     * POST同步请求,支持请求体和请求头
     * @param object 可以是实体|map|HashMapPOST同步请求,支持请求体和请求头
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019年3月22日 18:54:58
    */
    public static String post(final String url,final HashMap<String,String> headerMap,String contentType,final Object object) throws Exception{
        Request.Builder builder = new Request.Builder().url(url);
        if(headerMap != null && headerMap.size() > 0){
            Headers headers = Headers.of(headerMap);
            builder = new Request.Builder().url(url).headers(headers);
        }
        Request request = builder.post(new FormBody.Builder().build()).build();
        if(contentType == null || contentType.length() <= 0){
            contentType = "application/json";
        }
        if(object != null && object.toString().length() >0){
            final String data = JSONObject.parseObject(JSON.toJSONString(object)).toJSONString();
            final RequestBody requestBody = RequestBody.create(MediaType.parse(contentType),data);
            request = builder.post(requestBody).build();
        }
        return new OkHttpClient().newCall(request).execute().body().string();
    }
}
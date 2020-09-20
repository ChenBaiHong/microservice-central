package com.baimicro.central.common.httpclient;

import com.baimicro.central.common.model.Result;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @project:
 * @author: chen.baihoo
 * @date: 2019年4月29日00点46分
 * @Description: TODO
 * version 0.1
 */
@Component
@Slf4j
public class RestClient {

    @Resource
    private CloseableHttpClient httpClient;
    @Resource
    private RequestConfig requestConfig;

    /**
     * @Author baiHoo
     * @Description //TODO
     * @Date 18:30 2019/6/20
     **/
    public String doPostJson(String url, Map<String, Object> param) throws Exception {
        // 1. 创建 Post 请求
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        Gson gson = new Gson();
        // 2. 转换成JSON格式
        if (param != null) {
            List<NameValuePair> paramList = new ArrayList<>();
            param.keySet().forEach(e -> {
                paramList.add(new BasicNameValuePair(e, gson.toJson(param.get(e))));
            });
            // 模拟表单
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
            httpPost.setEntity(entity);
        }
        // 2. 设置请求配置 及 响应模型
        // 2.1 设置 请求配置
        httpPost.setConfig(requestConfig);
        // 2.2 设置响应模型
        CloseableHttpResponse httpResponse = null;

        // 2.3.1 由客户端执行(发送)Post请求
        httpResponse = httpClient.execute(httpPost);
        // 2.3.2 从响应模型中获取响应实体
        HttpEntity responseEntity = httpResponse.getEntity();
        // 2.3.3 获取响应状态
        httpResponse.getStatusLine();

        return EntityUtils.toString(responseEntity, "utf-8");

    }

    /***
     *
     * @Author baihoo.chen
     * @Description TODO
     * @Date 2019/8/9 9:16
     * @Param [url, header, request]
     * @return Result
     **/
    public <T> Result doPost(String url, T bean) throws Exception {
        // 1. 创建 Post 请求
        URIBuilder uriBuilder = new URIBuilder(url);
        HttpPost httpPost = new HttpPost();
        // 2. 实体解析
        if (bean != null) {
            ArrayList<BasicNameValuePair> list = new ArrayList<>();
            Class clazz = bean.getClass();
            Field[] fields = clazz.getFields();
            Arrays.stream(fields).forEach(field -> {
                field.setAccessible(true);
                try {
                    Object value = field.get(bean);
                    if (value != null) {
                        list.add(new BasicNameValuePair(field.getName(), value.toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("请求出错了~！{}", e.getMessage());
                }
            });
            // 2.1 实际解析
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost.setURI(uriBuilder.build());

            // 3 设置 请求配置
            httpPost.setConfig(requestConfig);
            // 3.1 设置响应模型
            CloseableHttpResponse httpResponse = null;

            // 3.2 由客户端执行(发送)Post请求
            httpResponse = httpClient.execute(httpPost);
            // 3.3 从响应模型中获取响应实体
            HttpEntity responseEntity = httpResponse.getEntity();
            // 3.4 获取响应状态
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                String responseJson = EntityUtils.toString(responseEntity);
                Gson gson = new Gson();
                return gson.fromJson(responseJson, Result.class);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String doPostJson(String url, String json) throws Exception {
        // 1. 创建 Post 请求
        HttpPost httpPost = new HttpPost(url);
        // 2. 设置亲求字符集
        StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        stringEntity.setContentType("application/json;charset=utf8");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(stringEntity);
        // 3. 设置请求配置 及 响应模型
        // 3.1 设置 请求配置
        httpPost.setConfig(requestConfig);
        // 3.2 设置响应模型
        // 3.3.1 由客户端执行(发送)Post请求
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        // httpResponse.setHeader("Content-Type", "text/html; charset=UTF-8");
        // 3.3.2 从响应模型中获取响应实体
        HttpEntity responseEntity = httpResponse.getEntity();
        // 3.3.3 获取响应状态
        httpResponse.getStatusLine();
        String responseText = EntityUtils.toString(responseEntity, "utf-8");
        log.info("httpClient请求的数据：{}", responseText);
        return responseText;
    }

    /**
     * @Author baiHoo
     * @Description //TODO
     * @Date 18:46 2019/6/20
     **/
    public String doGet(String url, Map<String, Object> param) throws Exception {
        // 1. 创建uri
        Gson gson = new Gson();
        URIBuilder builder = new URIBuilder(url);
        if (param != null) {
            param.keySet().forEach(e -> {
                builder.addParameter(e, gson.toJson(param.get(e)));
            });
        }
        URI uri = builder.build();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(uri);
        // 执行请求
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        // 判断返回状态是否为200
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String responseText = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            log.info("httpClient请求的数据：{}", responseText);
            return responseText;
        }
        return null;
    }

    public String doGet(String url) throws Exception {
        return doGet(url, null);
    }

    public Result doGetResult(String url) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(doGet(url, null), Result.class);
    }

    public <T> T get(String url, Class<T> clazz) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(doGet(url), clazz);
    }
}
